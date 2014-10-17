var React = require('react');

var Test = React.createClass({
  render: function() {
    return (
      <div>{this.props.text}</div>
    );
  }
});

module.exports = Test;
