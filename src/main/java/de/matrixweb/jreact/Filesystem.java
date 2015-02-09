package de.matrixweb.jreact;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Defines a file system abstraction to be used from javascript. This allows
 * different lookup mechanisms for file resolving (e.g. real file system,
 * classpath, ...)
 */
public interface Filesystem {

  public abstract boolean exists(String fileName);

  public abstract boolean isFile(String fileName);

  public abstract String readFile(String path) throws IOException;

  public abstract String getDirectory(String path) throws FileNotFoundException;

  public abstract String getFilename(String path) throws FileNotFoundException;

}
