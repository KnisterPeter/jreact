package de.matrixweb.jreact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Implements a classpath based {@link Filesystem}.
 */
public class ClasspathBasedFilesystem implements Filesystem {

  private final ClassLoader loader;

  /**
   *
   */
  public ClasspathBasedFilesystem() {
    this.loader = getClass().getClassLoader();
  }

  /**
   * @param loader
   *          The {@link ClassLoader} used for file resolving.
   */
  public ClasspathBasedFilesystem(final ClassLoader loader) {
    this.loader = loader;
  }

  @Override
  public boolean isFile(final String fileName) {
    final URL url = this.loader.getResource(fileName);
    if (url == null) {
      return false;
    }

    final String[] strings = url.getPath().split("/");
    final String last = strings[strings.length - 1];

    // TODO: not good -> there could be files without extension, but it should
    // work for now
    if (last.contains(".")) {
      try (InputStream in = this.loader.getResourceAsStream(fileName)) {
        return in != null;
      } catch (final IOException e) {
        // Just ignore closing exeception (we can't do anything)
      }
    }
    return false;
  }

  @Override
  public String readFile(final String path) throws IOException {
    try (InputStream in = this.loader.getResourceAsStream(path)) {
      return new java.util.Scanner(in, "UTF-8").useDelimiter("\\Z").next().toString();
    }
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
