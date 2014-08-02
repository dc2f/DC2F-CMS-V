package com.dc2f.cms.rendering.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.cms.Dc2fConstants;
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
		for (Page page : sort(dc2f.getAllChildren(startPath, Page.class))) {
			chunks.add(new LatestNewsTemplateChunk(linkTemplate, page));
			if (chunks.size() >= maxItems) {
				break;
			}
		}
		return new ListTemplateChunk(chunks);
	}
	
	private List<Page> sort(List<Page> allChildren) {
		Collections.sort(allChildren, new Comparator<Page>() {
			@Override
			public int compare(Page o1, Page o2) {
				return o1.getUpdatetimestamp().compareTo(o2.getUpdatetimestamp());
			}
		});
		return allChildren;
	}

	@AllArgsConstructor
	private class LatestNewsTemplateChunk implements TemplateChunk {

		private final String template;

		private final Page page;
		
		@Override
		public String toString(JSONObject pageProperties) {
			String title = getSafe("title");
			String content = getSafe("content");
			return template.replaceAll("\\$name", page.getName())
					.replaceAll("\\$link", RenderServlet.getEnvironment().getProperty(TemplateEnvironment.BASE_URL_PROPERTY) + "/" + page.getPath())
					.replaceAll("\\$title", title)
					.replaceAll("\\$content", content);
		}

		private String getSafe(String property) {
			try {
				JSONObject pageProperties = new JSONObject(IOUtils.toString(page.getContent(false), Dc2fConstants.CHARSET));
				return pageProperties.getString(property);
			} catch (JSONException | IOException e) {
				log.debug("cannot get " + property + " for page " + page.getPath(), e);
				return "";
			}
		}
		
	}

}
