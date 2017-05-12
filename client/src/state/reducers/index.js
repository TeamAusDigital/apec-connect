import authentication from './auth.reducers';
import ui from './ui.reducers';
import participant from './participant.reducers';

module.exports = {
  ...ui,
  ...authentication,
  ...participant
};
