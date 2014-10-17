var config = {
  staticMarkup: @@staticMarkup@@,
  harmony: @@harmony@@,
  sourceMaps: @@sourceMaps@@
};

var React = require('react');
var JSX = require('react/dist/JSXTransformer');

require.transform = function(source) {
  return JSX.transform(source, {
    harmony: config.harmony,
    sourceMap: config.sourceMaps
  }).code; 
};

var Component = require('@@mainComponentPath@@');
var props = @@props@@;

if (!config.staticMarkup) {
  if (React.renderToString && React.createElement) {
    React.renderToString(React.createElement(Component, props));
  } else {
    React.renderComponentToString(new Component(props));
  }
} else {
  if (React.renderToString && React.createElement) {
    React.renderToStaticMarkup(React.createElement(Component, props));
  } else {
    React.renderComponentToStaticMarkup(new Component(props));
  }
}
