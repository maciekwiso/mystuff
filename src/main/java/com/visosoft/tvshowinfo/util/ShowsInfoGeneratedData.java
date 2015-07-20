package com.visosoft.tvshowinfo.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;

public class ShowsInfoGeneratedData {
	private Map<User, SortedSet<Show>> userSubs;
	private Set<Show> shows;
	private List<Episode> yesterday = new LinkedList<>();
	private List<Episode> today = new LinkedList<>();
	private List<Episode> inTwoWeeks = new LinkedList<>();
	private List<Episode> moreThanTwoWeeks = new LinkedList<>();
	private List<Episode> lastWeek = new LinkedList<>();
	public Map<User, SortedSet<Show>> getUserSubs() {
		return userSubs;
	}
	public void setUserSubs(Map<User, SortedSet<Show>> userSubs) {
		this.userSubs = userSubs;
	}
	public Set<Show> getShows() {
		return shows;
	}
	public void setShows(Set<Show> shows) {
		this.shows = shows;
	}
	public List<Episode> getYesterday() {
		return yesterday;
	}
	public void setYesterday(List<Episode> yesterday) {
		this.yesterday = yesterday;
	}
	public List<Episode> getToday() {
		return today;
	}
	public void setToday(List<Episode> today) {
		this.today = today;
	}
	public List<Episode> getInTwoWeeks() {
		return inTwoWeeks;
	}
	public void setInTwoWeeks(List<Episode> inTwoWeeks) {
		this.inTwoWeeks = inTwoWeeks;
	}
	public List<Episode> getMoreThanTwoWeeks() {
		return moreThanTwoWeeks;
	}
	public void setMoreThanTwoWeeks(List<Episode> moreThanTwoWeeks) {
		this.moreThanTwoWeeks = moreThanTwoWeeks;
	}

	public List<Episode> getLastWeek() {
		return lastWeek;
	}

	public void setLastWeek(List<Episode> lastWeek) {
		this.lastWeek = lastWeek;
	}
}
