package com.dc2f.cms.rendering.simple;

import java.util.List;

import lombok.AllArgsConstructor;

import org.json.JSONObject;

@AllArgsConstructor
public class ListTemplateChunk implements TemplateChunk {

	private final List<TemplateChunk> chunks;
	
	@Override
	public String toString(JSONObject pageProperties) {
		StringBuilder result = new StringBuilder();
		for(TemplateChunk chunk : chunks) {
			result.append(chunk.toString(pageProperties));
		}
		return result.toString();
	}

}
