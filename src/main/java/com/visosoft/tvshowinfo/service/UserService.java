package com.visosoft.tvshowinfo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserRole;
import com.visosoft.tvshowinfo.domain.UserRole.Role;

public interface UserService extends UserDetailsService {
	
	void insert(User s);
	
    User update(User s);
    
    void delete(User s);
    
    User selectOne(Long id);
    
    User selectOne(String username);
    
    void addRole(UserRole role);
    
    void deleteRole(UserRole role);
    
    List<UserRole> selectRoles(User user);
    
    List<User> selectAllByRole(Role role);

	List<User> selectAll();
}
