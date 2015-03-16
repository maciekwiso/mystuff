package com.visosoft.tvshowinfo.service;

import java.util.Collection;
import java.util.List;

import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;

public interface EpisodeService {
	public void insert(Episode s);
	
    public Episode update(Episode s);
    
    public void delete(Episode s);
    
    public void deleteByShow(Show show);
    
    public List<Episode> selectAllByShow(Show show);
    
    public List<Episode> selectAllByShow(Show show, boolean onlyNew);
    
    public List<Episode> selectEpisodesFromDays(int daysIntoPast, int daysIntoFuture, Collection<Show> shows);
    
    public List<Episode> selectNewEpisodes();
    
    public Episode selectOne(Long id);
}
