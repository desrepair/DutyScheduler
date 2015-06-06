'use strict';

var AppDispatcher = require('../dispatcher/AppDispatcher');
var DutyConstants = require('../constants/DutyConstants');

var DutyActions = {

  createMember: function() {
    AppDispatcher.dispatch({
      actionType: DutyConstants.DUTY_CREATE_MEMBER,
    });
  },

  updateMemberName: function(id, name) {
    AppDispatcher.dispatch({
      actionType: DutyConstants.DUTY_UPDATE_MEMBER_NAME,
      name: name,
      id: id,
    });
  },

  removeMember: function(id) {
    AppDispatcher.dispatch({
      actionType: DutyConstants.DUTY_REMOVE_MEMBER,
      id: id,
    });
  },

  updateMemberEmail: function(id, email) {
    AppDispatcher.dispatch({
      actionType: DutyConstants.DUTY_UPDATE_MEMBER_EMAIL,
      id: id,
      email: email,
    });
  },
  
  removeMemberEmail: function(id) {
    AppDispatcher.dispatch({
      actionType: DutyConstants.DUTY_REMOVE_MEMBER_EMAIL,
      id: id,
    });
  },

};

module.exports = DutyActions;
