package com.visosoft.tvshowinfo.web.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam(value="error", required=false) boolean error, Locale locale, ModelMap model) {
		LOG.info("Welcome home! the client locale is "+ locale.toString());
		if (error == true) {
			model.put("error", "You have entered an invalid username or password!");
		} else {
			model.put("error", "");
		}
		return "login";
	}
	
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
 	public String getDeniedPage() {
		LOG.debug("Received request to show denied page");
		return "denied";
	}
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
 	public String getmainPage() {
		LOG.debug("Received request to show the main page");
		return "main";
	}
}
