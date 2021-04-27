<template>
  <v-card class="mr-4 super-task-card pt-1" width="300">
    <v-card-title class="px-0">
      <v-avatar size="20" class="mr-2" :color="stateColor">
        <v-icon v-if="!list.superTask.usePublic" small color="white"
          >mdi-lock</v-icon
        >
      </v-avatar>
      <!-- <v-btn fab x-small depressed class="mr-2" :color="stateColor">
        <v-icon v-if="!list.superTask.usePublic" small color="white"
          >mdi-lock</v-icon
        >
      </v-btn> -->
      <span class="task-list-header">{{ list.superTask.title }}</span>
      <!-- <span class="grey--text pl-3 font-weight-regular">{{list.subTaskList.length}}</span> -->
      <v-spacer></v-spacer>
      <v-btn icon @click="show = !show" small>
        <v-icon>{{ show ? "mdi-chevron-up" : "mdi-chevron-down" }}</v-icon>
      </v-btn>
      <!-- <v-btn icon>
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>-->
      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn v-bind="attrs" v-on="on" small icon>
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>
        <v-list dense class="py-0">
          <v-subheader class="text-center">업무 액션</v-subheader>
          <v-divider></v-divider>
          <v-list-item class="px-2" @click="showDetailSuperTask()">
            <v-list-item-icon class="mr-2">
              <v-icon small>mdi-card-text-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="pr-3">
              <v-list-item-title>업무 상세보기</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- <v-list-item class="px-2">
            <v-list-item-icon class="mr-2">
              <v-icon small>mdi-plus</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="pr-3">
              <v-list-item-title>관심업무 등록</v-list-item-title>
            </v-list-item-content>
          </v-list-item> -->
          <v-list-item class="px-2" @click="sendToArchive">
            <v-list-item-icon class="mr-2">
              <v-icon small left>mdi-archive-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-content class="pr-3">
              <v-list-item-title>아카이브 보내기</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-card-title>
    <!-- <v-progress-linear color="indigo lighten-1" height="8" value="30"></v-progress-linear> -->
    <v-expand-transition>
      <div v-show="show">
        <!-- <v-divider></v-divider> -->
        <v-card-text class="expand-card-text pa-3">
          <!-- <router-link
            :to="`/projects/${board.id}/super/task/${list.superTask.taskId}`"
          > -->
          <!-- <v-row align="center" class="mx-0"> -->
          <!-- <v-rating :value="4.5" color="amber" dense half-increments readonly size="14"></v-rating> -->
          <div>
            <v-avatar v-if="resistrant.imgCode === -1" size="24">
              <v-icon fab>mdi-account-off</v-icon>
            </v-avatar>
            <v-avatar v-else-if="resistrant.imgCode" size="24">
              <img :src="resistrant.imgCode" />
            </v-avatar>
            <v-avatar v-else size="24">
              <v-icon fab dark>mdi-account</v-icon>
            </v-avatar>
            <span style="color:#7d7d7d; font-size:13px">
              {{ resistrant.name }}</span
            >
            <!-- <span class="grey--text">{{ list.superTask.regDate }}</span> -->
          </div>
          <v-list-item
            three-line
            class="px-0"
            v-if="list.superTask.description"
          >
            <v-list-item-content>
              <v-list-item-subtitle>{{
                list.superTask.description
              }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <!-- <div class="grey--text">RegDate:{{ list.superTask.regDate }}</div> -->
          <!-- </v-row> -->

          <!-- <div class="my-4 subtitle-1">$ • Italian, Cafe</div> -->

          <!-- <div class="my-4">{{ list.superTask.description }}</div> -->
          <div v-if="list.superTask.labels" class="pt-2">
            <v-chip
              v-for="label in setLabel(list.superTask.labels)"
              :key="label.lableNo"
              filter
              small
              dark
              class="mr-1"
              :color="label.labelColor"
              >{{ label.labelName }}</v-chip
            >
          </div>
          <div
            class="my-2"
            v-if="list.superTask.startDate || list.superTask.endDate"
          >
            <v-chip label small color="#cacaca">
              <v-icon left small>mdi-clock-outline</v-icon>
              {{ list.superTask.startDate }} - {{ list.superTask.endDate }}
            </v-chip>
          </div>
          <p class="my-auto">
            <v-icon small>mdi-paperclip</v-icon>
            {{ list.superTask.fileCnt }}
            <v-icon small>mdi-comment-text-outline</v-icon>
            {{ list.superTask.commentCnt }}
          </p>
          <!-- </router-link> -->
        </v-card-text>
      </div>
    </v-expand-transition>
    <!-- <v-divider></v-divider> -->
    <v-card-text class="list-content px-0 py-2">
      <ul class="task-list">
        <draggable v-model="items" v-bind="dragOptions" class="list-group">
          <transition-group
            type="transition"
            :name="!drag ? 'flip-list' : null"
          >
            <task-child
              v-for="item in items"
              :item="item"
              :list="list"
              :board="board"
              :key="item.taskId"
            />
          </transition-group>
        </draggable>
      </ul>
    </v-card-text>
    <!-- <v-divider></v-divider> -->
    <v-card-actions class="pa-0">
      <v-btn
        text
        block
        @click.prevent="
          showAddTask(
            list.superTask.taskId,
            list.superTask.usePublic,
            lastSubTaskPos
          )
        "
      >
        <v-icon small>mdi-plus</v-icon>하위 업무 추가
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import Draggable from "vuedraggable";
import { mapMutations, mapState, mapActions } from "vuex";

export default {
  props: ["board", "list"],
  data() {
    return {
      selection: 0,
      show: false,
      drag: false,
      active: false,
      resistrant: undefined,
    };
  },
  created() {
    // console.log("created..");

    this.resistrant = this.getMember(this.list.superTask.memberNo);
    if (this.resistrant === undefined) {
      this.resistrant = { name: "존재하지 않는 사용자", imgCode: -1 };
    }
    // console.log("등록자 : ", this.resistrant);
    // console.log(this.resistrant);
    // console.log(",,,");
  },
  mounted() {
    // console.log("mounted..");
    // console.dir(this.items);
  },
  beforeUpdate() {
    console.log("beforeUpdate..");
  },
  updated() {
    console.log("Update..");
  },
  beforeDestroy() {
    console.log("beforeDestroy..");
  },
  components: {
    Draggable,
    TaskChild: () => import("./TaskChild.vue"),
  },
  computed: {
    ...mapState({
      labelList: "labelList",
      memberList: "memberList",
      project: "project",
    }),
    dragOptions() {
      return {
        animation: "400",
        ghostClass: "ghost",
        group: "kanban-board-list-items",
      };
    },
    lastSubTaskPos() {
      const lastSubTask = this.list.subTaskList[
        this.list.subTaskList.length - 1
      ];
      let sortNo = 65535;
      if (lastSubTask) sortNo = lastSubTask.sortNo + sortNo;
      return sortNo;
    },
    stateColor() {
      let color = "";
      switch (this.list.superTask.state) {
        case "H":
          color = "#e0e0e0";
          break;
        case "P":
          color = "#2196f3";
          break;
        case "C":
          color = "#4caf50";
          break;
        case "E":
          color = "#ff5252";
          break;
        case "W":
          color = "#fb8c00";
          break;
      }
      return color;
    },
    items: {
      get() {
        // console.log("get items..");
        // console.log(this.list.subTaskList);
        // for (var i = 0; i < this.list.subTaskList.length; i++) {
        //   console.log(
        //     this.list.subTaskList[i].taskId,
        //     this.list.subTaskList[i].sortNo
        //   );
        // }
        return this.list.subTaskList;
      },
      set(reorderedListItems) {
        console.log("1. set items..");
        if (this.project.memberNo === -1) {
          alert("참여 멤버가 아닙니다 !");
          return;
        }
        // console.log(reorderedListItems);
        for (var i = 0; i < reorderedListItems.length; i++) {
          console.log(
            reorderedListItems[i].taskId,
            reorderedListItems[i].sortNo
          );
        }

        const payload = {
          boardId: this.board.id,
          memberNo: this.board.memberNo,
          taskSuperId: this.list.superTask.taskId,
          items: reorderedListItems,
        };
        // for (var i in reorderedListItems) {
        //   payload.items.push({
        //     taskSuperId: reorderedListItems[i].taskSuperId,
        //     taskId: reorderedListItems[i].taskId,
        //     sortNo: reorderedListItems[i].sortNo
        //   });
        // }
        this.reorderTaskListItems(payload);
      },
    },
  },
  methods: {
    ...mapMutations([
      "SET_ADD_TASK_MODAL",
      "SET_SUPER_TASK_ID",
      "SET_LAST_SUB_SORT_NO",
      "SET_SUPER_TASK_PUBLIC",
    ]),
    ...mapActions(["REORDER_TASK", "SEND_TO_ARCHIVE_SUPER"]),
    showDetailSuperTask() {
      this.show = false;
      this.$router.push(
        `/projects/${this.board.id}/super/task/${this.list.superTask.taskId}`
      );
    },
    sendToArchive() {
      this.show = false;
      if (this.project.memberNo === -1) {
        alert("참여 멤버가 아닙니다 !");
        return "";
      }
      this.SEND_TO_ARCHIVE_SUPER(this.list.superTask.taskId).then(() => {
        alert("보관함으로 이동했습니다.");
        this.$emit("update");
        //this.onClose();
      });
    },
    showAddSubTask(taskId, sortNo) {
      console.log(sortNo);
      this.SET_ADD_SUB_TASK_MODAL(true);
      this.SET_SUPER_TASK_ID(taskId);
      this.SET_LAST_SUB_SORT_NO(sortNo);
    },
    reorderTaskListItems(payload) {
      console.log("2. reorderTaskItems...");
      // alert("2.reorder");
      // console.log("this.list", this.list);
      var targetTask = {};
      var items = payload.items;
      // console.log("items", items);
      // console.log("--------------------------");
      targetTask.taskSuperId = payload.taskSuperId;
      targetTask.projectId = payload.boardId; // 재호출용 데이터
      targetTask.memberNo = payload.memberNo; // 재호출용 데이터

      for (var j = 0; j < items.length - 1; j++) {
        console.log(items[j].taskId, items[j].sortNo);
      }
      if (this.list.subTaskList.length === 0) {
        console.log("업무대 빵");
        targetTask.taskId = items[0].taskId;
        targetTask.sortNo = 65535;
        return this.REORDER_TASK(targetTask).then(() => {
          this.$emit("update");
        });
      }
      for (var i = 0; i < items.length; i++) {
        // console.log(i, "items[i]", item[i].sortNo);
        if (i == 0 && items[i].sortNo >= items[i + 1].sortNo) {
          console.log("카드가 맨~~위로 올라온경우", i);
          targetTask.taskId = items[i].taskId;
          targetTask.sortNo = items[i + 1].sortNo / 2;
          return this.REORDER_TASK(targetTask).then(() => {
            this.$emit("update");
          });
        } else if (
          i > 0 &&
          i < items.length - 1 &&
          items[i].sortNo <= items[i - 1].sortNo
        ) {
          //items[i].sortNo >= items[i + 1].sortNo) // && items[i].sortNo > items[i - 1].sortNo
          console.log("앞에 카드보다 작은경우!!", i);
          targetTask.taskId = items[i].taskId;
          targetTask.sortNo = (items[i - 1].sortNo + items[i + 1].sortNo) / 2;
          return this.REORDER_TASK(targetTask).then(() => {
            this.$emit("update");
          });
        } else if (
          i > 0 &&
          i < items.length - 1 &&
          items[i].sortNo >= items[i + 1].sortNo &&
          items[i - 1].sortNo < items[i + 1].sortNo
        ) {
          //items[i].sortNo >= items[i + 1].sortNo) // && items[i].sortNo > items[i - 1].sortNo
          console.log("뒤에 카드보다 큰 경우!", i);
          targetTask.taskId = items[i].taskId;
          targetTask.sortNo = (items[i - 1].sortNo + items[i + 1].sortNo) / 2;
          return this.REORDER_TASK(targetTask).then(() => {
            this.$emit("update");
          });
        } else if (
          i == items.length - 1 &&
          items[i].sortNo < items[i - 1].sortNo
        ) {
          console.log("카드가 맨~~아래로 내려온경우", i);
          targetTask.taskId = items[i].taskId;
          targetTask.sortNo = items[i - 1].sortNo * 2;
          return this.REORDER_TASK(targetTask).then(() => {
            this.$emit("update");
          });
        } else if (items[i].taskSuperId != payload.taskSuperId) {
          console.log("다른리스트로 옮겼지만 순서엔 문제없음", i);
          // console.log("item의 슈퍼id: ", items[i].taskSuperId);
          // console.log("현재 리스트 id: ", payload.taskSuperId);
          targetTask.taskId = items[i].taskId;
          targetTask.sortNo = items[i].sortNo;
          targetTask.taskSuperId = payload.taskSuperId;
          return this.REORDER_TASK(targetTask).then(() => {
            this.$emit("update");
          });
        } else {
          console.log("무슨경우 ?  ", i);
          // return;
        }
      }
      console.log("../end");
      // this.list.subTaskList = payload.items;
      // console.log("////reorderTaskItems-----");
      // this.REORDER_TASK(targetTask);
    },
    // getLabelNo(labelString) {
    //   // console.log(labelString);
    //   // console.log(this.list.superTask);
    //   if (labelString !== null) {
    //     var labelsNo = labelString.split(":");
    //     const labels = [];
    //     labelsNo = labelsNo.slice(0, labelsNo.length - 1);
    //     for (var no in labelsNo) {
    //       var lb = this.labelList.find(item => {
    //         return item.labelNo == labelsNo[no];
    //       });
    //       labels.push(lb);
    //     }
    //     // console.log("---label no---");
    //     return labels;
    //   }
    // },
    showAddTask(taskSuperId, taskSuperPublic, sortNo) {
      console.log("SUB TASK ADD..");
      console.log(sortNo);
      // this.SET_ADD_TASK_MODAL(true);
      if (this.project.memberNo === -1) {
        alert("참여 멤버가 아닙니다 !");
      } else {
        this.SET_ADD_TASK_MODAL(true);
        this.SET_SUPER_TASK_ID(taskSuperId);
        this.SET_SUPER_TASK_PUBLIC(taskSuperPublic);
        this.SET_LAST_SUB_SORT_NO(sortNo);
        this.SET_LAST_SUB_SORT_NO(sortNo);
      }
    },
    setLabel(labelString) {
      const labelArr = JSON.parse(labelString); // labelArr Json String으로 변환할것 -> DB에 저장하는값
      var taskLabel = []; // 라벨로 보여줄 객체뽑아올곳
      for (var i in labelArr) {
        var lb = this.labelList.find((item) => {
          return item.labelNo == labelArr[i];
        });
        taskLabel.push(lb);
        //this.taskLabel[i] = this.labelList[labelArr[i]];
        //this.labels += this.taskLabel[i].labelNo + ":";
      }
      return taskLabel;
    },
    getMember(no) {
      var mb = this.memberList.find((item) => {
        return item.memberNo == no;
      });
      return mb;
    },
    // getImgCode(item) {
    //   return "data:image;base64," + item.imgCode;
    // }
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
  color: #797070;
}
ul,
li {
  list-style: none;
  padding: 0;
}
.task-list {
  padding: 0px;
}
.task-list-header {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 60%;
}
.super-task-card {
  background-color: #dcdcdc;
}
.super-task-card .v-card__title {
  font-size: 1.2em;
  color: #797070;
  /* background-color: #685083; */
  padding: 5px;
}
.list-group {
  min-height: 20px;
  display: flex;
  flex-direction: column !important;
}
.expand-card-text {
  background-color: #9fa4b93b;
}
.list-content {
  /* max-height: 100%; */
  overflow: auto;
}
</style>
