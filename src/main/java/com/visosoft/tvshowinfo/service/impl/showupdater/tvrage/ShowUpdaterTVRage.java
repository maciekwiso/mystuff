package com.visosoft.tvshowinfo.service.impl.showupdater.tvrage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visosoft.tvshowinfo.dao.EpisodeDao;
import com.visosoft.tvshowinfo.dao.ShowDao;
import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;
import com.visosoft.tvshowinfo.service.ShowUpdater;
import com.visosoft.tvshowinfo.service.XMLUnmarshaller;
import com.visosoft.tvshowinfo.service.impl.showupdater.tvrage.ShowXmlTVRage.Episodelist.Season;
import com.visosoft.tvshowinfo.util.ShowSearchResult;

@Component("showUpdaterTVRage")
public class ShowUpdaterTVRage implements ShowUpdater {

	private static final Logger logger = LoggerFactory.getLogger(ShowUpdaterTVRage.class);

	private static final String SEARCH_URL = "http://services.tvrage.com/feeds/search.php?show=";
	
	private static final String TV_RAGE_ID = "services.tvrage.com";
	
	private static final String TV_RAGE_SHOW_SUBSCRIPTION_URL = "http://services.tvrage.com/feeds/episode_list.php?sid=";
	
	@Autowired
	private XMLUnmarshaller xMLUnmarshaller;
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private EpisodeDao episodeDao;
	
	private final Date EMPTY_DATE = new Date(70, 0, 1);
	
	@Override
	public boolean updateShow(Show show) {
		logger.debug("Starting update of show: " + show);
		ShowXmlTVRage showXml = createShowXml(show);
		if (showXml == null) {
			logger.error("Unmarshalling of the show was unsuccessfull");
			return false;
		}
		updateShow(show, showXml);
		return updateEps(show, showXml);
	}
	
	private void updateShow(Show show, ShowXmlTVRage showXml) {
		String xmlTitle = showXml.getName().trim();
		if (!show.getTitle().equals(xmlTitle)) {
			show.setTitle(showXml.getName());
			showDao.update(show);
		}
	}

	private boolean updateEps(Show show, ShowXmlTVRage showXml) {
        if (showXml.getEpisodelist() == null || showXml.getEpisodelist().getSeason() == null) {
            logger.debug("updateShow; " + show.getTitle() + "; There was an empty list of episodes in tv rage object");
            return true;
        }
        List<Episode> eps = episodeDao.selectAllByShow(show);
		List<Episode> changedEps = new LinkedList<Episode>();
		logger.debug("updateShow; " + show.getTitle() + "; " + eps.size());
		for (Season ses : showXml.getEpisodelist().getSeason()) {
			for (ShowXmlTVRage.Episodelist.Season.Episode epXml : ses.getEpisode()) {
				logger.debug("Current xml ep: " + epXml.getTitle() + "; " + ses.getNo() + "x"
						+ epXml.getSeasonnum() + "; " + xmlDateToDate(epXml.getAirdate()));
				Episode ep = findEp(eps, ses, epXml);
				if (ep == null) {
					changedEps.add(addNewEp(epXml, ses, show));
				} else {
					eps.remove(ep);
					if (!areEpsSame(ep, ses, epXml)) {
						updateEp(ep, ses, epXml);
						changedEps.add(ep);
					}
				}
			}
		}
		if (changedEps.size() > 0) {
			logger.debug("Some episodes were changed. DB update needed for number of episodes: " + changedEps.size());
			updateEpsInDb(changedEps);
			show.setLastUpdated(new Date());
			showDao.update(show);
		} else {
			logger.debug("No episodes were changed.");
		}
		removeDeletedEpsInDb(eps);
		return true;
	}
	
	private void removeDeletedEpsInDb(List<Episode> changedEps) {
		if (changedEps.size() <= 0) {
			logger.debug("No episodes were deleted in TVRage.");
			return;
		}
		for (Episode ep : changedEps) {
			logger.debug("Removing episode: " + ep);
			episodeDao.delete(ep);
		}
	}
	
	private void updateEpsInDb(List<Episode> changedEps) {
		for (Episode ep : changedEps) {
			episodeDao.update(ep);
		}
	}

	private void updateEp(
			Episode ep,
			Season ses,
			com.visosoft.tvshowinfo.service.impl.showupdater.tvrage.ShowXmlTVRage.Episodelist.Season.Episode epXml) {
		copyEpData(ep, ses, epXml);
	}

