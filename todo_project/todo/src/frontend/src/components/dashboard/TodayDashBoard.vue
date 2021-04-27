<template>
  <v-card width="95%" class="mx-auto" outlined>
    <v-card-title class="text-h5">
      <v-icon left>mdi-calendar-today</v-icon>&nbsp;
      <span class="todayTitle">오늘의 업무</span>
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text class="py-0">
      <v-row>
        <v-col cols="12" lg="9">
          <v-data-table
            :headers="headers"
            :items="listItem"
            :items-per-page="5"
            :single-expand="singleExpand"
            :expanded="expanded"
            show-expand
            item-key="pid"
            height="288"
            @update:expanded="getSub"
            no-data-text="오늘의 상위업무가 존재하지 않습니다."
            @click:row="goProjectPage"
          >
            <template v-slot:item.prjTitle="{ item }">{{
              item.prjTitle
            }}</template>
            <!-- 공개여부 아이콘 설정 -->
            <template v-slot:item.usePublic="{ item }">
              <v-icon small v-show="item.usePublic"
                >mdi-lock-open-variant-outline</v-icon
              >
              <v-icon small v-show="!item.usePublic">mdi-lock-outline</v-icon>
            </template>
            <!-- 진행상태 표시 -->
            <template v-slot:item.state="{ item }">
              <v-chip
                v-if="item.state == 'P'"
                small
                color="blue"
                text-color="white"
                >진행</v-chip
              >
              <v-chip v-if="item.state == 'W'" small color="yellow"
                >대기</v-chip
              >
              <v-chip v-if="item.state == 'H'" small>보류</v-chip>
              <v-chip
                v-if="item.state == 'E'"
                small
                color="red"
                text-color="white"
                >긴급</v-chip
              >
              <v-chip
                v-if="item.state == 'C'"
                small
                color="green"
                text-color="white"
                >완료</v-chip
              >
            </template>

            <!-- 업무 대 관련한 업무 소 출력 -->
            <template v-slot:expanded-item="{ headers }">
              <td :colspan="headers.length" class="px-0">
                <v-simple-table>
                  <tbody v-if="todaySub.length == 0">
                    <tr style="background-color:#F5F5F5">
                      <td colspan="todaySub.length" class="text-center">
                        오늘의 하위업무가 존재하지 않습니다.
                      </td>
                    </tr>
                  </tbody>
                  <tbody v-else>
                    <tr
                      v-for="sub in todaySub"
                      :key="sub.cid"
                      style="background-color:#F5F5F5"
                    >
                      <td class="text-center" width="20%"></td>
                      <td class="text-center" width="20%">
                        <span class="sub">{{ sub.ctitle }}</span>
                      </td>
                      <td class="text-center sub" width="20%">
                        <span class="sub"
                          >{{ sub.startDate }} ~ {{ sub.endDate }}</span
                        >
                      </td>
                      <td
                        v-if="sub.state == 'P'"
                        class="text-center"
                        width="20%"
                      >
                        <v-chip x-small color="blue" text-color="white"
                          >진행</v-chip
                        >
                      </td>
                      <td
                        v-if="sub.state == 'W'"
                        class="text-center"
                        width="20%"
                      >
                        <v-chip x-small color="yellow">대기</v-chip>
                      </td>
                      <td
                        v-if="sub.state == 'H'"
                        class="text-center"
                        width="20%"
                      >
                        <v-chip x-small>보류</v-chip>
                      </td>
                      <td
                        v-if="sub.state == 'E'"
                        class="text-center"
                        width="20%"
                      >
                        <v-chip x-small color="red" text-color="white"
                          >긴급</v-chip
                        >
                      </td>
                      <td
                        v-if="sub.state == 'C'"
                        class="text-center"
                        width="20%"
                      >
                        <v-chip x-small color="green" text-color="white"
                          >완료</v-chip
                        >
                      </td>
                      <td
                        v-show="sub.usePublic"
                        class="text-center"
                        width="10%"
                      >
                        <v-icon small>mdi-lock-open-variant-outline</v-icon>
                      </td>
                      <td
                        v-show="!sub.usePublic"
                        class="text-center"
                        width="10%"
                      >
                        <v-icon small>mdi-lock-outline</v-icon>
                      </td>
                      <td class="text-center" width="10%"></td>
                    </tr>
                  </tbody>
                </v-simple-table>
              </td>
            </template>
          </v-data-table>
          <!-- <v-tooltip bottom color="#5a5a5a">
                <template v-slot:activator="{ on, attrs }">
                  <td class="text-center" style="font-size:12px" v-if="list.managerCount == 1">
                    <v-icon v-bind="attrs" v-on="on">mdi-account</v-icon>
                    &nbsp;({{ list.managerCount }}명)
                  </td>
                  <td class="text-center" style="font-size:12px" v-if="list.managerCount > 1">
                    <v-icon v-bind="attrs" v-on="on">mdi-account-supervisor</v-icon>
                    &nbsp;({{ list.managerCount }}명)
                  </td>
                </template>
                <span font-color="white">{{list.managerName}}</span>
              </v-tooltip>
              <td class="text-center" v-if="list.managerCount == 0">
                <v-icon>mdi-account-remove</v-icon>
              </td> 
          -->
        </v-col>
        <!-- <v-divider vertical></v-divider> -->
        <v-col cols="12" lg="3">
          <!-- <p class="text-center mx-3 my-3">TODAY CHART</p> -->
          <div v-if="todayList.length == 0">
            <p class="text-center">오늘의 업무 차트가 존재하지 않습니다.</p>
          </div>
          <div v-else>
            <canvas
              id="todayChart"
              class="mx-auto mt-5"
              width="300"
              height="300"
            ></canvas>
          </div>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { chartjs } from "../../utils/todoChart.js";

