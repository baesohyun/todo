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
              <v-list-item-content>
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
          <v-list class="project-filter">
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
      <div style="width:98%; max-height:700px;">
        <template v-if="tasks.length == 0">일정이 없습니다.</template>
        <template v-else>
          <gantt-elastic :tasks="tasks" :options="options"></gantt-elastic>
        </template>
      </div>
    </v-col>
  </v-row>
</template>

<script>
import { mapState, mapActions } from "vuex";
import GanttElastic from "gantt-elastic";

export default {
  components: {
    ganttElastic: GanttElastic,
    // ganttElasticFooter: { template: `<span>your footer</span>` },
  },
  data() {
    let tasks = [];
    let router = this.$router;
    let options = {
      xScale: -10,
      maxRows: 100,
      maxHeight: 750,
      row: {
        height: 30,
      },
      calendar: {
        hour: {
          display: false,
        },
      },
      chart: {
        progress: {
          bar: false,
        },
        expander: {
          display: true,
        },
      },
      taskList: {
        expander: {
          straight: false,
        },
        columns: [
          {
            id: 1,
            label: "업무", // 업무
            value: "title", // tasks안에 있는 label에 넣겟다
            width: 200,
            expander: true,
            html: true,
            events: {
              click({ data }) {
                if (confirm("해당 업무로 이동하시겠습니까??") == true) {
                  //확인
                  router.push(`/projects/${data.projectId}`);
                } else {
                  //취소
                  return;
                }

                // router.push(`/projects/${data.projectId}`);
              },
            },
          },
          {
            id: 2,
            label: "담당자", // 담당자
            value: "managerName", // tasks안에 있는 user를 넣겠다
            width: 120,
            html: true,
            // events: {
            //   click() {
            //     alert("담당자!");
            //   },
            // },
          },
          {
            id: 3,
            label: "상태",
            value: "state",
            width: 50,
          },
          // {
          //   id: 4,
          //   label: "진행율",
          //   value: "progress",
          //   width: 35,
          //   style: {
          //     "task-list-header-label": {
          //       "text-align": "center",
          //       width: "100%",
          //     },
          //     "task-list-item-value-container": {
          //       "text-align": "center",
          //       width: "100%",
          //     },
          //   },
          // },
        ],
      },
    };
    //////////////// Filter ///////////////////
    let projectFilter = [];
    let managerFilter = [];
    let prjSelection = [];
    let memSelection = [];
    let publicSelection = 0;
    //////////////// Filter END ///////////////////
    return {
      tasks: tasks,
      options: options,
      projectFilter: projectFilter,
      managerFilter: managerFilter,
      prjSelection: prjSelection,
      memSelection: memSelection,
      publicSelection: publicSelection,
    };
  },
  created() {
    // store -> actions
    this.fetchGanttInfo();
    this.fetchFilter();
    this.fetchChkItem();
  },
  computed: {
    // 사용할 mapstate 불러옴
    ...mapState({
      getGanttFilter: "getGanttFilter",
      ganttSuper: "ganttSuper",
      getChkGanttFilterItem: "getChkGanttFilterItem",
      ganntFilterItem: "ganntFilterItem",
    }),
  },
  methods: {
    ...mapActions([
      "FETCH_GANTT_FILTER",
      "FETCH_CHK_GANTT_FILTER_ITEM",
      "RESET_GANTT_FILTER",
      "FETCH_GANTT_SUPER",
      "ADD_GANTT_FILTER_ITEM",
    ]),
    async fetchGanttInfo() {
      await this.FETCH_GANTT_SUPER().then(() => {
        this.tasks = this.ganttSuper;
        console.log(this.tasks);
      });
    },
    ///////////////////// 필터 //////////////////////
    fetchFilter() {
      //ok
      // 필터 조회
      this.FETCH_GANTT_FILTER().then(() => {
        let list = this.getGanttFilter;

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
      // 저장된 필터 값 체크되도록 보이기
      this.FETCH_CHK_GANTT_FILTER_ITEM().then(() => {
        // ok
        // console.log("확이이이인", this.getChkGanttFilterItem);
        var filterArr = this.getChkGanttFilterItem.split("::");
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
      // 체크할때마다 디비에 저장 ok
      const calData = {
        prjData: this.prjSelection,
        memData: this.memSelection,
        useData: this.publicSelection,
      };

      await this.ADD_GANTT_FILTER_ITEM(calData); // 체크시, 간트필터 반영
      this.fetchGanttInfo(); // 다시 조회
    },
    resetFilter() {
      // 초기화 됬을 때 다시 조회
      this.RESET_GANTT_FILTER();
      this.prjSelection = [];
      this.memSelection = [];
      this.publicSelection = 0;
      this.fetchGanttInfo();
    },
    ////////////////////////////////////////////////////////////
  },
};
</script>

<style scoped>
.border {
  border: 1px solid #ccd1d1;
  /* height: 850px; */
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
.gantt-elastic:hover {
  cursor: default;
}
</style>
