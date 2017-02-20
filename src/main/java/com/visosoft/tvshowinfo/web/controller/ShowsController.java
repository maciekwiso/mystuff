package com.visosoft.tvshowinfo.web.controller;

import java.util.Date;
import java.util.List;

import com.visosoft.tvshowinfo.service.ShowsDataUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.EpisodeService;
import com.visosoft.tvshowinfo.service.ShowService;

@Controller
public class ShowsController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShowsController.class);
	
	@Autowired
	private ShowService showService;

	@Autowired private ShowsDataUpdaterService showsDataUpdaterService;
	
	@Autowired
	private EpisodeService episodeService;
	
	@RequestMapping("shows")
	public String showsMain(ModelMap model) {
		List<Show> shows = showService.selectAll();
		model.put("shows", shows);
		LOG.info("showing all shows: {}", shows.size());
		return "shows";
	}
	
	@RequestMapping("delshow/{showId}")
	public String deleteShow(@PathVariable long showId) {
		Show s = showService.selectOne(showId);
		if (s == null) {
			LOG.info("invalid show id to delete: {}", showId);
		} else {
			LOG.info("deleting show: {}", s);
			showService.delete(s);
		}
		return "redirect:/shows";
	}
	
	@RequestMapping(value = "refreshShows", method = RequestMethod.GET)
	public String refreshShows() {
		showsDataUpdaterService.updateShowsData();
		return "redirect:/shows";
	}

	@RequestMapping(value = "editshow/{showId}", method = RequestMethod.GET)
	public String editShow(@PathVariable long showId, ModelMap model) {
		Show s = showService.selectOne(showId);
		if (s == null) {
			LOG.info("invalid show id to edit: {}", showId);
		} else {
			LOG.info("Editing show: {}", s);
			model.addAttribute("show", s);
			model.put("eps", episodeService.selectAllByShow(s));
		}
		return "editshow";
	}
	
	@RequestMapping(value = "editshow/{showId}", method = RequestMethod.POST)
	public String saveEditShow(@ModelAttribute("show") Show show, @PathVariable long showId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			LOG.info("There were errors on editing show");
			return "editshow/" + show.getId();
		}
		updateShow(show, showId);
		return "redirect:/shows";
	}

	private void updateShow(Show editedShow, long showId) {
		Show show = showService.selectOne(showId);
		show.setTitle(editedShow.getTitle());
		show.setUrl(editedShow.getUrl());
		showService.update(show);
		LOG.info("Updated show: {}", show);
	}

	@RequestMapping(value = "addshow", method = RequestMethod.GET)
	public String addShow(ModelMap model) {
		model.addAttribute("show", new Show());
		return "addshow";
	}

	@RequestMapping(value = "addshow", method = RequestMethod.POST)
	public String saveAddShow(@ModelAttribute("show") Show show, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			LOG.info("There were errors on adding show");
			return "addshow";
		}
		show.setLastUpdated(new Date());
		showService.insert(show);
		showsDataUpdaterService.updateShow(show);
		return "redirect:/shows";
	}
}
