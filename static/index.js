'use strict';

var React = require('react');

var MemberList = require('./views/MemberList');

var MOCK_DATA = {
  members: [
    {
      name: "Mick"
    },
    {
      name: "Yujin"
    }
  ]
};

var DutyScheduler = React.createClass({
  getInitialState: function() {
    return {data: MOCK_DATA};
  },
  render: function() {
    return (
      <MemberList data={this.state.data.members}>
      </MemberList>
    );
  }
});

React.render(
  <DutyScheduler />,
  document.getElementById('container')
);
