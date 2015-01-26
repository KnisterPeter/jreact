function exists(path) {
  return !!(new java.io.File(path)).exists();
}
function isFile(path) {
  return !!(new java.io.File(path)).isFile();
}
function isDirectory(path) {
  return !!(new java.io.File(path)).isDirectory();
}
function getDirectory(path) {  
  if(isDirectory(path))
     return getFilename(path);
  return (new java.io.File(path)).getParent();  
}
function getFilename(path) {    
  return (new java.io.File(path)).getName();
}
function readFile(path) {
  return '' + new java.util.Scanner(new java.io.File(path), 'UTF-8')
    .useDelimiter('\\Z')
    .next();
}

function loadFromNodeModules(base, id) {
  var path = base + '/' + require.stack[0] + '/node_modules/' + id;
  var resolvedPath;
  if (exists(path)) {
    if (isFile(path)) {
      resolvedPath = path;
    } else if (isFile(path + '/' + 'package.json')) {
      var pkg = readFile(path + '/' + 'package.json');
      eval( 'pkg = '+ pkg);
      if (pkg['main']) {
        resolvedPath = path + '/' + pkg['main'];
        if(isFile(resolvedPath + '.js')) {
            resolvedPath = resolvedPath + ".js";
        }
      }
    } else if (isFile(path + '/index.js')) {
      resolvedPath = path + '/index.js';
    }
  } else if (isFile(path + '.js')) {
    resolvedPath =  path + '.js';
  }
  return resolvedPath;
}

function resolve(base, id) {
  var resolvedPath;
  if (id[0] === '.' || id[0] === '/') {
    var path = base + '/' + require.stack[0] + '/' + id;
    if (isFile(path)) {
      resolvedPath = path;
    } else if (isFile(path + '.js')) {
      resolvedPath = path + '.js';
    }
  } else {
    resolvedPath = loadFromNodeModules(base, id);
  }
  if (resolvedPath !== undefined) {
    resolvedPath = resolvedPath.replace('/./', '/');
  }
  return resolvedPath;
}

require = function(id) {
  var uri;
  for (var i = 0, n = require.paths.length; typeof uri === 'undefined' && i < n; i++) {
    uri = resolve(require.paths[i], id)
    if (typeof uri !== 'undefined') {
      uri = { uri: uri, base: require.paths[i] };
    }
  }
  if (typeof uri !== 'undefined') {
    if (require.cache[uri.uri] !== undefined) {
      return require.cache[uri.uri];
    }

    var content = readFile(uri.uri);
    var f = new Function('require', 'exports', 'module', '__filename','__dirname',  (require.transform ? require.transform(content) : content));
    var exports = {};
    var module = { id: id, uri: uri.uri, exports: exports };

    var stackPath = uri.uri
      .replace(new RegExp('^' + uri.base), '')
      .replace(/[^\/]+$/, '')
      .replace(/\/\//g, '/')
      .replace(/^\//, '')
      .replace(/\/$/, '');
    require.stack.unshift(stackPath);
    var __dirname = getDirectory(uri.uri);
    var __filename = getFilename(uri.uri);
    f.call({}, require, exports, module,__filename,__dirname);
    exports = module.exports || exports;
    require.cache[uri.uri] = exports;
    require.stack.shift();

    return exports;
  }
  throw 'Unable to resolve: ' + id;
};
require.paths = [];
require.stack = ['.'];
require.cache = {};
