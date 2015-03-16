package com.visosoft.tvshowinfo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visosoft.tvshowinfo.dao.RoleDao;
import com.visosoft.tvshowinfo.dao.UserDao;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserRole;
import com.visosoft.tvshowinfo.domain.UserRole.Role;
import com.visosoft.tvshowinfo.service.UserService;
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User u = userDao.selectOne(username);
		if (u == null) {
			throw new UsernameNotFoundException("No user with username: " + username);
		}
		org.springframework.security.core.userdetails.User userd
			= new org.springframework.security.core.userdetails.User(
					username, u.getPassword(), getAuthorities(u)); 
		return userd;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		List<UserRole> roles = selectRoles(user);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (UserRole role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
		}
		return authorities;
	}

	@Override
	public void insert(User s) {
		userDao.insert(s);
	}

	@Override
	public User update(User s) {
		return userDao.update(s);
	}

	@Override
	public void delete(User s) {
		userDao.delete(s);
	}

	@Override
	public User selectOne(Long id) {
		return userDao.selectOne(id);
	}

	@Override
	public void addRole(UserRole role) {
		roleDao.insert(role);
	}

	@Override
	public void deleteRole(UserRole role) {
		roleDao.delete(role);
	}

	@Override
	public List<UserRole> selectRoles(User user) {
		return roleDao.selectAll(user);
	}

	@Override
	public List<User> selectAllByRole(Role role) {
		return userDao.selectAll(role);
	}

	@Override
	public User selectOne(String username) {
		return userDao.selectOne(username);
	}

	@Override
	public List<User> selectAll() {
		return userDao.selectAll();
	}
}
