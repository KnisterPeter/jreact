var React = require('react');

var Test = React.createClass({
  render() {
    return (
      <div>{this.props.text}</div>
    );
  }
});

module.exports = Test;
