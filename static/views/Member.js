'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');

var Member = React.createClass({
  render: function() {
    return <span>{this.props.data.name}</span>;
  }
});

module.exports = Member;
