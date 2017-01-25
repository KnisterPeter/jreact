package de.matrixweb.jreact;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

@SuppressWarnings("javadoc")
public class JReactClasspathTest {

  private JReact setupReact011() {
    final JReact react = new JReact(true);
    react.addRequirePath("react-0.11");
    return react;
  }

  private JReact setupReact012() {
    final JReact react = new JReact(true);
    react.addRequirePath("react-0.12");
    return react;
  }

  private JReact setupReact013() {
    final JReact react = new JReact();
    react.addRequirePath("./src/test/resources/react-0.13");
    return react;
  }

  private JReact setupReact014() {
    final JReact react = new JReact();
    react.addRequirePath("./src/test/resources/react-0.14");
    return react;
  }

  private JReact setupReact015() {
    final JReact react = new JReact();
    react.addRequirePath("./src/test/resources/react-0.15");
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
    final JReact react = setupReact012();
    for (int i = 0; i < 10; i++) {
      dynamicMarkup(react);
    }
  }

  @Test
  public void testRenderWithHarmony() throws IOException {
    final JReact react = setupReact012();
    react.setHarmony(true);

    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderToString("./test-harmony.js", props);

    assertThat(result, startsWith("<div data-reactid=\""));
    assertThat(result, endsWith("\">Hello World!</div>"));
  }

  @Test
  public void testRenderWithSourceMaps() throws IOException {
    final JReact react = setupReact012();
    react.setSourceMaps(true);
    dynamicMarkup(react);
  }

  @Test
  public void testRenderStaticMarkupWithReact012() throws IOException {
    staticMarkup(setupReact012());
  }

  @Test
  public void testDynamicRenderWithReact013() throws IOException {
    dynamicMarkup(setupReact013());
  }

  @Test
  public void testStaticRenderWithReact013() throws IOException {
    staticMarkup(setupReact013());
  }

  @Test
  public void testDynamicRenderWithReact014() throws IOException {
    dynamicMarkup(setupReact014());
  }

  @Test
  public void testStaticRenderWithReact014() throws IOException {
    staticMarkup(setupReact014());
  }

  @Test
  public void testDynamicRenderWithReact015() throws IOException {
    dynamicMarkup15(setupReact015());
  }

  @Test
  public void testStaticRenderWithReact015() throws IOException {
    staticMarkup(setupReact015());
  }

  private void dynamicMarkup(final JReact react) throws IOException {
    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderToString("./test.js", props);

    assertThat(result, startsWith("<div data-reactid=\""));
    assertThat(result, endsWith("\">Hello World!</div>"));
  }

  private void dynamicMarkup15(final JReact react) throws IOException {
    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderToString("./test.js", props);

    assertThat(result, startsWith("<div data-reactroot=\""));
    assertThat(result, endsWith("\">Hello World!</div>"));
  }

  private void staticMarkup(final JReact react) throws IOException {
    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = react.renderComponentToStaticMarkup("./test.js", props);

    assertThat(result, is("<div>Hello World!</div>"));
  }

}
