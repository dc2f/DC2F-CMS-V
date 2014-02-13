package com.dc2f.cms.gui.dao;

import lombok.AllArgsConstructor;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.File;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

@AllArgsConstructor
public class Dc2fResource implements ConnectorResource {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	private final File file;

	@Override
	public String getMIMEType() {
		return file.getMimetype();
	}

	@Override
	public DownloadStream getStream() {
		DownloadStream ds = new DownloadStream(file.getContent(false), getMIMEType(), getFilename());
		ds.setCacheTime(30 * 1000);
		ds.setBufferSize(0);
		return ds;
	}

	@Override
	public String getFilename() {
		return file.getName();
	}

}
