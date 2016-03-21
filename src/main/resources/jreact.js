var config = {
  staticMarkup: @@staticMarkup@@,
  harmony: @@harmony@@,
  sourceMaps: @@sourceMaps@@
};

var React = require('react');
var ReactDOM;
var renderToString;
var renderToStaticMarkup;

var JSX;
var version = React.version.match(/(\d+)\.(\d+)\.([-0-9a-zA-Z]+)/);
var major = parseInt(version[1], 10);
var minor = parseInt(version[2], 10);
if (major === 0 && minor < 13) {
  JSX = require('react/dist/JSXTransformer');
  require.transform = function(source) {
    return JSX.transform(source, {
      harmony: config.harmony,
      sourceMap: config.sourceMaps
    }).code; 
  };
  renderToString = React.renderToString;
  renderToStaticMarkup = React.renderToStaticMarkup;
} else if (major === 0 && minor === 13) {
  renderToString = React.renderToString;
  renderToStaticMarkup = React.renderToStaticMarkup;
} else if (major === 0 && minor >= 14) {
  ReactDOM = require('react-dom/server');
  renderToString = ReactDOM.renderToString;
  renderToStaticMarkup = ReactDOM.renderToStaticMarkup;
}

var Component = require('@@mainComponentPath@@');
var props = @@props@@;

if (!config.staticMarkup) {
  if (renderToString && React.createElement) {
    renderToString(React.createElement(Component, props));
  } else {
    React.renderComponentToString(new Component(props));
  }
} else {
  if (renderToStaticMarkup && React.createElement) {
    renderToStaticMarkup(React.createElement(Component, props));
  } else {
    React.renderComponentToStaticMarkup(new Component(props));
  }
}
