import auth from '../../../src/state/reducers/auth.reducers';
import { AUTH_SUCCESS, AUTH_FAILURE, NOT_AUTHENTICATED } from '../../../src/state/actions/actionTypes';

describe('Authorisation reducer', () => {

  it('should return initial state', () => {
    expect(
      auth.authentication(undefined, {type: '', payload: {}})
    ).toEqual({});
  });

  it('should log user in when authentication success and store the token', () => {
    expect(
      auth.authentication({}, {type: AUTH_SUCCESS, payload: 'TOKEN'})
    ).toEqual({ loading: false, loggedIn: true, token: 'TOKEN' });
  });

  it('should not log user in when authentication failed and supply an error message', () => {
    expect(
      auth.authentication({}, {
        type: AUTH_FAILURE,
        payload: 'Wrong username or password',
        error: true
      })
    ).toEqual({ loading: false, loggedIn: false, errorMessage: 'Wrong username or password' });
  });

  it('should turn authentication loggedIn to NOT logged in and auth token has been cleared if participant not authenticated', () => {
    expect(
      auth.authentication({}, {
        type: NOT_AUTHENTICATED
      })
    ).toEqual({});
  });
});
