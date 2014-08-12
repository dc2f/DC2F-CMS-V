package com.dc2f.cms.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.dc2f.cms.dao.constants.MagicPropertyValues;

import lombok.Getter;
import lombok.Setter;

public class File extends Node {
	public File(String name, Folder parent) {
		super(name, parent);
	}
	
	public File(String name, Folder parent, String relativePath) {
		super(name, parent, relativePath);
	}
	
	public File(String name, String path) {
		super(name, path);
	}
	
	public String getNodeType() {
		return MagicPropertyValues.NODE_TYPE_FILE;
	}
	
	@Setter @Getter
	private String mimetype;
	
	@Setter @Getter
	private Long updatetimestamp;
	
	private InputStream content;
	
	private MultiReadInputStreamReader bufferedContent;

	public void setContent(InputStream newContent) {
		synchronized(this) {
			bufferedContent = null;
			content = newContent;
		}
	}
	
	public InputStream getContent(boolean exclusive) {
		if (exclusive) {
			if (content != null) {
				synchronized(this) {
					if (content != null) {
						InputStream result = content;
						content = null;
						return result;
					}
				}
			}
		} else {
			if (bufferedContent == null && content != null) {
				synchronized(this) {
					if (bufferedContent == null && content != null) {
						bufferedContent = new MultiReadInputStreamReader(content);
						content = null;
					}
				}
			}
			if (bufferedContent != null) {
				return bufferedContent.newInputSteam();
			}
		}
		return null;
	}
	
	public class MultiReadInputStreamReader {
		
		InputStream originalContent;
		
		final int chunkSize = 1024;

		List<int[]> buffer = new ArrayList<int[]>();
		
		public MultiReadInputStreamReader(InputStream content) {
			originalContent = content;
		}

		public InputStream newInputSteam() {
			return new MultipleReadInputStream(this);
		}

		public int readPos(int pos) throws IOException {
			if (pos >= buffer.size() * chunkSize) {
				synchronized(this) {
					if (pos >= buffer.size() * chunkSize) {
						int[] nextChunk = new int[chunkSize];
						for (int i = 0; i < chunkSize; i++) {
							nextChunk[i] = originalContent.read();
						}
						buffer.add(nextChunk);
					}
				}
			}
			int chunkIndex = pos / chunkSize;
			int chunkPos = pos % chunkSize;
			return buffer.get(chunkIndex)[chunkPos];
		}


	}
	
	public class MultipleReadInputStream extends InputStream {

		MultiReadInputStreamReader reader;
		
		int pos = 0;
		
		public MultipleReadInputStream(
				MultiReadInputStreamReader multiReadInputStreamReader) {
			reader = multiReadInputStreamReader;
		}

		@Override
		public int read() throws IOException {
			return reader.readPos(pos++);
		}
		
	}

}
