const getters = {
	//vue의 computed속성과 비슷한 점.. isAuth를 함수로 호출하는 것이 아니라, 변수처럼 사용가능!
	isAuth(state) {
		// return !! state.token // token값의 유무로 boolean값 전달
		return !!state.accessToken;
	}
};
export default getters;
