package de.matrixweb.jreact;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

@SuppressWarnings("javadoc")
public class JReactTest {

  private JReact react;

  @Before
  public void setUp() {
    this.react = new JReact().addRequirePath("./src/test/resources")
        .addRequirePath("./src/test/resources/node_modules");
  }

  @Test
  public void testRender() throws IOException {
    final Map<String, Object> props = new HashMap<>();
    props.put("text", "Hello World!");
    final String result = this.react.render("./test.js", props);
    assertThat(result, startsWith("<div data-reactid=\""));
    assertThat(result, endsWith("\">Hello World!</div>"));
  }

}
