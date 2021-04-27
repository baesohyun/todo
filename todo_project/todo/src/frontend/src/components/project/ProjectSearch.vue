<template>
  <v-row>
    <v-col cols="2">
      <v-card outlined height="800" class="overflow-y-auto mx-2">
        <v-text-field
          class="searchInput"
          v-model="search"
          append-icon="mdi-magnify"
          label="Search to Enter"
          single-line
          hide-details
          @keydown.enter="fatchData"
        ></v-text-field>
        <div class="mx-0">
          <v-list class="project-filter">
            <v-subheader class="indigo lighten-5">검색 옵션</v-subheader>
            <v-radio-group
              v-model="option"
              @change="fatchData"
              color="primary"
              :mandatory="false"
              class="px-3"
            >
              <v-radio label="전체" value="opa"></v-radio>
              <v-radio label="프로젝트" value="opp"></v-radio>
              <v-radio label="업무" value="opt"></v-radio>
              <v-radio label="담당자" value="opm"></v-radio>
            </v-radio-group>
          </v-list>
          <v-list class="project-filter">
            <v-subheader class="indigo lighten-5">공개 범위</v-subheader>
            <v-radio-group
              v-model="range"
              @change="fatchData"
              color="primary"
              :mandatory="false"
              class="px-3"
            >
              <v-radio label="전체" value="rga"></v-radio>
              <v-radio label="공개용" value="t"></v-radio>
              <v-radio label="개인용" value="f"></v-radio>
            </v-radio-group>
          </v-list>
        </div>
      </v-card>
    </v-col>
    <v-col cols="10">
      <v-card outlined max-height="800" class="overflow-y-auto mx-2">
        <v-subheader class="indigo lighten-5 ma-0" style="margin:8px">검색 결과</v-subheader>
        <!-- <v-card-title style="font-size:20px; font-weight:bold;">검색 결과</v-card-title> -->
        <v-card v-show="showProject" flat>
          <!-- <v-card-title style="font-size:15px; font-weight:bold;">프로젝트</v-card-title> -->
          <v-data-table
            :headers="projectHeaders"
            :items="this.projectList"
            v-model="projectSelected"
            :items-per-page="5"
            @click:row="goProjectPage"
          >
            <!-- 담당자 -->
            <template v-slot:item.members="{ item }">
              <v-tooltip bottom color="#5a5a5a" v-for="(mem, index) in item.members" :key="index">
                <template v-slot:activator="{ on, attrs }">
                  <v-avatar v-bind="attrs" v-on="on" size="32px">
                    <img class="user-avatar" :src="item.img[index]" />
                  </v-avatar>
                </template>
                <span font-color="white" class="user-font">{{ mem }}</span>
              </v-tooltip>
            </template>
            <!-- 기간 -->
            <template v-slot:item.dueDate="{ item }">{{ item.startdate }} ~ {{ item.enddate }}</template>
            <!-- 진행 상태 -->
            <template v-slot:item.state="{ item }">
              <v-chip v-if="item.state == 'P'" small color="blue" text-color="white">진행</v-chip>
              <v-chip v-if="item.state == 'W'" small color="yellow">대기</v-chip>
              <v-chip v-if="item.state == 'H'" small>보류</v-chip>
              <v-chip v-if="item.state == 'E'" small color="red" text-color="white">긴급</v-chip>
              <v-chip v-if="item.state == 'C'" small color="green" text-color="white">완료</v-chip>
            </template>
            <!-- 공개 여부 -->
            <template v-slot:item.usePublic="{ item }">
              <v-icon small v-show="item.usepublic">mdi-lock-open-variant-outline</v-icon>
              <v-icon small v-show="!item.usepublic">mdi-lock-outline</v-icon>
            </template>
          </v-data-table>
        </v-card>
        <v-card flat v-show="showTask">
          <v-data-table
            :headers="taskHeaders"
            :items="this.taskList"
            v-model="taskSelected"
            :items-per-page="itemCnt"
            @click:row="goProjectPage"
          >
            <template v-slot:item.members="{ item }">
              <v-tooltip bottom color="#5a5a5a" v-for="(mem, index) in item.members" :key="index">
                <template v-slot:activator="{ on, attrs }">
                  <v-avatar v-bind="attrs" v-on="on" size="32px">
                    <img class="user-avatar" :src="item.img[index]" />
                  </v-avatar>
                </template>
                <span font-color="white" class="user-font">{{ mem }}</span>
              </v-tooltip>
            </template>
            <!-- 기간 -->
            <template v-slot:item.dueDate="{ item }">{{ item.startdate }} ~ {{ item.enddate }}</template>
            <!-- 진행 상태 -->
            <template v-slot:item.state="{ item }">
              <v-chip v-if="item.state == 'P'" small color="blue" text-color="white">진행</v-chip>
              <v-chip v-if="item.state == 'W'" small color="yellow">대기</v-chip>
              <v-chip v-if="item.state == 'H'" small>보류</v-chip>
              <v-chip v-if="item.state == 'E'" small color="red" text-color="white">긴급</v-chip>
              <v-chip v-if="item.state == 'C'" small color="green" text-color="white">완료</v-chip>
            </template>
            <!-- 공개 여부 -->
            <template v-slot:item.usePublic="{ item }">
              <v-icon small v-show="item.usepublic">mdi-lock-open-variant-outline</v-icon>
              <v-icon small v-show="!item.usepublic">mdi-lock-outline</v-icon>
            </template>
          </v-data-table>
        </v-card>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
