package com.visosoft.tvshowinfo.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.visosoft.tvshowinfo.dao.PicViewerDao;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.PicViewerRecordService;

@Service
@Transactional
public class PicViewerRecordServiceImpl implements PicViewerRecordService {

    private static final Logger logger = LoggerFactory.getLogger(PicViewerRecordServiceImpl.class);

	@Autowired
	private PicViewerDao picViewerDao;

	@Override
	public void insert(PicViewerRecord s) {
		picViewerDao.insert(s);
	}

	@Override
	public void refresh() {
        logger.info("Starting Pic Viewer refresh");
		doRefresh();
        logger.info("Pic Viewer refresh done, deleting old pics");
		picViewerDao.deleteOld();
	}

	@Override
	public List<PicViewerRecord> selectAll() {
		return picViewerDao.selectAll();
	}

	@Override
	public List<PicViewerRecord> selectUnseen() {
		return picViewerDao.selectUnseen();
	}

	@Override
	public PicViewerRecord selectOne(Long id) {
		return picViewerDao.selectOne(id);
	}

	@Override
	public void setAsSeenWithIdLorE(Long id) {
		picViewerDao.setAsSeenWithIdLorE(id);
	}
	
	private void doRefresh() {
		String page = "http://9gag.com";
		try {
			String contents = Resources.toString(new URL(page), Charsets.UTF_8);
			addPics(contents);
		} catch (IOException e) {
			logger.error("Exception in doRefresh", e);
		}
	}

	private void addPics(String contents) {
		Pattern artPattern = Pattern.compile("<article.*?</article>", Pattern.DOTALL);
		Pattern titlePattern = Pattern.compile("<a .*?>(.*?)</a>", Pattern.DOTALL);
		Pattern srcPattern = Pattern.compile("src=\"(.*?)\"");
		Pattern gifPattern = Pattern.compile("\"([^\"]*\\.gif)\"");
		Matcher m = artPattern.matcher(contents);
        int added = 0;
		while (m.find()) {
			String currentContent = m.group();
			Matcher titleMatcher = titlePattern.matcher(currentContent);
			titleMatcher.find();
			String title = titleMatcher.group(1).trim();
			Matcher gifMatcher = gifPattern.matcher(currentContent);
			Optional<String> url = Optional.empty();
			if (gifMatcher.find()) {
				url = Optional.of(gifMatcher.group(1));
			} else {
				Matcher m2 = srcPattern.matcher(currentContent);
				m2.find();
				String foundUrl = m2.group(1);
				if (!foundUrl.endsWith("_v1.jpg") && !foundUrl.contains("long-post-cover")) {
					url = Optional.of(foundUrl);
				}
			}
			if (url.isPresent() && addNewPic(url.get(), title)) added++;
		}
        logger.info("Added new pics {}", added);
	}

	private boolean addNewPic(String url, String title) {
        int lastSlash = url.lastIndexOf("/");
        if (lastSlash == -1) {
            return false;
        }
        if (picViewerDao.withUrlEndingExists(url.substring(lastSlash))) {
			return false;
		}
		PicViewerRecord p = new PicViewerRecord();
		p.setUrl(url);
		p.setTitle(title);
		picViewerDao.insert(p);
        return true;
	}

	@Override
	public List<PicViewerRecord> selectAll(int maxResults) {
		return picViewerDao.selectAll(maxResults);
	}

	@Override
	public List<PicViewerRecord> selectUnseen(int maxResults) {
		return picViewerDao.selectUnseen(maxResults);
	}
}