	private Episode addNewEp(
			com.visosoft.tvshowinfo.service.impl.showupdater.tvrage.ShowXmlTVRage.Episodelist.Season.Episode epXml,
			Season ses, Show show) {
		Episode e = new Episode();
		e.setShow(show);
		copyEpData(e, ses, epXml);
		return e;
	}
	
	private void copyEpData(Episode e, Season ses, com.visosoft.tvshowinfo.service.impl.showupdater.tvrage.ShowXmlTVRage.Episodelist.Season.Episode epXml) {
		e.setAirdate(xmlDateToDate(epXml.getAirdate()));
		e.setNumber(epXml.getSeasonnum());
		e.setSeason(ses.getNo());
		e.setTitle(epXml.getTitle().trim());
	}

	private Episode findEp(List<Episode> eps, Season ses, ShowXmlTVRage.Episodelist.Season.Episode epXml) {
		for (Episode e : eps) {
			if (e.getSeason().shortValue() == ses.getNo()
					&& e.getNumber().shortValue() == epXml.getSeasonnum()) {
				return e;
			}
		}
		logger.debug("Didn't find EP: " + epXml.getTitle());
		return null;
	}
	
	private boolean areEpsSame(Episode epDb, Season ses, ShowXmlTVRage.Episodelist.Season.Episode epXml) {
		return dateToString(epDb.getAirdate()).equals(dateToString(epXml.getAirdate()))
				&& epDb.getNumber().shortValue() == epXml.getSeasonnum()
				&& epDb.getSeason().shortValue() == ses.getNo()
				&& epDb.getTitle().equals(epXml.getTitle());
	}
	
	private Date xmlDateToDate(XMLGregorianCalendar d) {
		return d == null ? EMPTY_DATE : d.toGregorianCalendar().getTime();
	}
	
	private String dateToString(XMLGregorianCalendar d) {
		return dateToString(d == null ? EMPTY_DATE : d.toGregorianCalendar().getTime());
	}
	
	private String dateToString(Date d) {
		return d.getYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
	}

	private ShowXmlTVRage createShowXml(Show show) {
		try {
			ShowXmlTVRage r = xMLUnmarshaller.unmarshall(new URL(show.getUrl()), ShowXmlTVRage.class);
			if (r == null) {
				logger.error("Couldn't unmarshall show: " + show);
			} else {
				logger.debug("Unmarshalled for: " + show.getTitle() + "; " + r.getName() + "; " + r.getTotalseasons());
			}
			return r;
		} catch (MalformedURLException e) {
			logger.error("Error on unmarshalling show: " + show, e);
		}
		return null;
	}

	@Override
	public List<ShowSearchResult> searchShow(String showName) {
		SearchResultsTVRage results = getSearchResults(showName);
		if (results == null) {
			logger.info("There was a problem with getting search results");
			return new ArrayList<ShowSearchResult>();
		}
		return mapSearchResults(results);
	}
	
	private List<ShowSearchResult> mapSearchResults(SearchResultsTVRage results) {
		if (results.getShow() == null || results.getShow().isEmpty()) {
			return new ArrayList<ShowSearchResult>();
		}
		ArrayList<ShowSearchResult> mappedResults = new ArrayList<ShowSearchResult>();
		for (SearchResultsTVRage.Show show : results.getShow()) {
			mappedResults.add(mapSearchResult(show));
		}
		return mappedResults;
	}

	private ShowSearchResult mapSearchResult(
			com.visosoft.tvshowinfo.service.impl.showupdater.tvrage.SearchResultsTVRage.Show show) {
		ShowSearchResult result = new ShowSearchResult();
		result.setShowUpdaterId(TV_RAGE_ID);
		result.setName(show.getName());
		result.setShowId(String.valueOf(show.getShowid()));
		return result;
	}

	private SearchResultsTVRage getSearchResults(String showName) {
		try {
			SearchResultsTVRage r = xMLUnmarshaller.unmarshall(new URL(SEARCH_URL + showName), SearchResultsTVRage.class);
			return r;
		} catch (MalformedURLException e) {
			logger.error("Error on unmarshalling search results for showName: {}", showName, e);
		}
		return null;
	}

	@Override
	public String generateSubscriptionLink(ShowSearchResult show) {
		return TV_RAGE_SHOW_SUBSCRIPTION_URL + show.getShowId();
	}

}
