package com.visosoft.tvshowinfo.web.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.visosoft.tvshowinfo.dao.UserSubscriptionDao;
import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.domain.User;
import com.visosoft.tvshowinfo.domain.UserSubscription;
import com.visosoft.tvshowinfo.service.EpisodeService;
import com.visosoft.tvshowinfo.service.ShowService;
import com.visosoft.tvshowinfo.service.UserService;
import com.visosoft.tvshowinfo.service.UserSubscriptionService;
import com.visosoft.tvshowinfo.util.ShowSearchResult;
import com.visosoft.tvshowinfo.util.Utils;


@Controller
public class UsersController {
	
	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserSubscriptionService userSubscriptionService;
	
	@Autowired
	private ShowService showService;
	
	@Autowired
	private EpisodeService episodeService;
	
	@Autowired
	private UserSubscriptionDao userSubscriptionDao;
	
	@RequestMapping("users")
	public String usersMain(ModelMap model) {
		List<User> users = userService.selectAll();
		model.put("users", users);
		LOG.info("usering all users: {}", users.size());
		return "users";
	}
	
	@RequestMapping(value = "edituser/{userId}", method = RequestMethod.GET)
	public String edituser(@PathVariable long userId, ModelMap model) {
		User s = userService.selectOne(userId);
		if (s == null) {
			LOG.info("invalid user id to edit: {}", userId);
		} else {
			LOG.info("Editing user: {}", s);
			model.addAttribute("user", s);
		}
		return "edituser";
	}
	
