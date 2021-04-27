<template>
  <v-dialog
    persistent
    max-width="600px"
    v-model="isDetailCalendar"
    :close-on-content-click="false"
    offset-x
  >
    <v-card>
      <v-card-title class="pa-2">
        <span class="headline px-7 py-4"
          >&nbsp;<span class="dateTitle">{{ date }}</span></span
        >
      </v-card-title>
      <v-divider></v-divider>
      <v-card-text class="py-0">
        <template v-if="clickDateList.length == 0">
          <div class="mt-5">업무 일정이 없습니다.</div>
        </template>
        <template v-else>
          <div class="mt-5">
            <v-timeline dense>
              <template v-for="item in clickDateList">
                <v-timeline-item
                  fill-dot
                  small
                  :key="item.pid"
                  :icon="item.icon"
                  color="teal lighten-3"
                  class="mb-5"
                  @click="getSub(item.pid)"
                >
                  <v-row>
                    <v-col cols="7" @click="goProjectPage(item.prjId)">
                      <span class="detail-font">{{ item.prjTitle }}</span>
                      <v-spacer></v-spacer>
                      <v-chip
                        v-if="item.pstate == 'P'"
                        class="text-center"
                        x-small
                        label
                        color="blue"
                        text-color="white"
                        >진행</v-chip
                      >
                      <v-chip
                        v-if="item.pstate == 'W'"
                        class="text-center"
                        x-small
                        label
                        color="yellow"
                        >대기</v-chip
                      >
                      <v-chip
                        v-if="item.pstate == 'H'"
                        class="text-center"
                        x-small
                        label
                        >보류</v-chip
                      >
                      <v-chip
                        v-if="item.pstate == 'E'"
                        class="text-center"
                        x-small
                        label
                        color="red"
                        text-color="white"
                        >긴급</v-chip
                      >
                      <v-chip
                        v-if="item.pstate == 'C'"
                        class="text-center"
                        x-small
                        label
                        color="green"
                        text-color="white"
                        >완료</v-chip
                      >
                      &nbsp;&nbsp;<span class="title-font">{{
                        item.ptitle
                      }}</span>
                    </v-col>
                    <v-col class="text-right pt-5" cols="5">
                      <v-btn icon @click="getSub(item.pid)">
                        <v-icon small>mdi-arrow-right-bold-outline</v-icon>
                      </v-btn>
                    </v-col>
                  </v-row>
                  <v-spacer></v-spacer>
                </v-timeline-item>
              </template>
            </v-timeline>
          </div>
        </template>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="SET_IS_DETAIL_CALENDAR(false)"
          >닫기</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapMutations, mapState, mapActions } from "vuex";

export default {
  props: ["date"],
  data: () => ({
    dateList: "",
  }),
  created() {
    console.log("날짜는?", this.date);
  },
  computed: {
    ...mapState({
      clickDateList: "getClickDateList",
    }),
    ...mapState(["isDetailCalendar"]),
  },
  methods: {
    ...mapMutations(["SET_IS_DETAIL_CALENDAR", "SET_IS_DETAIL_SUB"]),
    ...mapActions(["FETCH_CALENDAR_EVENT"]),
    getSub(id) {
      const superId = id;
      this.FETCH_CALENDAR_EVENT(superId);
      this.SET_IS_DETAIL_SUB(true);
    },
    goProjectPage(prjId) {
      console.log("aa", prjId);
      if (confirm("해당 업무로 이동하시겠습니까??") == true) {
        //확인
        this.$router.push(`/projects/${prjId}`);
        this.SET_IS_DETAIL_CALENDAR(false);
      } else {
        //취소
        return;
      }
    },
  },
};
</script>

<style scoped>
.detail-font {
  font-size: 11px;
  color: cadetblue;
  cursor: default;
}
.user-font {
  font-size: 11px;
}
.title-font {
  font-size: 14px;
  cursor: default;
}
.user-list {
  padding-left: 15px;
}
.user-avatar {
  margin-left: -5px;
}
.dateTitle {
  /* background: #ffffff; */
  font-family: "Jeju Gothic", sans-serif;
}
</style>
