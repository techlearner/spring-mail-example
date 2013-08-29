package com.lsi.mailclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public final class MailClient implements MailSender {

	private String userName;

	private String passWord;

	private String hostName;

	private Integer portNo;

	private boolean smtpAuth = true;

	private boolean tlsEnable = true;
	
	private static Map<String, Object> lastMessages = new HashMap<>();

	public MailClient() {

	}

	private JavaMailSenderImpl sender;

	@SuppressWarnings("deprecation")
	public String validateAndSendMail(EmailObject emailObject) {
		if(emailObject!=null) {
			String body = emailObject.getBody();
			Date lastSent = (Date)lastMessages.get(body);
			Date currentTime = new Date();
			int hours = currentTime.getHours();
			currentTime.setHours(hours - Constants.MAIL_RESENT_DELAY);
			if(lastSent!=null) {
				if(currentTime.compareTo(lastSent)>=0) {
					sendMail(emailObject);
					return Constants.SUCCESS_MSG;

				} else {
					return Constants.RETRY_MSG;
				}
			} else {
				sendMail(emailObject);
			}
		} 
		return Constants.FAILURE_MSG;
	}
	
	
	public Map<String, Object> delete(Map<String, Object> oldMessages) {
		if(oldMessages !=null) {
			Date currentDate = new Date();
			int date = currentDate.getDate();
			currentDate.setDate(date - 1);
			for(String key:oldMessages.keySet()) {
				Date lastSent = (Date)oldMessages.get(key);
				if(currentDate.compareTo(lastSent)>0){
					oldMessages.remove(key);
				}
			}
		}
		return oldMessages;
	}
	
	private void sendMail(EmailObject emailObject) {
		String body = emailObject.getBody();
		MimeMessage message = getSender().createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(emailObject.getFrom());
			for(String to : emailObject.getTo())
				helper.addTo(to);
			List<String> mailList = emailObject.getBcc();
			if(null != mailList) {
				for(String bcc : mailList)
					helper.addBcc(bcc);	
			}
			mailList = emailObject.getCc();
			if(null != mailList) {
				for(String cc : mailList)
					helper.addCc(cc);
			}
			helper.setSubject(emailObject.getSubject());
			helper.setText(emailObject.getBody());

			if (emailObject.getAttachmentLocation() != null) {
				FileSystemResource file = new FileSystemResource(emailObject.getAttachmentLocation());
				helper.addAttachment(file.getFilename(), file);
			}

		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		lastMessages.put(body, new Date());
		getSender().send(message);
		delete(lastMessages);
	}

	public JavaMailSenderImpl getSender() {
		if(sender == null) {
			sender = new JavaMailSenderImpl();
			sender.setHost(hostName);
			sender.setPort(portNo);
			sender.setUsername(userName);
			sender.setPassword(passWord);
			Properties javaMailProperties = new Properties();
			javaMailProperties.put("mail.smtp.auth", smtpAuth);
			javaMailProperties.put("mail.smtp.starttls.enable", tlsEnable);
			sender.setJavaMailProperties(javaMailProperties);

		}

		return sender;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public void setPortNo(Integer portNo) {
		this.portNo = portNo;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public void setTlsEnable(boolean tlsEnable) {
		this.tlsEnable = tlsEnable;
	}
}