	@RequestMapping(value = "edituser/{userId}", method = RequestMethod.POST)
	public String saveEdituser(@ModelAttribute("user") User user, @PathVariable long userId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			LOG.info("There were errors on editing user");
			return "edituser/" + user.getId();
		}
		updateuser(user, userId);
		return "redirect:/users";
	}

	private void updateuser(User editeduser, long userId) {
		User user = userService.selectOne(userId);
		user.setEmail(editeduser.getEmail());
		user.setEnabled(editeduser.getEnabled());
		if (editeduser.getPassword() != null && !editeduser.getPassword().isEmpty()) {
			user.setPassword(Utils.md5(editeduser.getPassword()));
		}
		userService.update(user);
		LOG.info("Updated user: {}", user);
	}
	
	@RequestMapping(value = "adduser", method = RequestMethod.GET)
	public String adduser(ModelMap model) {
		User s = new User();
		model.addAttribute("user", s);
		return "adduser";
	}
	
	@RequestMapping(value = "adduser", method = RequestMethod.POST)
	public String saveAdduser(@ModelAttribute("user") User user, BindingResult bindingResult) {
		validateAddUser(user, bindingResult);
		if (bindingResult.hasErrors()) {
			LOG.info("There were errors on adding user");
			return "adduser";
		}
		adduser(user);
		return "redirect:/edituser/" + user.getId();
	}

	private void validateAddUser(User user, BindingResult bindingResult) {
		if (user.getUsername() == null) {
			bindingResult.reject("username", "Empty username");
			return;
		}
		user.setUsername(user.getUsername().trim());
		if (user.getUsername().isEmpty()) {
			bindingResult.reject("username", "Empty username");
			return;
		}
		if (StringUtils.isEmpty(user.getEmail())) {
			bindingResult.reject("email", "Empty email");
			return;
		}
		user.setEmail(user.getEmail().trim());
		if (StringUtils.isEmpty(user.getPassword())) {
			bindingResult.reject("password", "Empty password");
			return;
		}
		User u = userService.selectOne(user.getUsername());
		if (u != null) {
			bindingResult.reject("username", "Username already exists!");
			return;
		}
	}

	private void adduser(User user) {
		user.setPassword(Utils.md5(user.getPassword()));
		user.setCreated(new Date());
		user.setEnabled(true);
		userService.insert(user);
	}
	
	@RequestMapping("usersubs/{userId}/{days}")
	public String showSubsWithDays(@PathVariable long userId, @PathVariable int days, ModelMap model) {
		User s = userService.selectOne(userId);
		if (s == null) {
			LOG.info("invalid user id show subscriptions: {}", userId);
		} else {
			LOG.info("Showing subscriptions of user: {}", s);
			model.put("user", s);
			model.put("subs", userSubscriptionService.selectAll(s));
			model.put("usersubinfo", getUserSubInfo(s, days));
			model.put("days", days);
		}
		return "usersubs";
	}
	
	private String getUserSubInfo(User s, int days) {
		List<UserSubscription> subs = userSubscriptionDao.selectAll(s);
		checkActiveSubscriptions(subs);
		List<Show> shows = new LinkedList<>();
        subs.forEach((us) -> shows.add(us.getShow()));
		int past = 0, future = 0;
		if (days > 0) {
			future = days;
		} else {
			past = days;
		}
		List<Episode> episodes = episodeService.selectEpisodesFromDays(past, future, shows);
		StringBuilder result = new StringBuilder();
		Date now = new Date();
		now.setHours(0);
		now.setMinutes(0);
		now.setSeconds(0);
		for (Episode e : episodes) {
			result.append(e.getAirdateFormatted()).append(" (").append(Utils.daysBetween(now, e.getAirdate())).append(" days) - ")
				.append(e.toReadableString()).append("<br />");
		}
		return result.toString();
	}

	private void checkActiveSubscriptions(List<UserSubscription> subs) {
        subs.removeIf((s) -> !s.getEnabled());
	}
	
	@RequestMapping("usersubs/{userId}")
	public String showSubs(@PathVariable long userId, ModelMap model) {
		return showSubsWithDays(userId, 14, model);
	}
	
	@RequestMapping("delusersub/{userId}/{subsId}")
	public String showSubs(@PathVariable long userId, @PathVariable long subsId) {
		UserSubscription sub = userSubscriptionService.select(subsId);
		if (sub != null) {
			LOG.info("deleting subscription: {}", sub);
			userSubscriptionService.delete(sub);
		} else {
			LOG.info("Didn't find subscription with id: {}", subsId);
		}
		return "redirect:/usersubs/" + userId;
	}
	
	@RequestMapping(value = "addusersub/{userId}", method = RequestMethod.GET)
	public String addSub(@PathVariable long userId, ModelMap model) {
		User s = userService.selectOne(userId);
		if (s == null) {
			LOG.info("invalid user id show subscriptions: {}", userId);
		} else {
			LOG.info("Showing subscriptions of user: {}", s);
			model.put("user", s);
			List<UserSubscription> subs = userSubscriptionService.selectAll(s);
			model.put("subs", subs);
			if (!model.containsKey("showName")) {
				model.put("showName", "");
			}
			Set<Long> existingShows = subs.stream().map(a -> a.getShow().getId()).collect(Collectors.toSet());
			model.put("shows", showService.selectAll().stream().filter(a -> !existingShows.contains(a.getId())).collect(Collectors.toList()));
		}
		return "addsub";
	}
	
	@RequestMapping(value = "addusersub", method = RequestMethod.POST)
	public String addSubSearch(@RequestParam long userId, @RequestParam String showName, ModelMap model) {
		List<ShowSearchResult> searchResults = userSubscriptionService.searchShow(showName);
		model.put("searchResults", searchResults);
		model.put("showName", showName);
		return addSub(userId, model);
	}

	@RequestMapping(value = "addnewsub/{userId}/{showId}", method = RequestMethod.GET)
	public String addNewSub(@PathVariable long userId, @PathVariable String showId) {
		UserSubscription newSub = new UserSubscription();
		newSub.setEnabled(true);
		newSub.setEmailEnabled(false);
		newSub.setUser(userService.selectOne(userId));
		newSub.setShow(showService.selectOne(showId));
		userSubscriptionService.insert(newSub);
		return "redirect:/usersubs/" + userId;
	}
	
	@RequestMapping(value = "addselectedusersub/{userId}/{showName}/{showUpdaterId}/{showId}/{emailEnabled}", method = RequestMethod.GET)
	public String addSelectedSub(@PathVariable long userId, @PathVariable String showName, @PathVariable String showUpdaterId,
			@PathVariable String showId, @PathVariable boolean emailEnabled, ModelMap model) {
		User s = userService.selectOne(userId);
		if (s == null) {
			LOG.info("invalid user id show subscriptions: {}", userId);
		} else {
			Show show = showService.selectOne(showName);
			if (show != null) {
				LOG.info("The show already is in local DB: {}", show);
				addSelectedSub(s, show, emailEnabled);
			} else {
				ShowSearchResult ssr = new ShowSearchResult();
				ssr.setName(showName);
				ssr.setShowId(showId);
				ssr.setShowUpdaterId(showUpdaterId);
				String showUrl = userSubscriptionService.generateSubscriptionUrl(ssr);
				show = new Show();
				show.setTitle(showName);
				show.setUrl(showUrl);
				show.setLastUpdated(new Date());
				showService.insert(show);
				addSelectedSub(s, show, emailEnabled);
			}
		}
		return "redirect:/usersubs/" + userId;
	}

	private void addSelectedSub(User s, Show show, boolean emailEnabled) {
		UserSubscription us = new UserSubscription();
		us.setEmailEnabled(emailEnabled);
		us.setEnabled(true);
		us.setShow(show);
		us.setUser(s);
		userSubscriptionService.insert(us);
	}
}
