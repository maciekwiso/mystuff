package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.util.ShowSearchResult;

public interface ShowUpdater {
	boolean updateShow(Show show);
	
	List<ShowSearchResult> searchShow(String showName);
	
	String generateSubscriptionLink(ShowSearchResult show);
}
