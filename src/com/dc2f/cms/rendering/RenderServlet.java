package com.dc2f.cms.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.dao.Template;
import com.dc2f.cms.rendering.simple.RenderPlugin;
import com.dc2f.cms.rendering.simple.TemplateChunk;

@WebServlet(value = RenderServlet.SERVLET_PATH + "*", asyncSupported = true)
public class RenderServlet extends HttpServlet {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SERVLET_PATH = "/renderer/";

	private Dc2f dc2f;
	
	private Renderer renderer;
	
	private static TemplateEnvironment environment = new TemplateEnvironment();
	
	public RenderServlet() {
		dc2f = Dc2fSettings.get().initDc2f();
		renderer = dc2f.getRenderer();
		renderer.registerPlugin(new ServletLinkPlugin());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String baseURL = req.getContextPath() + req.getServletPath();
		String path = req.getRequestURI();
		if (path.startsWith(baseURL)) {
			path = path.substring(baseURL.length());
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
		}
		path = URLDecoder.decode(path, Dc2fConstants.CHARSET.displayName());
		TemplateEnvironment.init(baseURL, path);
		Node node = dc2f.getNodeForPath(path);
		if (node instanceof Page) {
			resp.setHeader("Content-Type", ((Page) node).getMimetype() + "; charset=" + Dc2fConstants.CHARSET.displayName());
			InputStream renderedPageStream = dc2f.getRenderer().render((Page) node);
			IOUtils.copy(renderedPageStream, resp.getOutputStream());
		} else if (node instanceof File) {
			String mimetype = ((File) node).getMimetype();
			if (mimetype.startsWith("text/*")) {
				mimetype += "; charset=" + Dc2fConstants.CHARSET.displayName();
			}
			resp.setHeader("Content-Type", mimetype);
			IOUtils.copy(((File) node).getContent(false), resp.getOutputStream());
		}
	}
	
	private static class TemplateEnvironment extends ThreadLocal<Properties> {
		
		public static final String BASE_URL_PROPERTY = "baseURL";
		
		public static final String PAGE_PATH_PROPERTY = "pagePath";

		public static void init(String baseURL, String path) {
			Properties properties = new Properties();
			properties.setProperty(BASE_URL_PROPERTY, baseURL);
			properties.setProperty(PAGE_PATH_PROPERTY, path);
			environment.set(properties);
		}
		
	}
	
	private class ServletLinkPlugin implements RenderPlugin {

		public ServletLinkPlugin() {
		}
		
		@Override
		public String getDefaultKey() {
			return "link";
		}

		@Override
		public TemplateChunk generateTemplateChunkFor(Template template, String renderDefinition) {
			String linkPath = renderDefinition.replaceAll("^.*?:", "");
			return new LinkPluginTemplateChunk(template.getParentPath() + "/" + linkPath);
		}
	}

	@AllArgsConstructor
	private class LinkPluginTemplateChunk implements TemplateChunk {

		private final String path;

		@Override
		public String toString(JSONObject pageProperties) {
			return environment.get().getProperty(TemplateEnvironment.BASE_URL_PROPERTY) + "/" + path;
		}
		
	}
	
}
