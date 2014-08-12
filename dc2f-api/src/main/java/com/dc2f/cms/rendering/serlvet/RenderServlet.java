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
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.rendering.Renderer;
import com.dc2f.cms.rendering.simple.LatestNewsPlugin;
import com.dc2f.cms.rendering.simple.TopNavigationPlugin;
import com.dc2f.cms.settings.Dc2fSettings;

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
			renderPage(resp, (Page) node);
		} else if (node instanceof File) {
			renderFile(resp, (File) node);
		} else if (node instanceof Folder) {
			renderFolder(resp, baseURL, (Folder) node);
		}
	}

	/**
	 * Try rendering folder by fetching startpage and sending redirect.
	 * @param resp - servlet response to use to send redirect
	 * @param baseURL - base url of the request
	 * @param folder - folder to render
	 * @throws IOException - if redirect cannot be sent
	 */
	private void renderFolder(HttpServletResponse resp, String baseURL,
			Folder folder) throws IOException {
		Page startpage = null;
		for (Page page : dc2f.getChildren(folder.getPath(), Page.class)) {
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

	/**
	 * Render the file into the response. This is currently just copying the file to the output stream 1:1. Except for
	 * for text files where the correct encoding is set.
	 * @param resp - response to render the file
	 * @param file - file to render
	 * @throws IOException - in case we get an error pushing the file contents to the response
	 */
	private void renderFile(HttpServletResponse resp, File file)
			throws IOException {
		String mimetype = file.getMimetype();
		if (mimetype.startsWith("text/*")) {
			mimetype += "; charset=" + Dc2fConstants.CHARSET.displayName();
		}
		resp.setHeader("Content-Type", mimetype);
		IOUtils.copy(file.getContent(false), resp.getOutputStream());
	}
	
	/**
	 * Render the page with the help of the renderer into the output stream.
	 * @param resp - response to render the page to
	 * @param page - page to render
	 * @throws IOException - in case we get an error writing the output stream of the response
	 */
	private void renderPage(HttpServletResponse resp, Page page)
			throws IOException {
		resp.setHeader("Content-Type", ((Page) page).getMimetype() + "; charset=" + Dc2fConstants.CHARSET.displayName());
		InputStream renderedPageStream = dc2f.getRenderer().render((Page) page);
		IOUtils.copy(renderedPageStream, resp.getOutputStream());
	}
	
}
