'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var ListGroupItem = ReactBootstrap.ListGroupItem;

var DutyActions = require('../../actions/DutyActions');

var styles = {
  listGroupItem: {
    cursor: 'pointer',
  }
};

var AddCell = React.createClass({
  handleClick: function(event) {
    DutyActions.createMember();
  },
  render: function() {
    return (
      <ListGroupItem style={styles.listGroupItem} onClick={this.handleClick}>
        <i className="fa fa-plus-circle fa-fw" ></i>
        Add Member
      </ListGroupItem>
    );
  }
});

module.exports = AddCell;
