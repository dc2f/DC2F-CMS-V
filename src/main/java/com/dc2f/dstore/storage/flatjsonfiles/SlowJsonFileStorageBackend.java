package com.dc2f.dstore.storage.flatjsonfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc2f.dstore.storage.StorageBackend;
import com.dc2f.dstore.storage.StorageId;
import com.dc2f.dstore.storage.StoredCommit;
import com.dc2f.dstore.storage.StoredFlatNode;
import com.dc2f.dstore.storage.simple.SimpleUUIDStorageId;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class SlowJsonFileStorageBackend implements StorageBackend {
	
	private final static String ROOT_COMMIT_ID = "rootCommitId";
	private final static String BRANCH_STORAGE_ID = "branchstorage";
	private Logger logger = LoggerFactory.getLogger(SlowJsonFileStorageBackend.class);
	
	private static final String FILE_TYPE_MISC = "misc";
	private static final String FILE_TYPE_CHILDREN = "children";
	private static final String FILE_TYPE_NODE = "node";
	private static final String FILE_TYPE_COMMIT = "commit";

	private File flatStoreDir;

	public SlowJsonFileStorageBackend(File path) {
		flatStoreDir = new File(path, "flatstore");
		flatStoreDir.mkdirs();
		if (!flatStoreDir.exists()) {
			throw new RuntimeException("target directory does not exist. " + flatStoreDir.getAbsolutePath());
		}
	}

	@Override
	public StorageId generateUniqueId() {
		return SimpleUUIDStorageId.generateRandom();
	}

	@Override
	public StoredCommit readCommit(StorageId id) {
		try {
			JSONObject commitInfo = readFile(id, FILE_TYPE_COMMIT);
			if (commitInfo == null) {
				return null;
			}
			StorageId storageId = readStorageId(commitInfo.getString("storageId"));
			StorageId[] parents = readStorageIdArray(commitInfo.optJSONArray("parents"));
			StorageId rootNode = readStorageId(commitInfo.getString("rootNode"));
			return new StoredCommit(storageId, parents, rootNode);
		} catch (JSONException e) {
			logger.error("Error while reading commit.", e);
		}
		return null;
	}

	@Override
	public void writeCommit(StoredCommit commit) {
		try {
			JSONObject commitInfo = new JSONObject();
			commitInfo.put("storageId", commit.getId());
			commitInfo.put("parents", storageIdArrayToJsonArray(commit.getParents()));
			commitInfo.put("rootNode", storeStorageId(commit.getRootNode()));
			writeFile(commit.getId(), commitInfo, FILE_TYPE_COMMIT);
		} catch(JSONException e) {
			logger.error("error writing commit", e);
		}
	}

	@Override
	public StoredCommit readBranch(String name) {
		SimpleUUIDStorageId branchesStorageId = new SimpleUUIDStorageId(BRANCH_STORAGE_ID);
		JSONObject branches = readFile(branchesStorageId, FILE_TYPE_MISC);
		try {
			StorageId commitId = readStorageId(branches.getString(name));
			return readCommit(commitId);
		} catch (JSONException e) {
			logger.error("Error while loading branch.", e);
			return null;
		}
	}

	@Override
	public void writeBranch(String name, StoredCommit commit) {
		SimpleUUIDStorageId branchesStorageId = new SimpleUUIDStorageId(BRANCH_STORAGE_ID);
		JSONObject branches = readFile(branchesStorageId, FILE_TYPE_MISC);
		if (branches == null) {
			branches = new JSONObject();
		}
		try {
			branches.put(name, storeStorageId(commit.getId()));
			writeFile(branchesStorageId, branches, FILE_TYPE_MISC);
		} catch (JSONException e) {
			logger.error("Error writing branch.", e);
		}
	}

	@Override
	public StoredFlatNode readNode(StorageId id) {
		try {
			JSONObject obj = readFile(id, FILE_TYPE_NODE);
			String name = obj.getString("name");
			StorageId children = readStorageId(obj.optString("children", null));
			StorageId parentId = readStorageId(obj.optString("parentid", null));
			StorageId properties = readStorageId(obj.optString("properties", null));
			return new StoredFlatNode(id, name, parentId, children, properties);
		} catch (JSONException e) {
			logger.error("Error reading node.", e);
			return null;
		}
	}
	
	private StorageId readStorageId(String string) {
		if (string == null) {
			return null;
		}
		return new SimpleUUIDStorageId(string);
	}

	private String storeStorageId(StorageId id) {
		if (id == null) {
			return null;
		}
		return id.getIdString();
	}

	@Override
	public StoredFlatNode writeNode(StoredFlatNode node) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("name", node.getName());
			obj.put("children", storeStorageId(node.getChildren()));
			obj.put("parentid", storeStorageId(node.getParentId()));
			obj.put("properties", storeStorageId(node.getProperties()));
			writeFile(node.getStorageId(), obj, FILE_TYPE_NODE);
			StoredFlatNode newNode = new StoredFlatNode(node);
			return newNode;
		} catch (JSONException e) {
			logger.error("Error writing node.", e);
			return null;
		}
	}

	@Override
	public SimpleUUIDStorageId getDefaultRootCommitId() {
		return new SimpleUUIDStorageId(ROOT_COMMIT_ID);
	}

	@Override
	public Map<String, StorageId[]> readChildren(StorageId childrenStorageId) {
		if (childrenStorageId == null) {
			return null;
		}
		JSONObject tmp = readFile(childrenStorageId, FILE_TYPE_CHILDREN);
		Map<String, StorageId[]> ret = new HashMap<String, StorageId[]>();
		
		try {
			for (@SuppressWarnings("unchecked")
			Iterator<String> it = tmp.keys() ; it.hasNext() ; ) {
				String name = it.next();
				JSONArray arr = tmp.getJSONArray(name);
				StorageId[] children = readStorageIdArray(arr);
				
				ret.put(name, children);
			}
		} catch(JSONException e) {
			logger.error("Error while reading children.", e);
		}
		
		return ret;
	}

	private StorageId[] readStorageIdArray(JSONArray arr) throws JSONException {
		if (arr == null) {
			return null;
		}
		StorageId[] children = new StorageId[arr.length()];
		for (int i = 0 ; i < children.length ; i++) {
			children[i] = new SimpleUUIDStorageId(arr.getString(i));
		}
		return children;
	}

	@Override
	public StorageId writeChildren(Map<String, StorageId[]> children) {
		JSONObject tmp = new JSONObject();
		try {
			for (Map.Entry<String, StorageId[]> entry : children.entrySet()) {
				tmp.put(entry.getKey(), storageIdArrayToJsonArray(entry.getValue()));
			}
			StorageId childrenStorageId = generateUniqueId();
			writeFile(childrenStorageId, tmp, FILE_TYPE_CHILDREN);
			return childrenStorageId;
		} catch (JSONException e) {
			logger.error("Error while storing children.", e);
		}
		return null;
	}
	
	
	private JSONArray storageIdArrayToJsonArray(
			StorageId[] children) {
		if (children == null) {
			return null;
		}
		JSONArray ret = new JSONArray();
		for (StorageId child : children) {
			ret.put(child.getIdString());
		}
		return ret;
	}

	private void writeFile(StorageId store, JSONObject json, String type) {
		try {
			FileWriter output = new FileWriter(getFileForStorageId(store, type));
			output.write(json.toString());
			output.close();
		} catch (IOException e) {
			logger.error("Error while storing json into file.", e);
			return;
		}
	}

	private File getFileForStorageId(StorageId storageId, String type) {
		return new File(flatStoreDir, type + "." + storageId.getIdString() + ".txt");
	}

	private JSONObject readFile(StorageId storageId, String type) {
		try {
			String content = Files.toString(getFileForStorageId(storageId, type), Charsets.UTF_8);
			return new JSONObject(content);
		} catch (IOException | JSONException e) {
			logger.error("Error while reading json file.", e);
			return null;
		}
	}
}
