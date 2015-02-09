package de.matrixweb.jreact;

import java.io.FileNotFoundException;
import java.net.URL;

public class ClasspathBasedFilesystem implements Filesystem {

  private ClassLoader loader;

  public ClasspathBasedFilesystem() {
    this.loader = getClass().getClassLoader();
  }

  @Override
  public boolean isFile(final String fileName) {
    final URL url = this.loader.getResource(fileName);
    if (url == null) {
      return false;
    }

    final String[] strings = url.getPath().split("/");
    final String last = strings[strings.length - 1];

    return last.contains(".") // TODO: not good -> there could be files without
        // extension, but it should work for now
        && this.loader.getResourceAsStream(fileName) != null;
  }

  @Override
  public String readFile(final String path) throws FileNotFoundException {
    return new java.util.Scanner(this.loader.getResourceAsStream(path), "UTF-8").useDelimiter("\\Z").next().toString();
  }

  @Override
  public String getDirectory(final String path) throws FileNotFoundException {
    final URL url = this.loader.getResource(path);
    final String[] strings = url.getPath().split("/");
    final String last = strings[strings.length - 1];
    if (last.contains(".")) {
      // problem
      return strings[strings.length - 2];
    }
    return last;
  }

  @Override
  public String getFilename(final String path) throws FileNotFoundException {
    final URL url = this.loader.getResource(path);
    final String[] strings = url.getPath().split("/");
    return strings[strings.length - 1];
  }

  @Override
  public boolean exists(final String fileName) {
    return this.loader.getResource(fileName) != null;
  }
}
