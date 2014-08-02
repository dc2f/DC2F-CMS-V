package com.dc2f.cms.rendering.simple;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;

import org.json.JSONObject;

import com.dc2f.cms.dao.Template;

public class DatePlugin implements RenderPlugin {

	@Override
	public String getDefaultKey() {
		return "date";
	}

	@Override
	public TemplateChunk generateTemplateChunkFor(Template template,
			String ... renderDefinition) {
		String dateFormatString = renderDefinition[1];
		return new DateTemplateChunk(dateFormatString);
	}

	@AllArgsConstructor
	private class DateTemplateChunk implements TemplateChunk {

		/**
		 * Date format for generating the output string.
		 */
		private final String dateFormatString;
		
		@Override
		public String toString(JSONObject pageProperties) {
			return new SimpleDateFormat(dateFormatString).format(new Date());
		}
		
	}

}
