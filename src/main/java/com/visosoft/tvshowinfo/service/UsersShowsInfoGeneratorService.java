package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.UserSubscription;
import com.visosoft.tvshowinfo.util.ShowsInfoGeneratedData;

public interface UsersShowsInfoGeneratorService {
	
	ShowsInfoGeneratedData generateData(List<UserSubscription> subs);
}
