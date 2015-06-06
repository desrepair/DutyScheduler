'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var ListGroup = ReactBootstrap.ListGroup;
var ReactPropTypes = React.PropTypes;

var DutyStore = require('../../stores/DutyStore');

var MemberCell = require('./MemberCell');
var AddCell = require('./AddCell');

var styles = {
  div: {
    width: '300'
  }
};

var MemberList = React.createClass({
  render: function() {
    var member_data = this.props.members;
    var members = [];
    var selected = DutyStore.getSelectedMember();

    for (var id in member_data) {
      members.push(
        <MemberCell key={id}
                    member={member_data[id]}
                    memberID={id}
                    selected={selected == id}>
        </MemberCell>
      );
    }

    return (
      <div style={styles.div}>
        <ListGroup>
          {members}
          <AddCell></AddCell>
        </ListGroup>
      </div>
    );
  }
});

module.exports = MemberList;
