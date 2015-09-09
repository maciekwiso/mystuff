package com.visosoft.tvshowinfo.service;

import com.visosoft.tvshowinfo.domain.User;

public interface UserSubscriptionShowInfoGeneratorService {
	
	String generateShowsInfo(User user, boolean withLink);
}
