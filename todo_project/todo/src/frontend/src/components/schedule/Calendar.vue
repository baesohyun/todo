<template>
  <v-row>
    <v-col cols="2">
      <v-card outlined height="790" class="overflow-y-auto mx-2">
        <div class="mx-0">
          <v-subheader class="indigo lighten-5">프로젝트</v-subheader>
          <v-list class="project-filter">
            <v-list-item class="px-0" v-for="item in projectFilter" :key="item.prjId">
              <v-list-item-content class="px-0">
                <v-checkbox
                  class="px-3"
                  v-model="prjSelection"
                  :value="item.prjId"
                  @change="chkFilter()"
                >
                  <template v-slot:label>
                    <span class="font-filter">{{ item.prjTitle }}</span>
                  </template>
                </v-checkbox>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-subheader class="indigo lighten-5">담당자</v-subheader>
          <v-list class="project-filter">
            <v-list-item class="px-0" v-for="item in managerFilter" :key="item.userId">
              <v-list-item-content class="px-0">
                <v-checkbox
                  class="pl-3"
                  v-model="memSelection"
                  :value="item.userId"
                  @change="chkFilter()"
                >
                  <template v-slot:label>
                    <span class="font-filter">{{ item.userName }}</span>
                  </template>
                </v-checkbox>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-subheader class="indigo lighten-5">공개 여부</v-subheader>
          <v-list class="project-filter px-0">
            <v-list-item class="px-0">
              <v-list-item-content>
                <v-radio-group v-model="publicSelection" @change="chkFilter()">
                  <v-radio class="px-5 pb-2" :value="0">
                    <template v-slot:label>
                      <span class="font-filter">전체</span>
                    </template>
                  </v-radio>
                  <v-radio class="px-5 pb-2" :value="true">
                    <template v-slot:label>
                      <span class="font-filter">공개</span>
                    </template>
                  </v-radio>
                  <v-radio class="px-5 pb-2" :value="false">
                    <template v-slot:label>
                      <span class="font-filter">개인용</span>
                    </template>
                  </v-radio>
                </v-radio-group>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </div>
      </v-card>
      <!-- 필터 저장 초기화 -->
      <div class="ma-2">
        <v-btn class="mx-0 my-0" color="blue-grey" depressed block dark @click="resetFilter()">초기화</v-btn>
      </div>
    </v-col>
    <v-col cols="10">
      <v-card class="border" flat style="border:none;">
        <v-row class="fill-height">
          <v-col>
            <v-sheet height="64">
              <v-toolbar flat color="white">
                <!-- 업무추가버튼 -->
                <v-btn
                  class="mx-3"
                  outlined
                  fab
                  dark
                  small
                  color="blue"
                  @click.prevent="SET_IS_ADD_CALENDAR(true)"
                >
                  <v-icon dark>mdi-plus</v-icon>
                </v-btn>
                <v-spacer></v-spacer>
                <!-- 오늘날짜이동 -->
                <v-btn small outlined class="mr-4" color="grey darken-2" @click="setToday">오늘</v-btn>
                <!-- < 이전 버튼 -->
                <v-btn fab text color="grey darken-2" @click="prev">
                  <v-icon small>mdi-chevron-left</v-icon>
                </v-btn>
                <!-- 몇월인지 타이틀 -->
                <v-toolbar-title v-if="$refs.calendar">
                  {{ $refs.calendar.title }}
                  <!-- > 다음 버튼 -->
                  <v-btn fab text color="grey darken-2" @click="next">
                    <v-icon small>mdi-chevron-right</v-icon>
                  </v-btn>
                </v-toolbar-title>
                <v-spacer></v-spacer>
                <!-- <v-switch v-model="google" class="ma-3">
                    <template v-slot:label>
                      <span class="my-3 font-filter">구글 연동</span>
                    </template>
                </v-switch>-->
              </v-toolbar>
            </v-sheet>
            <!-- 달력 시작 -->
            <v-sheet height="750">
              <v-calendar
                ref="calendar"
                v-model="focus"
                color="primary"
                :events="events"
                :event-color="getEventColor"
                :type="type"
                @click:event="showEvent"
                @click:date="showDate"
                @change="updateRange"
              ></v-calendar>
              <!-- 상세모달 창 시작 -->
              <detail-calendar :date="this.focus" />
              <!-- 상세모달 창 끝 -->
              <!-- 달력 끝 -->
              <!-- 업무소 모달 시작 -->
              <detail-cal-event />
              <!-- 업무소 모달 끝 -->
              <!-- 일정추가 모달 시작 -->
              <add-calendar />
              <!-- 일정추가 모달 끝 -->
            </v-sheet>
          </v-col>
        </v-row>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
import AddCalendar from "./AddCalendar.vue";
import DetailCalendar from "./DetailCalendar.vue";
import DetailCalEvent from "./DetailCalEvent.vue";

