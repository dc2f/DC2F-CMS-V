package com.dc2f.cms.gui.dao;

import java.io.InputStream;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Page;
import com.vaadin.server.DownloadStream;

public class Dc2fRenderResource extends Dc2fResource {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private final Page page;
	
	public Dc2fRenderResource(Page renderPage) {
		super(renderPage);
		page = renderPage;
	}
	
	@Override
	public DownloadStream getStream() {
		InputStream renderedPage = Dc2fSettings.get().initDc2f().getRenderer().render(page);
		DownloadStream ds = new DownloadStream(renderedPage, getMIMEType(), getFilename());
		ds.setCacheTime(5000);
		ds.setBufferSize(0);
		return ds;
	}

}
