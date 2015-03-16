package com.visosoft.tvshowinfo.dao;

import java.util.List;

import com.visosoft.tvshowinfo.domain.Show;

public interface ShowDao {
	void insert(Show s);
    
    public Show update(Show s);
    
    public void delete(Show s);
    public List<Show> selectAll();
    
    public Show selectOne(Long id);

	Show selectOne(String showName);
}
