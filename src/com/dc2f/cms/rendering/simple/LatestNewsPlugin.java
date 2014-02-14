package com.dc2f.cms.rendering.simple;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.dao.Template;
import com.dc2f.cms.rendering.serlvet.RenderServlet;
import com.dc2f.cms.rendering.serlvet.TemplateEnvironment;

@Slf4j
@AllArgsConstructor
public class LatestNewsPlugin implements RenderPlugin {

	@Getter
	private final Dc2f dc2f;

	@Override
	public String getDefaultKey() {
		return "latest";
	}

	@Override
	public TemplateChunk generateTemplateChunkFor(Template template,
			String ... renderDefinition) {
		String linkTemplate = renderDefinition[3];
		String projectPath = template.getParentPath();
		String startPath = projectPath + "/" + renderDefinition[1];
		int maxItems = Integer.parseInt(renderDefinition[2]);
		ArrayList<TemplateChunk> chunks = new ArrayList<>();
		for (Page page : dc2f.getAllChildren(startPath, Page.class)) {
			chunks.add(new LatestNewsTemplateChunk(linkTemplate, page));
			if (chunks.size() >= maxItems) {
				break;
			}
		}
		return new ListTemplateChunk(chunks);
	}
	
	@AllArgsConstructor
	private class LatestNewsTemplateChunk implements TemplateChunk {

		private final String template;

		private final Page page;
		
		@Override
		public String toString(JSONObject pageProperties) {
			String title = getSafe(pageProperties, "title");
			String content = getSafe(pageProperties, "content");
			return template.replaceAll("\\$name", page.getName())
					.replaceAll("\\$link", RenderServlet.getEnvironment().getProperty(TemplateEnvironment.BASE_URL_PROPERTY) + "/" + page.getPath())
					.replaceAll("\\$title", title)
					.replaceAll("\\$content", content);
		}

		private String getSafe(JSONObject pageProperties, String string) {
			try {
				return pageProperties.getString(string);
			} catch (JSONException e) {
				log.debug("cannot get title for page " + page.getPath(), e);
				return "";
			}
		}
		
	}

}
