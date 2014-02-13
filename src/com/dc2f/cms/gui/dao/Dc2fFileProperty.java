package com.dc2f.cms.gui.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import lombok.AllArgsConstructor;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.vaadin.data.Property;

@AllArgsConstructor
public class Dc2fFileProperty implements Property<String> {

	private final File file;
	
	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setReadOnly(boolean newStatus) { }

	@Override
	public String getValue() {
		try {
			return IOUtils.toString(file.getContent(false), Dc2fConstants.CHARSET);
		} catch (IOException e) {
			throw new Dc2fCmsError("Cannot get the contents of file " + file, e);
		}
	}

	@Override
	public void setValue(String newValue)
			throws com.vaadin.data.Property.ReadOnlyException {
		file.setContent(new ByteArrayInputStream(newValue.getBytes(Dc2fConstants.CHARSET)));
		
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

}
