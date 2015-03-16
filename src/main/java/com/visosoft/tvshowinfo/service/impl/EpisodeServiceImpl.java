package com.visosoft.tvshowinfo.service.impl;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.EpisodeService;
@Service
@Transactional
public class EpisodeServiceImpl implements EpisodeService {
	@Autowired
	private EpisodeDao episodeDao;
	private static final Logger logger = LoggerFactory.getLogger(EpisodeServiceImpl.class);
	@Override
	public void insert(Episode s) {
		logger.debug("inserting: " + s);
		episodeDao.insert(s);
	}

	@Override
	public Episode update(Episode s) {
		logger.debug("update: " + s);
		return episodeDao.update(s);
	}

	@Override
	public void delete(Episode s) {
		logger.debug("delete: " + s);
		episodeDao.delete(s);
	}

	@Override
	public void deleteByShow(Show show) {
		logger.debug("delete by show: " + show);
		episodeDao.deleteByShow(show);
	}

	@Override
	public List<Episode> selectAllByShow(Show show) {
		logger.debug("selectallbyshow: " + show);
		return episodeDao.selectAllByShow(show);
	}

	@Override
	public List<Episode> selectAllByShow(Show show, boolean onlyNew) {
		logger.debug("selectallbyshow: " + show + "; only new: " + onlyNew);
		return episodeDao.selectAllByShow(show, onlyNew);
	}

	@Override
	public List<Episode> selectEpisodesFromDays(int daysIntoPast, int daysIntoFuture, Collection<Show> shows) {
		logger.debug("selectepisodes from days into the past: " + daysIntoPast + "; into the future: " + daysIntoFuture);
		return episodeDao.selectEpisodesFromDays(daysIntoPast, daysIntoFuture, shows);
	}

	@Override
	public List<Episode> selectNewEpisodes() {
		logger.debug("select new episodes");
		return episodeDao.selectNewEpisodes();
	}

	@Override
	public Episode selectOne(Long id) {
		return episodeDao.selectOne(id);
	}

}
