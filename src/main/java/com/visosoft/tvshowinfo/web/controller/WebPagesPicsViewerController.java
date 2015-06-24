package com.visosoft.tvshowinfo.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.io.ByteStreams;
import com.visosoft.tvshowinfo.domain.PicViewerRecord;
import com.visosoft.tvshowinfo.service.PicViewerRecordService;

@Controller
public class WebPagesPicsViewerController {

	@Autowired
	private PicViewerRecordService picViewerRecordService;
	
	@RequestMapping(value = "/picviewer/showunseen", method = RequestMethod.GET)
	public String displayUnseen(ModelMap model) {
		List<PicViewerRecord> pics = picViewerRecordService.selectUnseen(50);
		setAsSeen(pics);
		model.put("pics", pics);
		return "picviewer";
	}
	
	@RequestMapping(value = "/picviewer/show", method = RequestMethod.GET)
	public String displayAll(ModelMap model) {
		List<PicViewerRecord> pics = picViewerRecordService.selectAll();
		model.put("pics", pics);
		return "picviewer";
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
	
	private void setAsSeen(List<PicViewerRecord> pics) {
		if (pics != null && !pics.isEmpty()) {
			picViewerRecordService.setAsSeenWithIdLorE(pics.get(pics.size() - 1).getId());
		}
	}

	private String getContentType(PicViewerRecord pvr) {
		return pvr.getUrl().endsWith("jpg") ? "image/jpg" : pvr.getUrl().endsWith("mp4") ? "video/mp4" : "image/gif";
	}

	private byte[] getPic(PicViewerRecord pvr) {
		try {
			URL url = new URL(pvr.getUrl());
			InputStream stream = url.openStream();
			return ByteStreams.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
