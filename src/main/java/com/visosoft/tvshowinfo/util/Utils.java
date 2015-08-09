package com.visosoft.tvshowinfo.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import com.visosoft.tvshowinfo.domain.Episode;
import com.visosoft.tvshowinfo.domain.Show;

public final class Utils {
	private Utils() {}
	
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String EMAIL_CONTENT_NOTHING = "nothing" + NEW_LINE;
	
	public static StringBuilder generateEpisodesList(SortedSet<Show> shows,
			List<Episode> eps, boolean showDays) {
		Date today = null;
		if (showDays) {
			today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
		}
		StringBuilder b = new StringBuilder();
		for (Episode e : eps) {
			if (!shows.contains(e.getShow())) {
				continue;
			}
			if (showDays) {
				b.append("In ").append(daysBetween(today, e.getAirdate())).append(" days (")
					.append(formatAirDate(e.getAirdate())).append("): ");
			}
			b.append(e.toReadableString()).append("  ").append(torrentzLink(e)).append(NEW_LINE);
		}
		if (b.length() <= 0) {
			b.append(EMAIL_CONTENT_NOTHING);
		}
		return b;
	}

	private static String torrentzLink(Episode e) {
		return String.format("<a href=\"https://torrentz.eu/search?q=%s+s%se%02d\">torrent</a>", e.getShow().getTitle().replaceAll(" ", "+"), e.getSeason(), e.getNumber());
	}

	private static DateFormat dateFormat = DateFormat.getDateInstance();
	
	public static String formatAirDate(Date airdate) {
		return dateFormat.format(airdate);
	}

	public static StringBuilder generateSubscribedShowsContent(
			SortedSet<Show> shows) {
		StringBuilder b = new StringBuilder();
        shows.forEach((s) -> b.append(s.getTitle()).append(NEW_LINE));
		return b;
	}
	
	private static final long ONE_HOUR = 60 * 60 * 1000L;

	public static long daysBetween(Date d1, Date d2) {
		return ((d2.getTime() - d1.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
	}
	
	public static String md5(String str) {
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(str.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
}
