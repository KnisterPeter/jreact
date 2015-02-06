package de.matrixweb.jreact;

import java.io.File;
import java.io.FileNotFoundException;

public class FilebasedFilesystem implements Filesystem {

	private String fileBase;

	public FilebasedFilesystem() {
		this(".");
	}
	
	public FilebasedFilesystem(String fileBase) {
		this.fileBase = fileBase;
	}

	@Override
	public boolean isFile(String fileName) {
		return new File(fileBase, fileName.toString()).isFile();
	}

	@Override
	public String readFile(String path) throws FileNotFoundException {
		return new java.util.Scanner(new java.io.File(fileBase, path), "UTF-8")
				.useDelimiter("\\Z").next().toString();
	}

	@Override
	public String getDirectory(String path) throws FileNotFoundException {
		File dir = new File(fileBase, path);
		return dir.isDirectory() ? dir.getName() :  dir.getParent(); 
	}
	
	@Override
	public String getFilename(String path) throws FileNotFoundException {
		return new File(fileBase, path).getName();
	}

	@Override
	public boolean exists(String fileName) {
		return new File(fileName).exists();
	}

}
