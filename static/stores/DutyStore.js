'use strict';

var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var DutyConstants = require('../constants/DutyConstants');

var assign = require('object-assign');
var uuid = require('node-uuid');

var CHANGE_EVENT = 'change';

var _members = {
  '0123': {
    name: 'Mick',
    email: 'abcd@gmail.com',
  },
  '1234': {
    name: 'Yujin',
    email: 'bcde@gmail.com',
  },
};
var _blacklists = {};
var _points = {};

var _untitled_count = 1;

// MEMBER API

function createMember() {
  var id = uuid.v4();

  _members[id] = {
    name: 'Untitled ' + _untitled_count++
  };
  _blacklists[id] = {}
}

function updateMember(id, updates) {
  _members[id] = assign({}, _members[id], updates);
}

function removeMember(id) {
  delete _members[id];
  delete _blacklists[id];
}

// BLACKLIST API



var DutyStore = assign({}, EventEmitter.prototype, {

  getMembers: function() {
    return _members;
  },

  emitChange: function() {
    this.emit(CHANGE_EVENT);
  },

  /**
   * @param {function} callback
   */
  addChangeListener: function(callback) {
    this.on(CHANGE_EVENT, callback);
  },

  /**
   * @param {function} callback
   */
  removeChangeListener: function(callback) {
    this.removeListener(CHANGE_EVENT, callback);
  }
});

AppDispatcher.register(function(action) {

  switch(action.actionType) {
    case DutyConstants.DUTY_CREATE_MEMBER:
      createMember();
      DutyStore.emitChange();
      break;

    case DutyConstants.DUTY_REMOVE_MEMBER:
      removeMember(action.id);
      DutyStore.emitChange();
      break;

    case DutyConstants.DUTY_UPDATE_MEMBER_NAME:
      var name = action.name.trim();
      if (name != '') {
        updateMember(action.id, {name: name});
      }
      DutyStore.emitChange();
      break;
    case DutyConstants.DUTY_UPDATE_MEMBER_EMAIL:
      var email = action.email.trim();
      if (email != '') {
        updateMember(action.id, {email: email});
      }
      DutyStore.emitChange();
      break;
    case DutyConstants.DUTY_REMOVE_MEMBER_EMAIL:
      updateMember(action.id, {email: ""});
      DutyStore.emitChange();
      break;
    default:
      //no-op
      console.log('unrecognized type ' + action.actionType);
  }
});

module.exports = DutyStore;
