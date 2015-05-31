'use strict';

var React = require('react');

var DutyStore = require('./stores/DutyStore');

var MemberList = require('./views/MemberList');

function getState() {
  return {
    members: DutyStore.getMembers(),
  }
}

var DutyScheduler = React.createClass({
  getInitialState: function() {
    return getState();
  },

  componentDidMount: function() {
    DutyStore.addChangeListener(this._onChange);
  },

  componentDidUnmount: function() {
    DutyStore.removeChangeListener(this._onChange);
  },

  render: function() {
    return (
      <MemberList members={this.state.members}>
      </MemberList>
    );
  },

  _onChange: function() {
    this.setState(getState());
  }
});

React.render(
  <DutyScheduler />,
  document.getElementById('container')
);
