const state = {
  accessToken: null, // null, // 로그인처리 : 로그인 > 토큰값 저장 , 로그아웃 > 토큰값 삭제
  isShowSubMenu: false,
  activeMenu: "",
  headerTitle: "",
  appBarIcon: "",
  userInfo: {},
  userList: [],
  deptList: [],
  ///// PROJECT////////////
  projects: [],
  project: {},
  addTaskModal: false,
  taskList: [],
  taskInfo: {},
  taskSuperId: undefined,
  taskSuperPublic: true,
  lastSubSortNo: 0,
  labelList: [],
  memberList: [],
  ///////// DASHBOARD /////////
  todayList: [],
  weekList: [],
  starredList: [],
  todaySub: [],
  weekSub: [],
  ///////// CALENDAR /////////
  isAddCalendar: false,
  isDetailCalendar: false,
  isDetailSub: false,
  clickDate: "",
  getClickDateList: [],
  getsubList: [],
  getSuperTitle: "",
  getProjectTitle: "",
  calendarList: [],
  getProjects: [],
  getSuperTasks: [],
  getFilter: [],
  getChkFilterItem: "",
  calImgCode: [],
  calFilterItem: [],
  ///////// GANTT CHART /////////
  ganttSuper: [],
  ganttFilterItem: [],
  getChkGanttFilterItem: "",
  getGanttFilter: [],
  ///////// ARCHIVE /////////
  archiveSuper: [],
  archiveSub: [],
};
export default state;