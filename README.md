jreact
======

react.js on server-side java (with rhino or nashorn)

Usage
=====

To setup jreact create a de.matrixweb.jreact.JReact instance, call the #addRequirePath method with the path to your script sources and/or npm modules. This is enough for setup.

To do a render cycle call the #render method with the path to your entry point script (relative to the require-path) and a map which is give to react as props for the root instance.

Example
=======

```java
JReact react = new JReact();
react.addRequirePath("./src");
react.addRequirePath("./src/node_modules");

Map<String, Object> props = new HashMap<>();
props.put("text", "Hello World!");
// The app.js will be required from the above configured require-paths
String result = react.renderToString("./app.js", props);
```

For further examples look here: https://github.com/KnisterPeter/jreact-examples
