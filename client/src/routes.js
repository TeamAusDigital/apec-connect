import React from 'react';
import { IndexRoute, Route } from 'react-router';
import { connect } from 'react-redux';
import actions from './state/actions';
import Scroll from 'react-scroll';
import GetPaidScreen from './components/getPaidScreen';
import PayScreen from './components/payScreen';
import MainSplashScreen from './components/mainSplashScreen';
import LoadingSplashScreen from './components/loadingSplashScreen';
import LandingPage from './routes/landing-page';
import SignUpScreen from './components/signUpScreen';
import HomeScreen from './components/homeScreen';
import Inbox from './components/inbox';
import ViewInvoice from './components/viewInvoice';
import ReceiptScreen from './components/receiptScreen';
import FeedbackScreen from './components/feedbackScreen';
import {persistStore} from 'redux-persist';
import MessageScreen from './components/messageScreen';
import ErrorSnackbar from './components/errorSnackbar';

/**
 * The main application component that contains the routing configuration.
 */
@connect((state) => {
  return {
    dispatch: state.dispatch,
    messages: state.messages,
    };
})
class App extends React.Component {

  constructor() {
    super();
    this.state = {
      rehydrated: false
    };
  }

  componentWillMount() {
    const store = require('state');

    // Emit store hydrated state change to make sure the store's
    // values have been loaded successfully.
    persistStore(store.default, {}, () => {
      this.setState({rehydrated: true});
    });
  }

  componentDidMount() {
    let {router} = this.props;

    // Make sure the window scroll to top on route change.
    router.listen(function() {
      Scroll.animateScroll.scrollToTop({smooth: false, duration: 0});
    });
  }

  /**
   * @see https://facebook.github.io/react/docs/react-component.html#render
   */
  render() {
    let {rehydrated} = this.state;

    return (
      <div>
        <div id='body'>
          { rehydrated ? this.props.children : <LoadingSplashScreen /> }
        </div>
        <ErrorSnackbar />
      </div>
    );
  }
}

const basicRoutes = (
  <Route></Route>
);

const routes = (
  <Route path='/' component={App}>

    <IndexRoute component={LandingPage} />
    <Route path='/' component={LandingPage} />
    <Route path='/join' component={MainSplashScreen} />
    <Route path='/home' component={HomeScreen} />
    <Route path='/getPaid' component={GetPaidScreen} />
    <Route path='/pay' component={PayScreen} />
    <Route path='/signUp' component={SignUpScreen} />
    <Route path='/inbox' component={Inbox} />
    <Route path='/viewInvoice' component={ViewInvoice} />
    <Route path='/viewReceipt' component={ReceiptScreen}/>
    <Route path='/feedback' component={FeedbackScreen}/>
    <Route path='/sendMessage' component={MessageScreen}/>


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
