package com.visosoft.tvshowinfo.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import com.visosoft.tvshowinfo.service.XMLUnmarshaller;
@Service
public class XMLUnmarshallerImpl implements XMLUnmarshaller {

	@Autowired
	private Unmarshaller unmarshaller;

	public XMLUnmarshallerImpl() {
		//
	}

	public XMLUnmarshallerImpl(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(XMLUnmarshallerImpl.class);
	
	@Override
	public <T> T unmarshall(URL url, Class<?> XMLMapperclass) {
        try {
			return (T)unmarshaller.unmarshal(new StreamSource(url.toExternalForm()));
		} catch (XmlMappingException | IOException e) {
			logger.error("Problem with unmarshalling url: " + url.toExternalForm(), e);
		}
        return null;
	}

	@Override
	public <T> T unmarshall(String xml, Class<?> XMLMapperclass) {
		try {
			return (T)unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
		} catch (XmlMappingException | IOException e) {
			logger.error("Problem with unmarshalling xml string: " + xml, e);
		}
		return null;
	}

}
