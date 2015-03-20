package com.visosoft.tvshowinfo.service.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;
import com.visosoft.tvshowinfo.service.EpisodeService;
import com.visosoft.tvshowinfo.service.UsersShowsInfoGeneratorService;
import com.visosoft.tvshowinfo.util.ShowsInfoGeneratedData;

@Service
public class UsersShowsInfoGeneratorServiceImpl implements
		UsersShowsInfoGeneratorService {
	
	private final int MAX_DAYS_INTO_FUTURE = 31;
	
	@Autowired
	private EpisodeService episodeService;
	
	private static final Logger logger = LoggerFactory.getLogger(UsersShowsInfoGeneratorServiceImpl.class);

	@Override
	public ShowsInfoGeneratedData generateData(List<UserSubscription> subs) {
		Map<User, SortedSet<Show>> activeSubs = getActiveSubscriptions(subs);
		ShowsInfoGeneratedData data = new ShowsInfoGeneratedData();
		data.setUserSubs(activeSubs);
		data.setShows(getNeededShows(subs));
		List<Episode> eps = getNewEpisodes(data.getShows());
		divideEpisodes(eps, data);
		return data;
	}
	
	private void divideEpisodes(List<Episode> eps, ShowsInfoGeneratedData data) {
		Calendar c0 = Calendar.getInstance();
		c0.set(Calendar.MILLISECOND, 0);
		c0.set(Calendar.MINUTE, 0);
		c0.set(Calendar.SECOND, 0);
		c0.set(Calendar.HOUR_OF_DAY, 0);
		Date today = c0.getTime();
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_MONTH, 14);
		Date twoWeeks = c1.getTime();
		for (Episode e : eps) {
			if (e.getAirdate().before(today)) {
				data.getYesterday().add(e);
			} else if (e.getAirdate().after(twoWeeks)) {
				data.getMoreThanTwoWeeks().add(e);
			} else if (e.getAirdate().equals(today)) {
				data.getToday().add(e);
			} else {
				data.getInTwoWeeks().add(e);
			}
		}
	}

	private Set<Show> getNeededShows(List<UserSubscription> subs) {
		Set<Show> res = new HashSet<>();
        subs.forEach((sub) -> res.add(sub.getShow()));
		return res;
	}

	private final Comparator<? super Show> alphabeticalComparator = new Comparator<Show>() {
		@Override
		public int compare(Show o1, Show o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	};
	
	private Comparator<? super Show> getShowsAlphabeticalComparator() {
		return alphabeticalComparator;
	}

	private List<Episode> getNewEpisodes(Set<Show> shows) {
		return episodeService.selectEpisodesFromDays(1, MAX_DAYS_INTO_FUTURE, shows);
	}

	private Map<User, SortedSet<Show>> getActiveSubscriptions(List<UserSubscription> subs) {
		Map<User, SortedSet<Show>> activeSubs = new LinkedHashMap<>();
        subs.forEach((us) -> {
            if (!activeSubs.containsKey(us.getUser())) {
                activeSubs.put(us.getUser(), new TreeSet<>(getShowsAlphabeticalComparator()));
            }
            activeSubs.get(us.getUser()).add(us.getShow());
        });
        return activeSubs;
	}

}
