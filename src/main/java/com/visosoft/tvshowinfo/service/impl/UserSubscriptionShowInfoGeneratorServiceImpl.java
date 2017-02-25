package com.visosoft.tvshowinfo.service.impl;

import java.util.*;

import com.google.common.collect.Lists;
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
	private final String CONTENTVAR_LASTWEEK = "\\$lastweek";

	@Override
	public String generateShowsInfo(User user, boolean withLinks) {
		List<UserSubscription> subs = userSubscriptionDao.selectAll(user);
		checkActiveSubscriptions(subs);
		ShowsInfoGeneratedData data = usersShowsInfoGeneratorService.generateData(subs);
		if (data.getUserSubs().entrySet().isEmpty()) {
			logger.debug("user subs entry set is empty");
			return "";
		}
		return generateEmailList(data, withLinks);
	}
	
	private void checkActiveSubscriptions(List<UserSubscription> subs) {
        subs.removeIf((sub) -> !sub.getEnabled());
	}

	private String generateEmailList(ShowsInfoGeneratedData data, boolean withLinks) {
		Map.Entry<User, SortedSet<Show>> rec = data.getUserSubs().entrySet().iterator().next();
		StringBuilder yesterday = Utils.generateEpisodesList(rec.getValue(), data.getYesterday(), false, withLinks);
		StringBuilder today = Utils.generateEpisodesList(rec.getValue(), data.getToday(), false, withLinks);
		StringBuilder inTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getInTwoWeeks(), true, withLinks);
		StringBuilder lastWeek = Utils.generateEpisodesList(rec.getValue(), Lists.reverse(data.getLastWeek()), true, withLinks);
		StringBuilder moreThanTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getMoreThanTwoWeeks(), true, withLinks);
		StringBuilder shows = Utils.generateSubscribedShowsContent(rec.getValue(), false);
        try {
            return contentTemplate.replaceAll(CONTENTVAR_SHOWS, escapeString(shows.toString()))
                    .replaceAll(CONTENTVAR_USERNAME, escapeString(rec.getKey().getUsername()))
                    .replaceAll(CONTENTVAR_TODAY, escapeString(today.toString()))
                    .replaceAll(CONTENTVAR_INTWOWEEKS, escapeString(inTwoWeeks.toString()))
                    .replaceAll(CONTENTVAR_LATERTHANTWOWEEKS, escapeString(moreThanTwoWeeks.toString()))
					.replaceAll(CONTENTVAR_LASTWEEK, escapeString(lastWeek.toString()))
					.replaceAll(CONTENTVAR_YESTERDAY, escapeString(yesterday.toString()));
        } catch (Exception e) {
            logger.error(String.format("An exception on creating the info for user, shows: [%s], username: [%s], today: [%s], " +
                    "in two weeks [%s], later than 2 weeks [%s], yesterday [%s]", shows, rec.getKey().getUsername(),
                    today, inTwoWeeks, moreThanTwoWeeks, yesterday), e);
        }
        return "Sorry, an error occurred";
	}

    private String escapeString(String s) {
        return s.replaceAll("\\$", "\\\\\\$");
    }

}
