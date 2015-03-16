package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;

public interface UserSubscriptionDao {
	void insert(UserSubscription s);
    UserSubscription update(UserSubscription s);
    void delete(UserSubscription s);
    List<UserSubscription> selectAll(User user);
    List<UserSubscription> selectAll(Show show);
    List<UserSubscription> selectActiveSubscriptions();
    List<UserSubscription> selectActiveEmailSubscriptions();
    UserSubscription selectOne(Long id);
	void delete(Show s);
}
