package com.visosoft.tvshowinfo.service;

import java.net.URL;

public interface XMLUnmarshaller {
	
	<T> T unmarshall(URL url, Class<?> XMLMapperclass);
}
