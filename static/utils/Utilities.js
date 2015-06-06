'use strict';
var assign = require('object-assign');

var m = function() {
  var res = {};
  for (var i=0; i < arguments.length; ++i) {
    if (arguments[i]) {
      assign(res, arguments[i]);
    }
  }
  return res;
}

module.exports = {m: m};
