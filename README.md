jreact
======

react.js on server-side java (with rhino or nashorn)

Usage
=====

To setup jreact create a de.matrixweb.jreact.JReact instance, call the #addRequirePath method with the path to your script sources and/or npm modules. This is enough for setup.

To do a render cycle call the #render method with the path to your entry point script (relative to the require-path) and a map which is give to react as props for the root instance.
