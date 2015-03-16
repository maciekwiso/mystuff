package com.visosoft.tvshowinfo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="user_subscriptions", uniqueConstraints= @UniqueConstraint(columnNames={"user_id", "show_id"}))
public class UserSubscription {
	
	private Long id;
	private User user;
	private Show show;
	private Boolean enabled;
	private Boolean emailEnabled;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usersub_id")
	public Long getId() {
		return id;
	}
	private void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne(targetEntity = Show.class)
    @JoinColumn(name = "show_id", nullable = false)
	public Show getShow() {
		return show;
	}
	public void setShow(Show show) {
		this.show = show;
	}
	@Column(name = "usersub_enabled")
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	@Column(name = "usersub_email_enabled")
	public Boolean getEmailEnabled() {
		return emailEnabled;
	}
	public void setEmailEnabled(Boolean enabled) {
		this.emailEnabled = enabled;
	}
}
