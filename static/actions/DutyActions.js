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

  toggleSelectMember: function(id) {
    AppDispatcher.dispatch({
      actionType: DutyConstants.DUTY_TOGGLE_SELECT_MEMBER,
      id: id,
    });
  },
};

module.exports = DutyActions;
