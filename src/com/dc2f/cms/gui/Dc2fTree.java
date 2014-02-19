package com.dc2f.cms.gui;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Project;
import com.vaadin.ui.Tree;

@Slf4j
public class Dc2fTree extends Tree {

	/**
	 * unique serialization version id
	 */
	private static final long serialVersionUID = 1L;
	
	private final Dc2f dc2f;

	public Dc2fTree() {
		dc2f = Dc2fSettings.get().initDc2f();
		for (Project project : dc2f.getProjects()) {
			Dc2fTreeItem projectItem = new Dc2fTreeItem(project);
			addItem(projectItem);
			setItemCaption(projectItem, project.getName());
		}
		addExpandListener(new Dc2fExpandListener());
		setImmediate(true);
	}
	
	@ToString(of={"name"})
	@EqualsAndHashCode
	public static class Dc2fTreeItem {
		public Dc2fTreeItem(Node node) {
			name = node.getName();
			path = node.getPath();
			type = node.getClass();
		}
		@Getter
		private final String name;
		@Getter
		private final String path;
		@Getter
		private final Class<? extends Node> type;
	}
	

	public void openFolder(Dc2fTreeItem clickedOn) {
		String[] path = clickedOn.getPath().split("/");
		StringBuilder currentPath = new StringBuilder();
		for (String pathElement : path) {
			if (currentPath.length() > 0) {
				currentPath.append("/");
			}
			currentPath.append(pathElement);
			Node node = dc2f.getNodeForPath(currentPath.toString());
			if (node != null) {
				Dc2fTreeItem item = new Dc2fTreeItem(node);
				if (!isExpanded(item)) {
					expandItem(new Dc2fTreeItem(node));
				}
			}
		}
	}
	
	private class Dc2fExpandListener implements ExpandListener {

		/**
		 * unique serialization version id.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void nodeExpand(ExpandEvent event) {
			Dc2fTreeItem item = (Dc2fTreeItem) event.getItemId();
			log.debug("Expand: {}", item);
			boolean hasChildren = false;
			for (Folder child : dc2f.getChildren(item.path, Folder.class)) {
				Dc2fTreeItem childItem = new Dc2fTreeItem(child);
				addItem(childItem);
				setItemCaption(childItem, child.getName());
				setParent(childItem, item);
				hasChildren = true;
			}
			if(!hasChildren) {
				setChildrenAllowed(item, false);
			}
		}
	}

}
