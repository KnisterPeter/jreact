package de.matrixweb.jreact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author markusw
 */
public class JReact {

  private final ScriptEngine js;

  private String componentCode;

  private boolean harmony = false;
  
  private boolean sourceMaps = false;

  /**
   * This creates a {@link JReact} instance using the default
   * {@link ScriptEngine} return for the mime-type 'application/javascript' and filebased resources.
   */
  public JReact() {
	  this(new ScriptEngineManager()
      .getEngineByMimeType("application/javascript"));
  }
  
  /**
   * This creates a {@link JReact} instance using classpath-resources instead of filebased resources.
   * @param useClasspathResources
   */
  public JReact(boolean useClasspathResources) {
	  this(new ScriptEngineManager()
      .getEngineByMimeType("application/javascript"), useClasspathResources);
  }
  
  /**
   * This creates a {@link JReact} instance using the given {@link ScriptEngine} and filebased resources.
   * 
   * @param js An instance of {@link ScriptEngine} to use
   */
  public JReact(ScriptEngine js) {
	  this(js, false);
  }
  
  /**
   * This creates a {@link JReact} instance using the given {@link ScriptEngine}
   * .
   * 
   * @param js An instance of {@link ScriptEngine} to use
   * @param useClasspathResources use classpath resources instead of file-resources
   */
  public JReact(ScriptEngine js, boolean useClasspathResources) {
    this.js = js;
    
    if (useClasspathResources)
    	js.put("filesystem", new ClasspathBasedFilesystem());
    else
    	js.put("filesystem", new FilebasedFilesystem());
    
    try {
      this.js.eval(new InputStreamReader(getClass().getResourceAsStream(
          "/require.js"), "UTF-8"));
      this.js
          .eval("if (typeof process === 'undefined') { process = { env: {} }; }");
      this.js
          .eval("if (typeof console === 'undefined') { console = { log: function(s) { print(s + '\\n'); }, warn: function(s) { print(s + '\\n'); } }; }");
    } catch (final UnsupportedEncodingException | ScriptException e) {
      throw new RuntimeException("Failed to setup JavaScriptEngine", e);
    }
  }

  /**
   * Enables JSX harmony rendering. False by default.
   * 
   * @param harmony
   *          True to enable harmony
   */
  public void setHarmony(boolean harmony) {
    this.harmony = harmony;
  }
  
  /**
   * Enables JSX source-maps rendering. False by default.
   * 
   * @param sourceMaps
   *          True to enable source-maps
   */
  public void setSourceMaps(boolean sourceMaps) {
    this.sourceMaps = sourceMaps;
  }

  /**
   * @param path
   *          The filesystem path to add to the require lookup paths (e.g. the
   *          react npm module).
   * @return Returns this for method chaining
   */
  public JReact addRequirePath(final String path) {
    try {
      this.js.eval("require.paths.push('" + path + "');");
    } catch (final ScriptException e) {
      throw new RuntimeException("Failed to setup JavaScriptEngine", e);
    }
    return this;
  }

  /**
   * @param mainComponentPath
   * @param props
   *          The props to render with
   * @return Returns the render result as {@link String}
   * @throws IOException
   *           Thrown if the json conversion of the props fails
   */
  public String renderComponentToString(final String mainComponentPath,
      final Map<String, Object> props) throws IOException {
    return render(mainComponentPath, props, false);
  }

  /**
   * @param mainComponentPath
   * @param props
   *          The props to render with
   * @return Returns the render result as {@link String}
   * @throws IOException
   *           Thrown if the json conversion of the props fails
   */
  public String renderComponentToStaticMarkup(final String mainComponentPath,
      final Map<String, Object> props) throws IOException {
    return render(mainComponentPath, props, true);
  }

  /**
   * @param mainComponentPath
   * @param props
   *          The props to render with
   * @return Returns the render result as {@link String}
   * @throws IOException
   *           Thrown if the json conversion of the props fails
   */
  public String renderToString(final String mainComponentPath,
      final Map<String, Object> props) throws IOException {
    return render(mainComponentPath, props, false);
  }

  private String render(final String mainComponentPath,
      final Map<String, Object> props, boolean staticMarkup) throws IOException {
    try {
      if (componentCode == null) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            getClass().getResourceAsStream("/jreact.js"), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
          sb.append(line).append('\n');
          line = reader.readLine();
        }
        componentCode = sb.toString()
            .replace("@@staticMarkup@@", Boolean.toString(staticMarkup))
            .replace("@@harmony@@", Boolean.toString(harmony))
            .replace("@@sourceMaps@@", Boolean.toString(sourceMaps))
            .replace("@@mainComponentPath@@", mainComponentPath)
            .replace("@@props@@", new ObjectMapper().writeValueAsString(props));
      }
      return (String) this.js.eval(componentCode);
    } catch (final ScriptException e) {
      throw new RuntimeException("Failed to execute render request", e);
    }
  }

  /**
   * Resets the {@link JReact} instance.
   * 
   * Call this if you need to render different components.
   */
  public void reset() {
    componentCode = null;
  }

}
