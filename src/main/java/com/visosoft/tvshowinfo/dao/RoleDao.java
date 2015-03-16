package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.UserRole;
import com.visosoft.tvshowinfo.domain.User;

public interface RoleDao {
	void insert(UserRole s);
    UserRole update(UserRole s);
    void delete(UserRole s);
    List<UserRole> selectAll(User user);
    UserRole selectOne(Long id);
}
