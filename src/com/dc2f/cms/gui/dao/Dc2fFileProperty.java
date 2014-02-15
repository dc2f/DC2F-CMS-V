package com.dc2f.cms.gui.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import lombok.AllArgsConstructor;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;

@AllArgsConstructor
public class Dc2fFileProperty implements Property<String>, TextChangeListener {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private final File file;
	
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
		Dc2fSettings.get().initDc2f().addFile(file, file.getNodeType());
		
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

	@Override
	public void textChange(TextChangeEvent event) {
		setValue(event.getText());
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setReadOnly(boolean newStatus) { }

}
