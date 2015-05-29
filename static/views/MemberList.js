'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var ListGroup = ReactBootstrap.ListGroup;
var ListGroupItem = ReactBootstrap.ListGroupItem;

var Member = require('./Member');

var MemberList = React.createClass({
  render: function() {
    var members = this.props.data.map(function(member, index) {
      return (
        <ListGroupItem key={index}>
          <Member data={member}>
          </Member>
        </ListGroupItem>
      );
    });
    return (
      <div>
        <ListGroup>
          {members}
        </ListGroup>
      </div>
    );
  }
});

module.exports = MemberList;
