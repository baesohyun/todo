<template>
  <v-card width="95%" class="mx-auto" outlined>
    <v-card-title class="text-h5">
      <v-icon left>mdi-star</v-icon>&nbsp;
      <span class="starTitle">관심 업무</span>
    </v-card-title>
    <v-divider></v-divider>
    <v-card-text class="py-0">
      <v-row>
        <v-col cols="12" md="9">
          <v-data-table
            :headers="headers"
            :items="listItem"
            :items-per-page="5"
            item-key="starId"
            height="288"
            no-data-text="관심 업무가 존재하지 않습니다."
          >
            <!-- 공개여부 아이콘 설정 -->
            <template v-slot:item.starIcon="{ item }">
              <v-btn icon color="yellow" @click="delStar(item.starId)">
                <v-icon>mdi-star</v-icon>
              </v-btn>
            </template>
            <template v-slot:item.prjTitle="{ item }">
              <span @click.prevent="goProjectPage(item.prjId)">{{ item.prjTitle }}</span>
              <!-- <router-link :to="`/projects/${item.prjId}`">{{
                item.prjTitle
              }}</router-link>-->
            </template>
            <template v-slot:item.usePublic="{ item }">
              <v-icon small v-show="item.usePublic">mdi-lock-open-variant-outline</v-icon>
              <v-icon small v-show="!item.usePublic">mdi-lock-outline</v-icon>
            </template>
            <!-- 진행상태 표시 -->
            <template v-slot:item.state="{ item }">
              <v-chip v-if="item.state == 'P'" small color="blue" text-color="white">진행</v-chip>
              <v-chip v-if="item.state == 'W'" small color="yellow">대기</v-chip>
              <v-chip v-if="item.state == 'H'" small>보류</v-chip>
              <v-chip v-if="item.state == 'E'" small color="red" text-color="white">긴급</v-chip>
              <v-chip v-if="item.state == 'C'" small color="green" text-color="white">완료</v-chip>
            </template>
          </v-data-table>
        </v-col>
        <v-col cols="12" lg="3">
          <div v-if="starredList.length == 0" class="text-center py-5">관심 업무 차트가 존재하지 않습니다.</div>
          <div v-else>
            <canvas id="starredChart" class="mx-auto mt-5" width="300" height="300"></canvas>
          </div>
        </v-col>
      </v-row>
      <v-dialog v-model="openDialog" persistent max-width="290">
        <v-card>
          <v-card-title class="subheading font-weight-bold">관심업무를 취소하시겠습니까?</v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn text small @click="openDialog = false">취소</v-btn>
            <v-btn text small @click="deleteStarred()">확인</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
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
        { text: "", value: "starIcon", align: "center", width: "5%" },
        { text: "업무 명", value: "ptitle", align: "center", width: "20%" },
        {
          text: "프로젝트",
          align: "center",
          sortable: false,
          value: "prjTitle",
          width: "25%",
        },
        { text: "기간", value: "dueDate", align: "center", width: "20%" },
        { text: "진행 상태", value: "state", align: "center", width: "20%" },
        {
          text: "공개 여부",
          value: "usePublic",
          align: "center",
          width: "10%",
        },
      ],
      listItem: [],
      //////////////////////// 차트 ////////////////////////////
      chartStateCnt: { H: 0, P: 0, C: 0, W: 0, E: 0 },
      /////////////////
      openDialog: false,
      starId: "",
    };
  },
  created() {
    this.fetchStarredTasks();
  },
  computed: {
    ...mapState({
      starredList: "starredList",
    }),
  },
  methods: {
    ...mapActions(["FETCH_STARRED_DASHBOARD", "DELETE_STARRED"]),
    fetchStarredTasks() {
      this.FETCH_STARRED_DASHBOARD().then(() => {
        console.log("====", this.starredList);
        this.listItem = [];
        for (var i = 0; i < this.starredList.length; i++) {
          this.listItem.push(this.starredList[i]);
          switch (this.starredList[i].state) {
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
        console.log("하.....", this.listItem);

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
        chartjs.createChart("starredChart", chartObj);
      }); // store -> actions
    },
    delStar(starId) {
      console.log("관심", starId);
      this.openDialog = true;
      this.starId = starId;
    },
    deleteStarred() {
      this.DELETE_STARRED(this.starId).then(() => {
        console.log("취소 완료");
        this.chartStateCnt = { H: 0, P: 0, C: 0, W: 0, E: 0 };
        this.fetchStarredTasks();
        this.openDialog = false;
      });
    },
    goProjectPage(prjId) {
      if (confirm("해당 업무로 이동하시겠습니까??") == true) {
        //확인
        this.$router.push(`/projects/${prjId}`);
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
/* .router-link-active {
  text-decoration: none;
  color:black
}
.router-link:hover {
  text-decoration: none !important;
} */
.starTitle {
  /* background: #ffffff; */
  font-family: "Jeju Gothic", sans-serif;
}
* {
  cursor: default;
}
</style>
