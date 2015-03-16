package com.visosoft.tvshowinfo.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="email_tasks")
public class EmailTask implements Serializable {
	private static final long serialVersionUID = 7791491395280139489L;
	private Long id;
	private String subject;
	private String content;
	private String address;
	private String name;
	private Integer priority;
	private Date startNotSoonerThan;
	private Integer attempts;
	private String lastError;
	private EmailTaskStatus status;
	private Date added;
	private Date lastAttempt;
	private Date whenDone;
	private String execId;
	private Integer execCount;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_id")
	public Long getId() {
		return id;
	}
	private void setId(Long id) {
		this.id = id;
	}
	@Column(name = "email_subject", nullable = false)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Column(name = "email_content", nullable = false)
	@Lob
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "email_address", nullable = false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "email_name", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "email_priority", nullable = false)
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	@Column(name = "email_start_not_sooner_than", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getStartNotSoonerThan() {
		return startNotSoonerThan;
	}
	public void setStartNotSoonerThan(Date startNotSoonerThan) {
		this.startNotSoonerThan = startNotSoonerThan;
	}
	@Column(name = "email_attempts", nullable = false)
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	@Column(name = "email_last_error", nullable = false)
	@Lob
	public String getLastError() {
		return lastError;
	}
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "email_status", nullable = false)
	public EmailTaskStatus getStatus() {
		return status;
	}
	public void setStatus(EmailTaskStatus status) {
		this.status = status;
	}
	@Column(name = "email_added", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getAdded() {
		return added;
	}
	public void setAdded(Date added) {
		this.added = added;
	}
	@Column(name = "email_last_attempt", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getLastAttempt() {
		return lastAttempt;
	}
	public void setLastAttempt(Date lastAttempt) {
		this.lastAttempt = lastAttempt;
	}
	@Column(name = "email_when_done", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getWhenDone() {
		return whenDone;
	}
	public void setWhenDone(Date whenDone) {
		this.whenDone = whenDone;
	}
	@Column(name = "email_exec_id", nullable = false)
	public String getExecId() {
		return execId;
	}
	public void setExecId(String execId) {
		this.execId = execId;
	}
	@Column(name = "email_exec_count", nullable = false)
	public Integer getExecCount() {
		return execCount;
	}
	public void setExecCount(Integer execCount) {
		this.execCount = execCount;
	}
	@Override
	public String toString() {
		return "EmailTask [id=" + id + ", subject=" + subject + ", content="
				+ content + ", address=" + address + ", name=" + name
				+ ", priority=" + priority + ", startNotSoonerThan="
				+ startNotSoonerThan + ", attempts=" + attempts
				+ ", lastError=" + lastError + ", status=" + status
				+ ", added=" + added + ", lastAttempt=" + lastAttempt
				+ ", whenDone=" + whenDone + ", execId=" + execId
				+ ", execCount=" + execCount + "]";
	}
}
