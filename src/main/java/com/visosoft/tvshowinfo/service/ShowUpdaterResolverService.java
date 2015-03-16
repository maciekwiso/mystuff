package com.visosoft.tvshowinfo.service;

import java.util.Collection;
import java.util.Map;

import com.visosoft.tvshowinfo.domain.Show;

public interface ShowUpdaterResolverService {
	ShowUpdater resolve(Show show);
	/**
	 * 
	 * @param updaters URL => class name
	 */
	void setUpdaters(Map<String, ShowUpdater> updaters);
	
	ShowUpdater resolve(String updaterID);
	
	Collection<ShowUpdater> getAllUpdaters();
}
