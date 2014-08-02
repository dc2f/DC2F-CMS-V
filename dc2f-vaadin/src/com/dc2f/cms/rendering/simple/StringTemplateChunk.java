package com.dc2f.cms.rendering.simple;

import org.json.JSONObject;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StringTemplateChunk implements TemplateChunk {
	private final String content;
	
	public String toString(JSONObject pageProperties) {
		return content;
	}
}
