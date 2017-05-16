import React from 'react';
import Snackbar from 'material-ui/Snackbar';
import { withRouter } from 'react-router';
import { connect } from 'react-redux';
import actions from 'state/actions';

/***

This shows errors raised via the 'showError' action as a snackbar at the bottom of the screen.

***/

const snackbarStyle = {
  height: 'auto',
  lineHeight: 1.5,
  paddingTop: '1em'
};

@connect((state) => {
  return {
    dispatch: state.dispatch,
    ui: state.ui
  };
})
export default class ViewInvoice extends React.Component {

  hideError() {
    this.props.dispatch(actions.hideError());
  }

  render() {
    return (
      <Snackbar
        className='error-bar'
        open={this.props.ui.showError}
        message={this.props.ui.errorMessage}
        bodyStyle={snackbarStyle}
        onRequestClose={::this.hideError}
        onActionTouchTap={::this.hideError}
        action='dismiss'/>
    );
  }
}


