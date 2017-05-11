import React from 'react';
import { IndexRoute, Route } from 'react-router';
import { connect } from 'react-redux';
import actions from './state/actions';
import Home from './routes/landing-page';
import Scroll from 'react-scroll';
import GetPaidScreen from './components/getPaidScreen';
import PayScreen from './components/payScreen';
import MainSplashScreen from './components/mainSplashScreen';
import SignUpScreen from './components/signUpScreen';
import HomeScreen from './components/homeScreen';
import Inbox from './components/inbox';
import ViewInvoice from './components/viewInvoice';
/**
 * The main application component that contains the routing configuration.
 */
@connect((state) => {
  return {
    dispatch: state.dispatch
  };
})
class App extends React.Component {

  /**
   * Loads the Vendor data on component mount.
   * The Vendor data load here is to prevent data lost upon page fresh.
   * @see https://facebook.github.io/react/docs/react-component.html#componentdidmount
   */
  componentDidMount() {
    let { router, dispatch } = this.props;

    setTimeout(function () {
      //dispatch(actions.getVendor());
    });

    // Make sure the window scroll to top on route change.
    router.listen(function () {
      Scroll.animateScroll.scrollToTop({smooth: false, duration: 0});
    });

    if (this.props.ui.alreadyUser) {
      {/** If token is valid proceed to home **/}
      this.props.router.push('/home');
    } else {
      {/** proceed to join **/}
      this.props.router.push('/join');
    };

  }

  /**
   * @see https://facebook.github.io/react/docs/react-component.html#render
   */
  render() {
    return (
      <div>
        <div id='body'>
          {this.props.children}
        </div>
      </div>
    );
  }
}

const basicRoutes = (
  <Route>
  </Route>
);

const routes = (
  <Route path='/' component={App}>

    <IndexRoute component={Home} />
    <Route path='/' component={Home} />
    <Route path='/home' component={HomeScreen} />
    <Route path='/getPaid' component={GetPaidScreen} />
    <Route path='/pay' component={PayScreen} />
    <Route path='/join' component={MainSplashScreen} />
    <Route path='/signUp' component={SignUpScreen} />
    <Route path='/inbox' component={Inbox} />
    <Route path='/viewInvoice' component={ViewInvoice} />

  </Route>
);

const combinedRoutes = (
  <Route>
    <Route>
      {routes}
    </Route>
    <Route>
      {basicRoutes}
    </Route>
  </Route>
);

export default combinedRoutes;
