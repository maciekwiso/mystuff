package com.visosoft.tvshowinfo.service;

import java.util.List;

import com.visosoft.tvshowinfo.domain.Show;

public interface ShowService {
	void insert(Show s);
    
    Show update(Show s);
    
    void delete(Show s);
    List<Show> selectAll();
    
    Show selectOne(Long id);

	Show selectOne(String showName);
}
