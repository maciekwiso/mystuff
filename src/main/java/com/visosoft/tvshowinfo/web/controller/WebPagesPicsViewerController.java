package com.visosoft.tvshowinfo.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.google.common.io.ByteStreams;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.PicViewerRecordService;

@Controller
public class WebPagesPicsViewerController {

	@Autowired
	private PicViewerRecordService picViewerRecordService;
	
	@RequestMapping(value = "/picviewer/showunseen", method = RequestMethod.GET)
	public String displayUnseen(@RequestParam("group") String groupName, ModelMap model) {
		List<PicViewerRecord> pics = picViewerRecordService.selectUnseen(50, groupName);
		setAsSeen(pics, groupName);
		model.put("pics", pics);
		return "picviewer";
	}
	
	@RequestMapping(value = "/picviewer/show", method = RequestMethod.GET)
	public String displayAll(@RequestParam("group") String groupName, ModelMap model) {
		List<PicViewerRecord> pics = picViewerRecordService.selectAll(groupName);
		model.put("pics", pics);
		return "picviewer";
	}

	@RequestMapping(value = "/picviewer/setasseen", method = RequestMethod.GET)
	public String setAsSeen(@RequestParam("group") String groupName) {
		setAsSeenInDb(groupName);
		return "redirect:/picviewer/groups";
	}

	@RequestMapping(value = "/picviewer/setasseen", method = RequestMethod.POST)
	public String setAsSeenFromCheckboxes(@RequestParam("seen") String[] groupNames) {
		Arrays.stream(groupNames).forEach(this::setAsSeenInDb);
		return "redirect:/picviewer/groups";
	}

	private void setAsSeenInDb(String groupName) {
		picViewerRecordService.setAsSeenWithIdLorE(Long.MAX_VALUE, groupName);
	}

	@RequestMapping(value = "/picviewer/groups", method = RequestMethod.GET)
	public String displayAllGroups(ModelMap model) {
		List<String> groups = picViewerRecordService.selectUnseenGroups();
		model.put("groups", groups);
		return "groupspicviewer";
	}
	
	@RequestMapping(value = "/picviewer/pic/{id}", method = RequestMethod.GET)
	@ResponseBody
    public void displayPicviewerPic(@PathVariable long id, HttpServletResponse response) {

		PicViewerRecord pvr = picViewerRecordService.selectOne(id);
		byte[] pic = getPic(pvr);
        if (pic != null) {
            response.setContentType(getContentType(pvr));
            try {
                response.getOutputStream().write(pic);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
	
	private void setAsSeen(List<PicViewerRecord> pics, String groupName) {
		if (pics != null && !pics.isEmpty()) {
			picViewerRecordService.setAsSeenWithIdLorE(pics.get(pics.size() - 1).getId(), groupName);
		}
	}

	private String getContentType(PicViewerRecord pvr) {
		if (pvr.getUrl().endsWith("jpg")) {
			return "image/jpg";
		} else if (pvr.getUrl().endsWith("mp4")) {
			return "video/mp4";
		} else if (pvr.getUrl().endsWith("png")) {
			return "image/png";
		} else {
			return "image/gif";
		}
	}

	private byte[] getPic(PicViewerRecord pvr) {
		try {
			return Request.Get(pvr.getUrl()).connectTimeout(5000).socketTimeout(10000).execute().returnContent().asBytes();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
