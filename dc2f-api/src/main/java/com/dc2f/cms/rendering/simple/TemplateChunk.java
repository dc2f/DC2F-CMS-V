package com.dc2f.cms.rendering.simple;

import org.json.JSONObject;

public interface TemplateChunk {
	public String toString(JSONObject pageProperties);
}
