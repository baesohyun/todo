<template>
  <div>
    <v-container class="schedule-container" fluid tag="section">
      <v-tabs v-model="tabs" color="indigo lighten-1">
        <v-tab>캘린더</v-tab>
        <v-tab>간트차트</v-tab>
      </v-tabs>

      <v-tabs-items v-model="tabs" class="schedule-tab">
        <v-tab-item>
          <v-divider></v-divider>
          <calendar class="schedule-wrapper"></calendar>
        </v-tab-item>
        <v-tab-item>
          <v-divider></v-divider>
          <gantt-chart class="schedule-wrapper"></gantt-chart>
        </v-tab-item>
      </v-tabs-items>
    </v-container>
  </div>
</template>

<script>
import Calendar from "../components/schedule/Calendar.vue";
import GanttChart from "../components/schedule/GanttChart.vue";
import { mapMutations } from "vuex";
export default {
  components: {
    Calendar,
    GanttChart,
  },
  data() {
    return {
      tabs: null,
    };
  },
  created() {
    this.SET_HEADER_TITLE("스케줄");
    this.SET_APPBAR_ICON("mdi-calendar-month-outline");
  },
  methods: {
    ...mapMutations(["SET_HEADER_TITLE", "SET_APPBAR_ICON"]),
  },
};
</script>

<style scoped>
.schedule-container {
  /* padding: 100px 30px 30px 30px; */
  min-height: 100%;
  padding: 0px;
  /* overflow-x: scroll; */
  /* height: 100%; */
}

/* .schedule-tab {
  background: #eef2f9;
} */

/* .schedule-wrapper {
  padding: 0px 0px 15px 15px;
} */
</style>
