package de.matrixweb.jreact;

import java.io.FileNotFoundException;

public interface Filesystem {

  public abstract boolean exists(String fileName);

  public abstract boolean isFile(String fileName);

  public abstract String readFile(String path) throws FileNotFoundException;

  public abstract String getDirectory(String path) throws FileNotFoundException;

  public abstract String getFilename(String path) throws FileNotFoundException;

}