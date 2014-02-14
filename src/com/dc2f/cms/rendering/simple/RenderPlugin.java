package com.dc2f.cms.rendering.simple;

import com.dc2f.cms.dao.Template;

public interface RenderPlugin {

	String getDefaultKey();

	TemplateChunk generateTemplateChunkFor(Template template, String ... renderDefinition);
}
