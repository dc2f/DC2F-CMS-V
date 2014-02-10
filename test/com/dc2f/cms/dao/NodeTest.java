package com.dc2f.cms.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeTest {
	@Test
	public void testGetParentPath() {
		Node node = new Node("test", "/my/test/path");
		assertEquals("/my/test", node.getParentPath());
	}
}
