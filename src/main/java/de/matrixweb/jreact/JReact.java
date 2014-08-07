package de.matrixweb.jreact;

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

  /**
   *
   */
  public JReact() {
    this.js = new ScriptEngineManager().getEngineByMimeType("application/javascript");
    try {
      this.js.eval(new InputStreamReader(getClass().getResourceAsStream("/require-impl.js"), "UTF-8"));
      this.js.eval("process = { env: {} };");
    } catch (final UnsupportedEncodingException | ScriptException e) {
      throw new RuntimeException("Failed to setup JavaScriptEngine", e);
    }
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
  public String render(final String mainComponentPath, final Map<String, Object> props) throws IOException {
    try {
      final StringBuilder src = new StringBuilder();
      src.append("var React = require('react');\n");
      src.append("var JSX = require('react/dist/JSXTransformer');\n");
      src.append("require.transform = function(source) { return JSX.transform(source, {}).code; };\n");
      src.append("var Component = require('").append(mainComponentPath).append("');\n");
      src.append("React.renderComponentToString(new Component(").append(new ObjectMapper().writeValueAsString(props))
          .append("));\n");
      return (String) this.js.eval(src.toString());
    } catch (final ScriptException e) {
      throw new RuntimeException("Failed to execute render request", e);
    }
  }

}