export default {
  components: {
    AddCalendar,
    DetailCalendar,
    DetailCalEvent,
  },
  data: () => ({
    google: false,
    focus: "",
    type: "month",
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    events: [],
    colors: ["#BFC9CA", "#5DADE2", "#82E0AA", "#F7DC6F", "#F1948A"],
    //////////////// Filt4er ///////////////////
    projectFilter: [],
    managerFilter: [],
    prjSelection: [],
    memSelection: [],
    publicSelection: 0,
    //////////////// Filter END ///////////////////
  }),
  created() {
    // store -> actions
    this.fetchCalendarInfo();
    this.fetchFilter();
    this.fetchChkItem();
  },
  computed: {
    // 사용할 mapstate 불러옴
    ...mapState({
      calendarList: "calendarList",
      getClickDateList: "getClickDateList",
      getFilter: "getFilter",
      getChkFilterItem: "getChkFilterItem",
      calFilterItem: "calFilterItem",
    }),
    ...mapState(["isDetailCalendar"]),
    // ...mapState(["clickDate"])
  },
  mounted() {
    this.$refs.calendar.checkChange();
  },
  methods: {
    ...mapMutations([
      "SET_IS_ADD_CALENDAR",
      "SET_IS_DETAIL_CALENDAR",
      "SET_IS_DETAIL_SUB",
      "IS_CLICK_DATE",
    ]),
    ...mapActions([
      "FETCH_CALENDAR_LIST",
      "FETCH_CALENDAR_CLICKDATE",
      "FETCH_CALENDAR_EVENT",
      "FETCH_FILTER",
      "FETCH_CHK_FILTER_ITEM",
      "RESET_CAL_FILTER",
      "ADD_CAL_FILTER_ITEM",
    ]),
    // 사용할 mapactions 등록

    getEventColor(event) {
      return event.color;
    },
    setToday() {
      // 오늘날짜로 이동
      this.focus = "";
    },
    prev() {
      // 이전 달 이동
      this.$refs.calendar.prev();
    },
    next() {
      // 다음 달 이동
      this.$refs.calendar.next();
    },
    showDate({ date }) {
      // 모달창 이벤트
      this.focus = date; // 들어온 해당 날짜
      // console.log("날짜 히얼", this.focus);
      // this.$emit("focus", this.focus);
      const clickDate = {
        clickDate: this.focus,
      };
      this.FETCH_CALENDAR_CLICKDATE(clickDate).then(() => {
        this.SET_IS_DETAIL_CALENDAR(true);
      });
      // this.IS_CLICK_DATE(this.focus);
    },
    showEvent({ event }) {
      // 해당 이벤트
      const superId = event.id;
      console.log(superId);
      this.FETCH_CALENDAR_EVENT(superId);
      this.SET_IS_DETAIL_SUB(true);
    },
    updateRange() {
      this.fetchCalendarInfo();
    },
    fetchFilter() {
      // 필터에 나올 프로젝트명이랑 담당자등을 조회
      this.FETCH_FILTER().then(() => {
        let list = this.getFilter;

        /// 프로젝트 id 중복 제거
        var prjObj = {};
        for (var i in list) {
          prjObj[list[i]["prjId"]] = list[i];
        }

        for (i in prjObj) {
          this.projectFilter.push(prjObj[i]);
        }

        /// 담당자 중복 제거
        var memObj = {};
        for (var j in list) {
          memObj[list[j]["userId"]] = list[j];
        }
        for (j in memObj) {
          this.managerFilter.push(memObj[j]);
        }
      });
    },
    fetchChkItem() {
      // 저장된 필터 값
      this.FETCH_CHK_FILTER_ITEM().then(() => {
        console.log("확이이이인", this.getChkCalFilterItem);
        var filterArr = this.getChkFilterItem.split("::");
        var prjFilter = filterArr[0];
        var prjFilterArr = prjFilter.split(",");

        var memFilter = filterArr[1];
        var memFilterArr = memFilter.split(",");

        var pulibcFilter = filterArr[2];
        var publicFilterArr = pulibcFilter.split(",");

        this.prjSelection = prjFilterArr;
        this.memSelection = memFilterArr;
        if (publicFilterArr[0] == "true") {
          this.publicSelection = true;
        } else if (publicFilterArr[0] == "false") {
          this.publicSelection = false;
        } else {
          this.publicSelection = 0;
        }
      });
    },
    async chkFilter() {
      const calData = {
        prjData: this.prjSelection,
        memData: this.memSelection,
        useData: this.publicSelection,
      };

      await this.ADD_CAL_FILTER_ITEM(calData);
      this.fetchCalendarInfo();
    },
    resetFilter() {
      this.RESET_CAL_FILTER();
      this.prjSelection = [];
      this.memSelection = [];
      this.publicSelection = 0;
      this.fetchCalendarInfo();
    },
    async fetchCalendarInfo() {
      await this.FETCH_CALENDAR_LIST().then(() => {
        const events = [];
        var calObj = {};
        var calArr = [];
        for (var i in this.calendarList) {
          calObj[this.calendarList[i]["id"]] = this.calendarList[i];
        }
        for (i in calObj) {
          calArr.push(calObj[i]);
        }
        for (i = 0; i < calArr.length; i++) {
          const taskName = calArr[i].title;

          let startDate = null;
          let endDate = null;
          if (calArr[i].startDate == "" || calArr[i].endDate == "") {
            startDate = new Date(calArr[i].regDate);
            endDate = new Date(calArr[i].regDate);
          } else {
            startDate = new Date(calArr[i].startDate);
            endDate = new Date(calArr[i].endDate);
          }
          events.push({
            id: calArr[i].id,
            name: taskName, // 타이틀
            start: startDate, // 시작일
            end: endDate, // 마감일
            color: this.colors[this.colorState(calArr[i].state)], // 색상
          });
        } // end for
        console.log(this.selectProjects);

        this.events = events;
      });
    },
    colorState(state) {
      // 상태에 따른 색상
      switch (state) {
        case "H":
          return 0;
        case "P":
          return 1;
        case "C":
          return 2;
        case "W":
          return 3;
        case "E":
          return 4;
      }
    },
  },
};
</script>

<style scoped>
.border {
  border: 1px solid #ccd1d1;
  margin-right: 30px;
}
.project-filter .v-input--selection-controls {
  margin-top: 0px;
}
.project-filter .v-list-item__content {
  padding: 0px;
}
.font-filter {
  font-size: 13px;
}
</style>
