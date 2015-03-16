package com.visosoft.tvshowinfo.service.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.ShowUpdater;
import com.visosoft.tvshowinfo.service.ShowUpdaterResolverService;
public class ShowUpdaterResolverServiceImpl implements
		ShowUpdaterResolverService {

	private Map<String, ShowUpdater> updaters;
	
	private static final Logger logger = LoggerFactory.getLogger(ShowUpdaterResolverServiceImpl.class);
	
	@Override
	public ShowUpdater resolve(Show show) {
		return findUpdater(show);
	}
	
	@Override
	public ShowUpdater resolve(String updaterID) {
		return updaters.get(updaterID);
	}
	
	@Override
	public Collection<ShowUpdater> getAllUpdaters() {
		return updaters.values();
	}

	private ShowUpdater findUpdater(Show show) {
		ShowUpdater su = getFromUpdaters(show);
		return su;
	}

	private ShowUpdater getFromUpdaters(Show show) {
		for (Map.Entry<String, ShowUpdater> u : updaters.entrySet()) {
			if (show.getUrl().contains(u.getKey())) {
				return u.getValue();
			}
		}
		logger.info("Didn't find updater for show: " + show);
		return null;
	}

	@Override
	public void setUpdaters(Map<String, ShowUpdater> updaters) {
		this.updaters = updaters;
	}

}
