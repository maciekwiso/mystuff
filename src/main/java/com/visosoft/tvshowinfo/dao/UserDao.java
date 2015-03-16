package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserRole.Role;

public interface UserDao {
	void insert(User s);
    
    public User update(User s);
    
    public void delete(User s);
    public List<User> selectAll();
    public List<User> selectAll(Role role);
    
    public User selectOne(Long id);
    
    public User selectOne(String username);
}
