package com.dc2f.cms.rendering;

import java.io.InputStream;

import com.dc2f.cms.dao.Page;


public interface Renderer {

	InputStream render(Page page);

}
