import Vue from 'vue'
import Vuex from 'vuex'
import user from '@/store/modules/user'
import meeting from '@/store/modules/meeting'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    meeting
  },
  getters
})

export default store
