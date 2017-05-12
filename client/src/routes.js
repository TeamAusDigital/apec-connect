import React from 'react';
import {IndexRoute, Route} from 'react-router';
import {connect} from 'react-redux';
import actions from './state/actions';
import Home from './routes/landing-page';
import Scroll from 'react-scroll';
import GetPaidScreen from './components/getPaidScreen';
import PayScreen from './components/payScreen';
import MainSplashScreen from './components/mainSplashScreen';
import SignUpScreen from './components/signUpScreen';
import HomeScreen from './components/homeScreen';
import {persistStore} from 'redux-persist';
import Inbox from './components/inbox';
import ViewInvoice from './components/viewInvoice';

/**
 * The main application component that contains the routing configuration.
 */
@connect((state) => {
  return {dispatch: state.dispatch, participant: state.participant};
})
class App extends React.Component {

  constructor() {
    super();
    this.state = {
      rehydrated: false,
      isParticipantRefreshed: false
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

  render() {
    let {rehydrated} = this.state;

    return (
      <div>
        {
          rehydrated ?
            <div>
              <div id='body'>
                {this.props.children}
              </div>
            </div>
          : ''
        }
      </div>
    );

  }
}

const basicRoutes = (
  <Route></Route>
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