// import { mapActions } from 'vuex'
import * as api from "../../api/index.js";

export default {
  data: () => ({
    loading: false,
    resultFilter: ["전체", "프로젝트", "업무", "담당자"],
    publicFilter: ["전체", "공개용", "개인용"],
    item: "",
    search: "",
    projectSelected: "",
    taskSelected: "",
    projectList: [],
    taskList: [],
    option: "opa",
    range: "rga",
    showProject: true,
    showTask: true,
    itemCnt: 5,
    testList: [],
  }),
  created() {},
  computed: {
    // ...mapState({
    //   projectList: "searchProject",
    //   taskList: "taskAll"
    // })
    projectHeaders() {
      return [
        {
          text: "프로젝트명",
          align: "center",
          value: "title",
          filterable: true,
        },
        { text: "담당자", align: "center", value: "members" },
        { text: "기간", align: "center", value: "dueDate" },
        { text: "진행 상태", align: "center", value: "state" },
        {
          text: "공개 여부",
          align: "center",
          value: "usePublic",
          filter: this.rFilter,
        },
      ];
    },
    taskHeaders() {
      return [
        {
          text: "업무명",
          align: "center",
          value: "title",
          filterable: true,
        },
        {
          text: "업무경로",
          align: "center",
          value: "route",
          filterable: false,
        },
        { text: "담당자", align: "center", value: "members" },
        { text: "기간", align: "center", value: "dueDate" },
        { text: "진행 상태", align: "center", value: "state" },
        {
          text: "공개 여부",
          align: "center",
          value: "usePublic",
          filter: this.rFilter,
        },
      ];
    },
  },
  methods: {
    // ...mapActions(["FETCH_PROJECTALL","FATCH_TASKALL"]),

    fatchData() {
      if (this.search.trim() === "") {
        this.projectList = [];
        this.taskList = [];
        return;
      }
      // this.loading = true;
      // this.FETCH_PROJECTALL()
      // .finally(() => {
      //   this.loading = false;
      // })
      // this.FETCH_TASKALL().finally(() => {
      //   this.loading = false;
      // })
      const srchData = {
        search: this.search,
        option: this.option,
        range: this.range,
      };

      api.search.fetchAll(srchData).then((res) => {
        console.log("---", srchData);
        this.projectList = [];
        this.taskList = [];
        console.log("project", res.data);

        let object = {};
        res.data.forEach((item) => {
          object = JSON.parse(item);
          if (object.id.startsWith("PJ")) {
            this.projectList.push(object);
          } else {
            console.log("11111");
            if (!object.pid) {
              // 상위업무
              console.log("22222");
              object.route = object.prjtitle;
            } else {
              console.log("33333");
              object.route = object.prjtitle + " > " + object.ptitle;
            }
            this.taskList.push(object);
          }
        });
        console.log("prj", this.projectList);
        console.log("task", this.taskList);
      });

      // option
      if (this.option == "opa") {
        // 전체
        this.showProject = true;
        this.showTask = true;
        this.itemCnt = 5;
        this.projectHeaders[0].filterable = true;
        this.taskHeaders[0].filterable = true;
      } else if (this.option == "opp") {
        // 프로젝트만
        this.showProject = true;
        this.showTask = false;
        this.itemCnt = 12;
        this.projectHeaders[0].filterable = true;
        this.taskHeaders[0].filterable = true;
      } else if (this.option == "opt") {
        // 업무만
        this.showProject = false;
        this.showTask = true;
        this.itemCnt = 12;
        this.projectHeaders[0].filterable = true;
        this.taskHeaders[0].filterable = true;
      } else if (this.option == "opm") {
        // 담당자
        this.showProject = true;
        this.showTask = true;
        this.itemCnt = 5;
        this.projectHeaders[0].filterable = false;
        this.taskHeaders[0].filterable = false;
      }
    },
    goProjectPage(value) {
      if (value.prjid) {
        this.$router.push(`/projects/${value.prjid}`);
      } else {
        this.$router.push(`/projects/${value.id}`);
      }
    },
  },
};
</script>

<style>
.searchInput {
  padding: 5px;
  margin: 8px 8px 0px 8px;
}
.col-2 {
  padding-right: 0px;
}
.col-10 {
  padding-left: 0px;
}
.v-card .v-card {
  margin: 25px 25px 30px 25px;
}
.v-data-footer {
  /* display: none; */
  height: 40px;
}
.v-data-footer__select {
  display: none;
}
.v-application p {
  margin-bottom: 0px;
}
.user-avatar {
  margin-left: -5px;
}
</style>
