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
        subs.removeIf((sub) -> !sub.getEnabled());
	}

	private String generateEmailList(ShowsInfoGeneratedData data) {
		Map.Entry<User, SortedSet<Show>> rec = data.getUserSubs().entrySet().iterator().next();
		StringBuilder yesterday = Utils.generateEpisodesList(rec.getValue(), data.getYesterday(), false);
		StringBuilder today = Utils.generateEpisodesList(rec.getValue(), data.getToday(), false);
		StringBuilder inTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getInTwoWeeks(), true);
		StringBuilder moreThanTwoWeeks = Utils.generateEpisodesList(rec.getValue(), data.getMoreThanTwoWeeks(), true);
		StringBuilder shows = Utils.generateSubscribedShowsContent(rec.getValue());
        try {
            return contentTemplate.replaceAll(CONTENTVAR_SHOWS, escapeString(shows.toString()))
                    .replaceAll(CONTENTVAR_USERNAME, escapeString(rec.getKey().getUsername()))
                    .replaceAll(CONTENTVAR_TODAY, escapeString(today.toString()))
                    .replaceAll(CONTENTVAR_INTWOWEEKS, escapeString(inTwoWeeks.toString()))
                    .replaceAll(CONTENTVAR_LATERTHANTWOWEEKS, escapeString(moreThanTwoWeeks.toString()))
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
