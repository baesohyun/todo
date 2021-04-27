import Vue from 'vue';
import Vuex from 'vuex';
import state from './state';
import getters from './getters';
import mutations from './mutations';
import actions from './actions';
// import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

const store = new Vuex.Store({
	state,
	getters,
	mutations,
	actions
	// plugins: [ createPersistedState() ]
});
// 어플리케이션이 구동되었을때, 브라우저 저장소에서 token이 있는지 확인하고 세팅해줌
//(자동 로그인)
const { accessToken } = localStorage;
if (accessToken !== '') {
	console.log('토큰이 존재');
	store.commit('LOGIN', { accessToken });
	store.dispatch('LOGIN_BY_TOKEN', { accessToken });
}

export default store;
