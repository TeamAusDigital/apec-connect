import client from '../../../src/state/reducers/auth.reducers';
import { AUTH_RESPONSE, NOT_AUTHENTICATED } from '../../../src/state/actions/actionTypes';

describe('Authorisation reducer', () => {

  it('should return initial state', () => {
    expect(
      client.authentication(undefined, {type: '', payload: {}})
    ).toEqual({});
  });

  it('should log user in when authentication success and store the token', () => {
    expect(
      client.authentication({}, {type: AUTH_RESPONSE, payload: { loggedIn: true, message: '', token: 'TOKEN' }})
    ).toEqual({ loggedIn: true, errorMessage: '', token: 'TOKEN' });
  });

  it('should not log user in when authentication failed and supply an error message', () => {
    expect(
      client.authentication({}, {
        type: AUTH_RESPONSE,
        payload: new Error('Wrong username or password'),
        error: true
      })
    ).toEqual({ loggedIn: undefined, errorMessage: 'Wrong username or password', token: undefined });
  });

  it('should turn authentication loggedIn to NOT logged in if client not authenticated', () => {
    expect(
      client.authentication({}, {
        type: NOT_AUTHENTICATED
      })
    ).toEqual({ loggedIn: false });
  });
});