export default {
  data() {
    return {
      expanded: [],
      singleExpand: true,
      headers: [
        {
          text: "프로젝트",
          align: "center",
          sortable: false,
          value: "prjTitle",
          width: "20%",
        },
        { text: "업무 명", value: "ptitle", align: "center", width: "20%" },
        { text: "기간", value: "dueDate", align: "center", width: "20%" },
        { text: "진행 상태", value: "state", align: "center", width: "20%" },
        {
          text: "공개 여부",
          value: "usePublic",
          align: "center",
          width: "10%",
        },
        { text: "", value: "data-table-expand", width: "10%" },
      ],
      listItem: [],
      //////////////////////// 차트 ////////////////////////////
      chartStateCnt: { H: 0, P: 0, C: 0, W: 0, E: 0 },
    };
  },
  created() {
    this.FETCH_TODAY_DASHBOARD().then(() => {
      console.log("---", this.todayList);
      for (var i = 0; i < this.todayList.length; i++) {
        this.listItem.push(this.todayList[i]);
        switch (this.todayList[i].state) {
          case "H":
            this.chartStateCnt.H++;
            break;
          case "P":
            this.chartStateCnt.P++;
            break;
          case "C":
            this.chartStateCnt.C++;
            break;
          case "W":
            this.chartStateCnt.W++;
            break;
          case "E":
            this.chartStateCnt.E++;
            break;
        }
      } // end for

      const chartObj = {
        type: "doughnut",
        data: {
          datasets: [
            {
              data: [
                this.chartStateCnt.H,
                this.chartStateCnt.P,
                this.chartStateCnt.C,
                this.chartStateCnt.W,
                this.chartStateCnt.E,
              ],
              backgroundColor: [
                "#BFC9CA",
                "#5DADE2",
                "#82E0AA",
                "#F7DC6F",
                "#F1948A",
              ],
              borderAlign: "left",
            },
          ],
          // These labels appear in the legend and in the tooltips when hovering different arcs
          labels: ["보류", "진행", "완료", "대기", "긴급"],
        },
        options: {
          legend: {
            position: "right",
            verticalAlign: "right",
          },
          responsive: false,
        },
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
              },
            },
          ],
        },
      };
      chartjs.createChart("todayChart", chartObj);
    }); // store -> actions
  },
  computed: {
    ...mapState({
      todayList: "todayList",
      todaySub: "todaySub",
    }),
  },
  methods: {
    ...mapActions(["FETCH_TODAY_DASHBOARD", "FETCH_TODAYSUB_DASHBOARD"]),
    getSub(id) {
      if ("undefined" !== typeof id && 0 < id.length) {
        let superId = id[0].pid;
        this.FETCH_TODAYSUB_DASHBOARD(superId);
      }
    },
    goProjectPage(value) {
      if (confirm("해당 업무로 이동하시겠습니까??") == true) {
        //확인
        this.$router.push(`/projects/${value.prjId}`);
      } else {
        //취소
        return;
      }
    },
  },
};
</script>

<style scoped>
.sub {
  font-size: 12px;
}
.todayTitle {
  /* background: #ffffff; */
  font-family: "Jeju Gothic", sans-serif;
}
* {
  cursor: default;
}
</style>
