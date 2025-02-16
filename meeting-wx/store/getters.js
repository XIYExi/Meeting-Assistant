const getters = {
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,

  meetingList: state => state.meeting.meetingList,
  meetingSearchText: state => state.meeting.searchText,
  agendaList: state => state.meeting.agendaList,
}
export default getters
