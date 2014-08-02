package com.dc2f.cms.rendering;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.dao.Template;
import com.dc2f.cms.rendering.simple.RenderPlugin;
import com.dc2f.cms.rendering.simple.SimpleDc2fRenderer;
import com.dc2f.cms.rendering.simple.StringTemplateChunk;
import com.dc2f.cms.rendering.simple.TemplateChunk;

public class SimpleDc2fRendererTest {
	@Test
	public void testSimpleString() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is my test template."));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is my test template.", renderedPage);
	}
	
	@Test
	public void testVariableReplacement() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is {my} test template."));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is our test template.", renderedPage);
	}
	
	@Test
	public void testMultipleVariableReplacement() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("{this} is {my} test template."));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("That is our test template.", renderedPage);
	}
	
	@Test
	public void testVariableReplacementAtEnd() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is my test template{point}"));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is my test template.", renderedPage);
	}
	
	@Test
	public void testMultiplinePluginDefinition() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is my {remove:\nmulitline}test template."));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is my test template.", renderedPage);
	}
	
	@Test
	public void testPluginsMixedWithVariables() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is {my} {remove:\nmulitline}test template."));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is our test template.", renderedPage);
	}
	
	@Test
	public void testPlugins() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is my {plugin:plugin} test {plugin:template}."));
		renderer.registerPlugin(new RenderPlugin() {
			@Override
			public String getDefaultKey() {
				return "plugin";
			}
			@Override
			public TemplateChunk generateTemplateChunkFor(Template template, String ... renderDefinition) {
				return new StringTemplateChunk(renderDefinition[1]);
			}
		});
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is my plugin test template.", renderedPage);
	}
	
	@Test
	public void testMissingPlugin() throws IOException {
		SimpleDc2fRenderer renderer = new SimpleDc2fRenderer(getDc2fMockup("This is my {notexistentplugin:plugin}test template."));
		String renderedPage = IOUtils.toString(renderer.render(getPageMockup()), Dc2fConstants.CHARSET);
		assertEquals("This is my test template.", renderedPage);
	}

	private Page getPageMockup() {
		Page page = new Page("page.html", "page.html");
		String content = "{\"my\":\"our\",\"this\":\"That\", \"point\":\".\"}";
		page.setContent(new ByteArrayInputStream(content.getBytes(Dc2fConstants.CHARSET)));
		return page;
	}

	private Dc2f getDc2fMockup(final String templateSource) {
		Dc2f dc2f = EasyMock.createMock(Dc2f.class);
		EasyMock.expect(dc2f.getNodeForPath(EasyMock.anyString())).andAnswer(new IAnswer<Node>() {

			@Override
			public Node answer() throws Throwable {
				Template template = new Template("template.html", "template.html");
				template.setContent(new ByteArrayInputStream(templateSource.getBytes(Dc2fConstants.CHARSET)));
				return template;
			}
		}).anyTimes();
		EasyMock.replay(dc2f);
		return dc2f;
	}
}
