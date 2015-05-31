'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var ListGroupItem = ReactBootstrap.ListGroupItem;

var MemberCell = React.createClass({
  render: function() {
    return (
      <ListGroupItem>
        <span>{this.props.member.name}</span>
      </ListGroupItem>
    );
  }
});

module.exports = MemberCell;
