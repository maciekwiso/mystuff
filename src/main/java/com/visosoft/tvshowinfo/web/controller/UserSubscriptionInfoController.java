package com.visosoft.tvshowinfo.web.controller;

import java.security.Principal;

import com.visosoft.tvshowinfo.service.ShowsDataUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.service.UserService;
import com.visosoft.tvshowinfo.service.UserSubscriptionShowInfoGeneratorService;


@RestController
public class UserSubscriptionInfoController {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserSubscriptionInfoController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private ShowsDataUpdaterService showService;
	
	@Autowired
	private UserSubscriptionShowInfoGeneratorService userSubscriptionShowInfoGeneratorService;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/services/tryLogin")
	public String isLoggedIn() {
		return "true";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/services/showsInfo")
	public String userInfo(Principal principal) {
		String username = principal == null ? null : principal.getName();
		if (username == null) {
			return "Bad username or password";
		}
		User user = userService.selectOne(username);
		LOG.info("generating info for user: {}", user.getUsername());
		return userSubscriptionShowInfoGeneratorService.generateShowsInfo(user, false);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/showsInfo/{username}")
	public String userInfo(@PathVariable String username) {
		User user = userService.selectOne(username);
		if (user == null) {
			return "Bad credentials!";
		}
		LOG.info("generating info (no security) for user: {}", user.getUsername());
		return userSubscriptionShowInfoGeneratorService.generateShowsInfo(user, false);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/showsInfoHtml/{username}")
	public String userInfoHtml(@PathVariable String username) {
		User user = userService.selectOne(username);
		if (user == null) {
			return "Bad credentials!";
		}
		LOG.info("generating html info (no security) for user: {}", user.getUsername());
		return userSubscriptionShowInfoGeneratorService.generateShowsInfo(user, true).replaceAll("\n", "<br \\>");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/updateShows")
	public String updateShows() {
		LOG.info("Updating shows");
		showService.updateShowsData();
		return "DONE";
	}
}
