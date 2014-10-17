/** @jsx React.DOM */
var React = require('react');

var Test = React.createClass({
  render: function() {
    //return React.DOM.div(null, null);
    return (
      <div>{this.props.text}</div>
    );
  }
});

module.exports = Test;
