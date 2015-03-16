package com.visosoft.tvshowinfo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="user_roles")
public class UserRole {
	
	public enum Role { USER, ADMIN };
	
	private Long id;
	private User user;
	private Role role;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
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
	@Enumerated(EnumType.STRING)
	@Column(name = "role_value", nullable = false)
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
