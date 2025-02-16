import { getMeetingListOrderByAsc, getMeetingAgendaList } from '@/api/meeting/meeting'

const meeting = {
  state: {
    meetingList: [],
    agendaList: [],
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
    },
    AgendaList({commit, state}, meetingId) {
      return new Promise((resolve, reject) => {
        getMeetingAgendaList(meetingId).then(resp => {
            const {data} = resp;
            // console.log('dd', data)
            commit('SET_AGENDA_LIST', data);
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
    },
    SET_AGENDA_LIST: (state, agendaList) => {
      state.agendaList = agendaList;
    }
  }
}

export default meeting;
