package com.lsi.mailclient;

import java.util.List;

public class EmailObject {

	private String from;

	private List<String> to;

	private String subject;

	private String body;

	private List<String> cc;

	private List<String> bcc;

	private String attachmentLocation;

	public EmailObject() {

	}

	public EmailObject(String from, List<String> to, String subject, String body, List<String>cc,
			 	        List<String>bcc, String attachmentLocation) {
			this.from  = from;
			this.attachmentLocation = attachmentLocation;
			this.bcc = bcc;
			this.cc = cc;
			this.to = to;
			this.body = body;
			this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getAttachmentLocation() {
		return attachmentLocation;
	}

	public void setAttachmentLocation(String attachmentLocation) {
		this.attachmentLocation = attachmentLocation;
	}

}
