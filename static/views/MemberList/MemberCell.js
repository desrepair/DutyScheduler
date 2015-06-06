'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var ListGroupItem = ReactBootstrap.ListGroupItem;
var DutyActions = require('../../actions/DutyActions');

var styles = {
  input: {
    width: 200,
    display: 'inline',
  },
  iconDelete: {
    fontSize: '18',
    color: 'red',
    cursor: 'pointer',
  },
  iconDone: {
    fontSize: '18',
    color: 'green',
    cursor: 'pointer',
  },
  iconEdit: {
    fontSize: '18',
    color: 'gray',
    cursor: 'pointer',
  },
}

var MemberCell = React.createClass({
  getInitialState: function() {
    return { editing: false, name: "" };
  },
  componentDidUpdate: function(prevProps, prevState) {
    if (!prevState.editing && this.state.editing) {
      React.findDOMNode(this.refs.input).select();
    }
  },
  handleSubmit: function(e) {
    e.preventDefault();
    this.handleDone();
  },
  handleNameChange: function(event) {
    this.setState({name: event.target.value});
  },
  handleEdit: function() {
    this.setState({editing: true, name: this.props.member.name});
  },
  handleRemove: function() {
    DutyActions.removeMember(this.props.memberID);
  },
  handleDone: function() {
    this.setState({editing: false});
    DutyActions.updateMemberName(this.props.memberID, this.state.name);
  },
  render: function() {
    var leftElement;
    var rightElements = [];
    if (this.state.editing) {
      leftElement = (
          <input type="text"
                 ref="input"
                 className="form-control input-group-sm"
                 placeholder="Name of RA"
                 value={this.state.name}
                 style={styles.input}
                 onChange={this.handleNameChange}>
          </input>
      );
      var deleteButton = (
        <i className="fa fa-remove fa-fw"
           style={styles.iconDelete}
           key="delete"
           onClick={this.handleRemove}>
        </i>
      );
      var doneButton = (
        <i className="fa fa-check fa-fw"
           style={styles.iconDone}
           key="done"
           onClick={this.handleDone}>
        </i>
      );
      rightElements.push(deleteButton, doneButton);
    }
    else {
      leftElement = (
        <span>
          {this.props.member.name}
        </span>
      );
      var editButton = (
        <i className="fa fa-edit fa-fw"
           style={styles.iconEdit}
           key="edit"
           onClick={this.handleEdit}>
        </i>
      );
      rightElements.push(editButton);
    }
    return (
      <ListGroupItem>
        <form className="inline-form"
              onSubmit={this.handleSubmit}>
          {leftElement}
          <span className="pull-right">
            {rightElements}
          </span>
        </form>
      </ListGroupItem>
    );
  }
});

module.exports = MemberCell;
