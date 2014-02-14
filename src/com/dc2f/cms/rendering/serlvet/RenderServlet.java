package com.dc2f.cms.rendering.serlvet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.rendering.Renderer;
import com.dc2f.cms.rendering.simple.LatestNewsPlugin;
import com.dc2f.cms.rendering.simple.TopNavigationPlugin;

@WebServlet(value = RenderServlet.SERVLET_PATH + "*", asyncSupported = true)
public class RenderServlet extends HttpServlet {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SERVLET_PATH = "/renderer/";

	private Dc2f dc2f;
	
	private Renderer renderer;
	
	static TemplateEnvironment environment = new TemplateEnvironment();
	
	public RenderServlet() {
		dc2f = Dc2fSettings.get().initDc2f();
		renderer = dc2f.getRenderer();
		renderer.registerPlugin(new ServletLinkPlugin());
		renderer.registerPlugin(new TopNavigationPlugin(dc2f));
		renderer.registerPlugin(new LatestNewsPlugin(dc2f));
		
	}
	
	public static Properties getEnvironment() {
		return environment.get();
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
		} else if (node instanceof Folder) {
			Page startpage = null;
			for (Page page : dc2f.getChildren(node.getPath(), Page.class)) {
				if ("index.html".equals(page.getName())) {
					startpage = page;
					break;
				} else if(startpage == null) {
					startpage = page;
				}
			}
			if (startpage != null) {
				resp.sendRedirect(baseURL + "/" + startpage.getPath());
			}
		}
	}
	
}
