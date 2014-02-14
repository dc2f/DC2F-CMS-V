package com.dc2f.cms.rendering.simple;

import java.util.ArrayList;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Template;
import com.dc2f.cms.rendering.serlvet.RenderServlet;
import com.dc2f.cms.rendering.serlvet.TemplateEnvironment;

@AllArgsConstructor
public class TopNavigationPlugin implements RenderPlugin {

	@Getter
	private final Dc2f dc2f;

	@Override
	public String getDefaultKey() {
		return "nav";
	}

	@Override
	public TemplateChunk generateTemplateChunkFor(Template template,
			String renderDefinition) {
		String linkTemplate = renderDefinition.replaceFirst("^.*?:", "");
		String projectPath = template.getParentPath();
		String homePath = projectPath + "/" + linkTemplate.replaceAll(":.*", "");
		linkTemplate = linkTemplate.replaceFirst("^.*?:", "");
		ArrayList<TemplateChunk> chunks = new ArrayList<>();
		for(Folder folder : dc2f.getChildren(homePath, Folder.class)) {
			chunks.add(new NavigationTemplateChunk(linkTemplate, folder));
		}
		return new ListTemplateChunk(chunks);
	}
	
	@AllArgsConstructor
	private class NavigationTemplateChunk implements TemplateChunk {

		private final String template;

		private final Folder folder;
		
		@Override
		public String toString(JSONObject pageProperties) {
			return template.replaceAll("\\$name", folder.getName())
					.replaceAll("\\$link", RenderServlet.getEnvironment().getProperty(TemplateEnvironment.BASE_URL_PROPERTY) + "/" + folder.getPath());
		}
		
	}

}
