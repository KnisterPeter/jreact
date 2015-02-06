package de.matrixweb.jreact;

import java.io.FileNotFoundException;
import java.net.URL;

public class ClasspathBasedFilesystem implements Filesystem {

	private ClassLoader loader;

	public ClasspathBasedFilesystem() {
		loader = getClass().getClassLoader();
	}
	
	
	@Override
	public boolean isFile(String fileName) {
		URL url = loader.getResource(fileName);
		if (url == null)
			return false;
		
		String[] strings = url.getPath().split("/");
		String last = strings[strings.length-1];
		
		return last.contains(".") //TODO: not good -> there could be files without extension, but it should work for now
				&& loader.getResourceAsStream(fileName) != null; 
	}

	@Override
	public String readFile(String path) throws FileNotFoundException {
		return new java.util.Scanner(loader.getResourceAsStream(path), "UTF-8")
				.useDelimiter("\\Z")
				.next()
				.toString();
	}

	@Override
	public String getDirectory(String path) throws FileNotFoundException {
		URL url = loader.getResource(path);
		String[] strings = url.getPath().split("/");
		String last = strings[strings.length-1];
		if (last.contains(".")) //TODO: same here, files without extensions are a problem
			return strings[strings.length-2];
		return last;
	}

	@Override
	public String getFilename(String path) throws FileNotFoundException {
		URL url = loader.getResource(path);
		String[] strings = url.getPath().split("/");
		return strings[strings.length-1];
	}

}
