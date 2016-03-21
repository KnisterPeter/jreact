var React = require('react');

var Test = React.createClass({
  render: function() {
    return (
      React.DOM.div(null, this.props.text)
    );
  }
});

module.exports = Test;
