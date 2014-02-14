package com.dc2f.cms.rendering.simple;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.dao.Template;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.dc2f.cms.rendering.Renderer;


@Slf4j
public class SimpleDc2fRenderer implements Renderer {

	private final Dc2f dc2f;
	
	private HashMap<String, RenderPlugin> plugins = new HashMap<String, RenderPlugin>();
	
	public SimpleDc2fRenderer(Dc2f dc2fDao) {
		dc2f = dc2fDao;
	}

	@Override
	public InputStream render(Page page) {
		try {
			RenderableTemplate template = findTemplateForPage(page);
			String pagePropertiesString = IOUtils.toString(page.getContent(false), Dc2fConstants.CHARSET);
			JSONObject pageProperties = new JSONObject(pagePropertiesString);
			return template.render(pageProperties);
		} catch (IOException | JSONException e) {
			throw new Dc2fCmsError("Cannot read content of page " + page, e);
		}
	}
	
	private RenderableTemplate findTemplateForPage(Page page) {
		String projectPath = page.getPath().replaceAll("/.*$", "");
		Node node = dc2f.getNodeForPath(projectPath + "/template.html");
		if(node instanceof Template) {
			return new RenderableTemplate((Template) node);
		}
		return null;
	}

	private class RenderableTemplate {
		
		private ArrayList<TemplateChunk> chunks = new ArrayList<TemplateChunk>();
		
		private RenderableTemplate(Template template) {
			try {
				InputStream source = template.getContent(false);
				String templateSource = IOUtils.toString(source, Dc2fConstants.CHARSET);
				Matcher templateVariableMatcher = Pattern.compile("\\{((?:(.*?):)?.*?)\\}").matcher(templateSource);
				int lastEnd = 0;
				while(templateVariableMatcher.find()) {
					int start = templateVariableMatcher.start();
					if(lastEnd != start) {
						chunks.add(new StringTemplateChunk(templateSource.substring(lastEnd, start)));
					}
					String pluginName = templateVariableMatcher.group(2);
					if (pluginName != null) { //renderplugin
						RenderPlugin plugin = plugins.get(pluginName);
						if (plugin != null) {
							chunks.add(plugin.generateTemplateChunkFor(template, templateVariableMatcher.group(1)));
						}
					} else {
						chunks.add(new VariableTemplateChunk(templateVariableMatcher.group(1)));
					}
					lastEnd = templateVariableMatcher.end();
				}
				if(lastEnd != templateSource.length()) {
					chunks.add(new StringTemplateChunk(templateSource.substring(lastEnd)));
				}
			} catch (IOException e) {
				throw new Dc2fCmsError("Cannot parse template source.", e);
			}
			
		}

		public InputStream render(JSONObject pageProperties) {
			return new RenderedTemplateInputStream(this, pageProperties);
		}
	}
	
	private class RenderedTemplateInputStream extends InputStream {

		private int chunkIndex = 0;
		
		private int bufferPos = 0;
		
		private byte[] buffer;
		
		final RenderableTemplate template;
		
		final JSONObject properties;
		
		private RenderedTemplateInputStream(RenderableTemplate renderableTemplate, JSONObject variableValues) {
			template = renderableTemplate;
			properties = variableValues;
		}
		
		@Override
		public int read() throws IOException {
			if (buffer == null || bufferPos >= buffer.length) {
				if (chunkIndex < template.chunks.size()) {
					buffer = template.chunks.get(chunkIndex++).toString(properties).getBytes(Dc2fConstants.CHARSET);
					bufferPos = 0;
				} else {
					return -1;
				}
			}
			return buffer[bufferPos++];
		}
		
	}
	
	@AllArgsConstructor
	private class VariableTemplateChunk implements TemplateChunk {
		private final String variableName;
		
		public String toString(JSONObject pageProperties) {
			try {
				return pageProperties.getString(variableName);
			} catch (JSONException e) {
				log.debug("Cannot resolve variable \"{}\" for page.", variableName);
				return "";
			}
		}
	}

	@Override
	public void registerPlugin(RenderPlugin plugin) {
		plugins.put(plugin.getDefaultKey(), plugin);
		
	}
}
