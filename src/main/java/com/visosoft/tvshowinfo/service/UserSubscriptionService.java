package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;
import com.visosoft.tvshowinfo.util.ShowSearchResult;

public interface UserSubscriptionService {
	
	void insert(UserSubscription s);
    
    UserSubscription update(UserSubscription s);
    
    void delete(UserSubscription s);
    List<UserSubscription> selectAll(User user);
    
    List<UserSubscription> selectAll(Show show);

	UserSubscription select(long subsId);

	List<ShowSearchResult> searchShow(String showName);

	String generateSubscriptionUrl(ShowSearchResult ssr);
}
