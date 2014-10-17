package de.matrixweb.jreact;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class JReactTest {

  private JReact setupReact011() {
    JReact react = new JReact();
    react.addRequirePath("./src/test/resources/react-0.11");
    return react;
  }

  private JReact setupReact012() {
    JReact react = new JReact();
    react.addRequirePath("./src/test/resources/react-0.12");
    return react;
  }

  @Test
  public void testRenderWithReact011() throws IOException {
    dynamicMarkup(setupReact011());
  }

  @Test
  public void testRenderStaticMarkupWithReact011() throws IOException {
    staticMarkup(setupReact011());

  }

  @Test
  public void testRenderWithReact012() throws IOException {
    dynamicMarkup(setupReact012());
  }

  @Test
  public void testRenderMultipleTimes() throws IOException {
    JReact react = setupReact012();
    for (int i = 0; i < 10; i++) {
      dynamicMarkup(react);
    }
  }

  @Test
  public void testRenderWithHarmony() throws IOException {
    JReact react = setupReact012();
    react.setHarmony(true);

    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderToString("./test-harmony.js", props);

    assertThat(result, startsWith("<div data-reactid=\""));
    assertThat(result, endsWith("\">Hello World!</div>"));
  }

  @Test
  public void testRenderWithSourceMaps() throws IOException {
    JReact react = setupReact012();
    react.setSourceMaps(true);
    dynamicMarkup(react);
  }

  @Test
  public void testRenderStaticMarkupWithReact012() throws IOException {
    staticMarkup(setupReact012());
  }

  private void dynamicMarkup(JReact react) throws IOException {
    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderToString("./test.js", props);

    assertThat(result, startsWith("<div data-reactid=\""));
    assertThat(result, endsWith("\">Hello World!</div>"));
  }

  private void staticMarkup(JReact react) throws IOException {
    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderComponentToStaticMarkup("./test.js",
        props);

    assertThat(result, is("<div>Hello World!</div>"));
  }

}
