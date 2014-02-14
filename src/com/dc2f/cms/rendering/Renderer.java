package com.dc2f.cms.rendering;

import java.io.InputStream;

import com.dc2f.cms.dao.Page;
import com.dc2f.cms.rendering.simple.RenderPlugin;


public interface Renderer {

	InputStream render(Page page);

	void registerPlugin(RenderPlugin plugin);

}
