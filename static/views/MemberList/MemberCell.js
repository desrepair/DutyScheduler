'use strict';

var React = require('react');
var ReactBootstrap = require('react-bootstrap');
var ListGroupItem = ReactBootstrap.ListGroupItem;
var DutyActions = require('../../actions/DutyActions');
var m = require('../../utils/Utilities').m;

var styles = {
  cell: {
    cursor: 'pointer',
  },
  input: {
    width: 200,
    display: 'inline',
  },
  icon: {
    fontSize: '18',
  },
  iconSelected: {
    color: 'white',
  },
  iconDelete: {
    color: 'red',
  },
  iconDone: {
    color: 'green',
  },
  iconEdit: {
    color: 'gray',
  },
}

var MemberCell = React.createClass({
  getInitialState: function() {
    return { editing: false, name: "" };
  },
  componentDidUpdate: function(prevProps, prevState) {
    if (!prevState.editing && this.state.editing) {
      React.findDOMNode(this.refs["input-" + this.props.memberID]).select();
    }
  },
  handleCellClick: function() {
    DutyActions.toggleSelectMember(this.props.memberID);
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
                 ref={"input-" + this.props.memberID}
                 className="form-control input-group-sm"
                 placeholder="Name of RA"
                 value={this.state.name}
                 style={styles.input}
                 onChange={this.handleNameChange}>
          </input>
      );
      var deleteButton = (
        <i className="fa fa-remove fa-fw"
           style={m(styles.icon, styles.iconDelete)}
           key="delete"
           onClick={this.handleRemove}>
        </i>
      );
      var doneButton = (
        <i className="fa fa-check fa-fw"
           style={m(styles.icon, styles.iconDone)}
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
           style={m(styles.icon, styles.iconEdit,
                    this.props.selected && styles.iconSelected)}
           key="edit"
           onClick={this.handleEdit}>
        </i>
      );
      rightElements.push(editButton);
    }
    return (
      <ListGroupItem className={this.props.selected ? "active" : ""}
                     style={styles.cell}
                     onClick={this.handleCellClick}>
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
