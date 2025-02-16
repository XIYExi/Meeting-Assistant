import { getMeetingListOrderByAsc, getBeginTimeList } from '@/api/meeting/meeting'

const meeting = {
  state: {
    meetingList: [],
    searchText: '',
  },
  actions: {
    MeetingList({ commit, state }) {
        return new Promise((resolve, reject) => {
            getMeetingListOrderByAsc().then(resp => {
                const {rows} = resp;
                // console.log(rows[0])
                // this.events = rows;
                commit('SET_MEETING_LIST', rows);
                resolve();
            });
        }).catch(error => {
            reject(error)
        })
    }
  },
  mutations: {
    CHANGE_MEETING_LIST(state, payload) {
        // console.log('current list search bar： ', payload);
        state.searchText = payload;
    },
    SET_MEETING_LIST: (state, meetingList) => {
        state.meetingList = meetingList;
    }
  }
}

export default meeting;
