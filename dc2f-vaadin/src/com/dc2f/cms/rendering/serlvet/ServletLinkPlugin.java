package com.dc2f.cms.rendering.serlvet;

import lombok.AllArgsConstructor;

import org.json.JSONObject;

import com.dc2f.cms.dao.Template;
import com.dc2f.cms.rendering.simple.RenderPlugin;
import com.dc2f.cms.rendering.simple.TemplateChunk;

class ServletLinkPlugin implements RenderPlugin {

	public ServletLinkPlugin() {
	}
	
	@Override
	public String getDefaultKey() {
		return "link";
	}

	@Override
	public TemplateChunk generateTemplateChunkFor(Template template, String ... renderDefinition) {
		String linkPath = renderDefinition[1];
		return new LinkPluginTemplateChunk(template.getParentPath() + "/" + linkPath);
	}
	
	@AllArgsConstructor
	private class LinkPluginTemplateChunk implements TemplateChunk {

		private final String path;

		@Override
		public String toString(JSONObject pageProperties) {
			return RenderServlet.getEnvironment().getProperty(TemplateEnvironment.BASE_URL_PROPERTY) + "/" + path;
		}
		
	}
}