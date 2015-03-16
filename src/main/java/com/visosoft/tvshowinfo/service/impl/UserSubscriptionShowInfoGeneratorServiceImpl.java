package com.visosoft.tvshowinfo.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.visosoft.tvshowinfo.dao.UserSubscriptionDao;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;
import com.visosoft.tvshowinfo.service.UserSubscriptionShowInfoGeneratorService;
import com.visosoft.tvshowinfo.service.UsersShowsInfoGeneratorService;
import com.visosoft.tvshowinfo.util.ShowsInfoGeneratedData;
import com.visosoft.tvshowinfo.util.Utils;

@Service
public class UserSubscriptionShowInfoGeneratorServiceImpl implements
		UserSubscriptionShowInfoGeneratorService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionShowInfoGeneratorServiceImpl.class);
	
	@Autowired
	private UserSubscriptionDao userSubscriptionDao;
	
	@Autowired
	private UsersShowsInfoGeneratorService usersShowsInfoGeneratorService;
	
	@Value("#{settings['mailqueue.content']}")
	private String contentTemplate;
	
	private final String CONTENTVAR_USERNAME = "\\$username";
	private final String CONTENTVAR_SHOWS = "\\$shows";
	private final String CONTENTVAR_YESTERDAY = "\\$yesterday";
	private final String CONTENTVAR_TODAY = "\\$today";
	private final String CONTENTVAR_INTWOWEEKS = "\\$twoweeks";
	private final String CONTENTVAR_LATERTHANTWOWEEKS = "\\$latertwoweeks";

	@Override
	public String generateShowsInfo(User user) {
		List<UserSubscription> subs = userSubscriptionDao.selectAll(user);
		checkActiveSubscriptions(subs);
		ShowsInfoGeneratedData data = usersShowsInfoGeneratorService.generateData(subs);
		if (data.getUserSubs().entrySet().isEmpty()) {
			logger.debug("user subs entry set is empty");
			return "";
		}
		return generateEmailList(data);
	}
	
	private void checkActiveSubscriptions(List<UserSubscription> subs) {
		Iterator<UserSubscription> it = subs.iterator();
		while (it.hasNext()) {
			UserSubscription s = it.next();
			if (!s.getEnabled()) {
				it.remove();
			}
		}
	}

	private String generateEmailList(ShowsInfoGeneratedData data) {
		Map.Entry<User, SortedSet<Show>> rec = data.getUserSubs().entrySet().iterator().next();
		StringBuilder yesterday = Utils.generateEpisodesList(rec.getValue(), data.getYesterday(), false);
		StringBuilder today = Utils.generateEpisodesList(rec.getValue(), data.getToday(), false);
		StringBuilder inTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getInTwoWeeks(), true);
		StringBuilder moreThanTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getMoreThanTwoWeeks(), true);
		StringBuilder shows = Utils.generateSubscribedShowsContent(rec.getValue());
		String content = contentTemplate.replaceAll(CONTENTVAR_SHOWS, shows.toString())
				.replaceAll(CONTENTVAR_USERNAME, rec.getKey().getUsername())
				.replaceAll(CONTENTVAR_TODAY, today.toString())
				.replaceAll(CONTENTVAR_INTWOWEEKS, inTwoWeeks.toString())
				.replaceAll(CONTENTVAR_LATERTHANTWOWEEKS, moreThanTwoWeeks.toString())
				.replaceAll(CONTENTVAR_YESTERDAY, yesterday.toString());
		return content;
	}

}
