package com.dc2f.cms.rendering.serlvet;

import java.util.Properties;

public class TemplateEnvironment extends ThreadLocal<Properties> {
	
	public static final String BASE_URL_PROPERTY = "baseURL";
	
	public static final String PAGE_PATH_PROPERTY = "pagePath";

	public static void init(String baseURL, String path) {
		Properties properties = new Properties();
		properties.setProperty(BASE_URL_PROPERTY, baseURL);
		properties.setProperty(PAGE_PATH_PROPERTY, path);
		RenderServlet.environment.set(properties);
	}
	
}