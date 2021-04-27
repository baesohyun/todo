<template>
  <div style="height:100%">
    <v-overlay :value="loading">
      <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
    <v-container class="project-container" fluid tag="section">
      <v-app-bar
        color="#FFFFFF"
        dense
        flat
        absolute
        class="px-0"
        style="height:10px"
        height="10px"
      >
        <!-- 칸반 보드 -->
        <!-- <v-tab>간트 차트</v-tab> -->
        <!-- <v-spacer></v-spacer> -->
        <!-- <v-btn icon>
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <v-btn icon @click="onStar">
          <v-icon>{{ active ? "mdi-star" : "mdi-star-outline" }}</v-icon>
        </v-btn> -->

        <v-progress-linear
          color="indigo lighten-2"
          height="9"
          :value="project.progressRate"
        ></v-progress-linear>
        <v-btn
          icon
          color="grey lighten-1"
          small
          @click.prevent="isOpenProjectInfo = true"
          style="position: absolute;right: 20px; top:-35px"
        >
          <v-icon>mdi-information-outline </v-icon>
        </v-btn>
      </v-app-bar>
      <v-divider></v-divider>
      <div class="ma-0 kanban-board-wrapper">
        <v-row class="mx-4 list-row">
          <draggable
            v-model="lists"
            v-bind="getDragOptions"
            class="row flex-nowrap"
          >
            <Task-parent
              v-for="(listItem, index) in lists"
              :key="index"
              :board="getBoard"
              :list="listItem"
              @update="update"
            />
            <v-card class="mr-4 pa-0 super-task-card" width="300">
              <v-card-actions>
                <v-btn text block @click.prevent="showAddTask('SUPER')">
                  <v-icon small>mdi-plus</v-icon>상위 업무 추가
                </v-btn>
              </v-card-actions>
            </v-card>
          </draggable>
          <!-- <add-super-task :openModal="addTaskModal" /> -->

          <add-super-task :openModal="addTaskModal" @update="update" />
        </v-row>
      </div>
      <router-view :projectId="project.id"></router-view>
    </v-container>

    <v-dialog v-model="isOpenProjectInfo" max-width="800" persistent>
      <v-card>
        <v-form
          ref="form"
          v-model="valid"
          @submit.prevent="onSubmit"
          lazy-validation
        >
          <v-card-title class="headline grey lighten-2 py-2" primary-title>
            <h4>프로젝트 정보</h4>
            <v-spacer></v-spacer>
            <v-btn color="grey darken-1" icon @click="onClose">
              <v-icon color="grey darken-1">mdi-close</v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text class="pb-0">
            <!-- <v-container> -->
            <v-row>
              <v-col cols="8">
                <v-row>
                  <v-col cols="12">
                    <v-text-field
                      :readonly="project.memberNo !== project.manager"
                      label="프로젝트 타이틀"
                      v-model="title"
                      :rules="titleRules"
                      required
                    ></v-text-field>
                    <!-- <v-text-field
                    v-if="!toggleTitle"
                    label="PROJECT TITLE"
                    v-model="project.title"
                    :rules="titleRules"
                    @click.prevent="editTitle"
                    readonly
                    required
                  ></v-text-field>-->
                    <!-- <v-text-field
                    v-else
                    label="PROJECT TITLE"
                    v-model="title"
                    :rules="titleRules"
                    required
                  ></v-text-field>-->
                  </v-col>
                  <v-col cols="12">
                    <!-- <v-text-field
                    v-if="!toggleDesc"
                    label="PROJECT DESCRIPTION"
                    readonly
                    @click.prevent="editDescription"
                    v-model="project.description"
                  ></v-text-field>-->
                    <v-text-field
                      :readonly="project.memberNo !== project.manager"
                      label="프로젝트 설명"
                      v-model="description"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    공개여부
                    <v-radio-group
                      required
                      :readonly="project.memberNo !== project.manager"
                      row
                      :rules="privateRules"
                      v-model="usePublic"
                    >
                      <br />
                      <v-radio label="공개" value="true"></v-radio>
                      <v-spacer></v-spacer>
                      <v-radio label="비공개" value="false"></v-radio>
                      <v-spacer></v-spacer>
                    </v-radio-group>
                  </v-col>
                  <v-col cols="12">
                    <p>기간</p>
                    <v-menu
                      v-model="startDatePicker"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      :disabled="project.memberNo !== project.manager"
                      transition="scale-transition"
                      offset-y
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="startDate"
                          label="시작일"
                          prepend-icon="mdi-calendar"
                          readonly
                          :clearable="project.memberNo === project.manager"
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="startDate"
                        no-title
                        @input="startDatePicker = false"
                      ></v-date-picker>
                    </v-menu>
                    <!-- </v-col> -->
                    <!-- <v-col cols="12" sm="6"> -->
                    <!-- <p>DUERATION</p> -->
                    <v-menu
                      v-model="endDatePicker"
                      :close-on-content-click="false"
                      :nudge-right="40"
                      :disabled="project.memberNo !== project.manager"
                      transition="scale-transition"
                      offset-y
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="endDate"
                          label="종료일"
                          prepend-icon="mdi-calendar"
                          readonly
                          :clearable="project.memberNo === project.manager"
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="endDate"
                        no-title
                        @input="endDatePicker = false"
                      ></v-date-picker>
                    </v-menu>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <p>프로젝트 커버</p>
                    <v-card
                      height="65%"
                      class="mx-auto project-card"
                      :color="getColor()"
                    >
                      <v-img
                        v-if="coverImg"
                        height="100"
                        :src="getImg()"
                      ></v-img>
                    </v-card>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <!-- <v-row> -->
                    <br />
                    <v-radio-group
                      v-model="coverColor"
                      row
                      hide-details
                      :readonly="project.memberNo !== project.manager"
                      @change="setCoverColor"
                    >
                      <v-radio
                        on-icon="mdi-check-circle"
                        off-icon="mdi-checkbox-blank-circle"
                        color="#EF9A9A"
                        value="#EF9A9A"
                        class="red-icon"
                      ></v-radio>
                      <v-radio
                        on-icon="mdi-check-circle"
                        off-icon="mdi-checkbox-blank-circle"
                        color="#FFCC80"
                        value="#FFCC80"
                        class="yellow-icon"
                      ></v-radio>
                      <v-radio
                        on-icon="mdi-check-circle"
                        off-icon="mdi-checkbox-blank-circle"
                        color="#81C784"
                        value="#81C784"
                        class="green-icon"
                      ></v-radio>
                      <v-radio
                        on-icon="mdi-check-circle"
                        off-icon="mdi-checkbox-blank-circle"
                        color="#448AFF"
                        value="#448AFF"
                        class="blue-icon"
                      ></v-radio>
                      <v-radio
                        on-icon="mdi-check-circle"
                        off-icon="mdi-checkbox-blank-circle"
                        color="#5C6BC0"
                        value="#5C6BC0"
                        class="puple-icon"
                      ></v-radio>
                      <v-radio
                        on-icon="mdi-check-circle"
                        off-icon="mdi-checkbox-blank-circle"
                        color="#546E7A"
                        value="#546E7A"
                        class="grey-icon"
                      ></v-radio>
                    </v-radio-group>
                    <!-- </v-col> -->
                    <!-- <v-col cols="12" sm="12"> -->
                    <v-file-input
                      :rules="imgRules"
                      v-model="coverImgFile"
                      accept="image/png, image/jpeg, image/bmp"
                      placeholder="커버 사진 등록"
                      prepend-icon="mdi-camera"
                      :disabled="project.memberNo !== project.manager"
                      @change="createBase64Image(coverImgFile)"
                    ></v-file-input>
                    <!-- </v-row> -->
                  </v-col>
                </v-row>
              </v-col>
              <v-col cols="4">
                <p>관리자</p>
                <v-list-item>
                  <v-list-item-avatar
                    v-if="getMember(project.manager).imgCode"
                    size="32"
                  >
                    <img :src="getMember(project.manager).imgCode" />
                  </v-list-item-avatar>
                  <v-list-item-avatar
                    v-else
                    size="32"
                    class="user-avatars"
                    color="grey"
                  >
                    <v-icon fab dark>mdi-account</v-icon>
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title
                      v-html="getMember(project.manager).name"
                    ></v-list-item-title>
                    <!-- <v-list-item-subtitle v-html="getDate(this.taskInfo.task.regDate)"></v-list-item-subtitle> -->
                  </v-list-item-content>
                </v-list-item>
                <!-- <v-avatar size="32" color="grey" class="mr-2">
                <template v-if="getMember(project.manager).imgCode">
                  <img :src="getMember(project.manager).imgCode" />
                </template>
                <template v-else>
                  <v-icon fab dark>mdi-plus</v-icon>
                </template>
              </v-avatar>-->

                <p>
                  참여 중인 멤버
                  <span class="pl-4">{{ exitsMemberList.length }}명</span>
                </p>
                <div>
                  <v-btn
                    color="rgb(88, 110, 150)"
                    dark
                    block
                    depressed
                    v-if="project.memberNo === project.manager"
                    @click.prevent="isOpenSearch = true"
                  >
                    <v-icon left>mdi-account-plus</v-icon> 멤버 추가
                  </v-btn>
                  <user-search
                    :openModal="isOpenSearch"
                    @close="isOpenSearch = false"
                    @addMember="addMember"
                  />
                  <div
                    v-if="exitsMemberList !== []"
                    max-height="100%"
                    style="height:480px"
                    class="overflow-y-auto mt-3"
                  >
                    <v-list>
                      <v-list-item
                        v-for="(mem, index) in exitsMemberList"
                        :key="mem.memberNo"
                      >
                        <v-list-item-avatar>
                          <v-avatar v-if="mem.imgCode">
                            <img :src="mem.imgCode" />
                          </v-avatar>
                          <v-avatar v-else color="grey">
                            <v-icon fab dark>mdi-account</v-icon>
                          </v-avatar>
                          <!-- <v-avatar color="grey" size="56" class="white--text">{{ mem.name }}</v-avatar> -->
                        </v-list-item-avatar>
                        <v-list-item-content>
                          <v-list-item-title>{{ mem.name }}</v-list-item-title>
                        </v-list-item-content>
                        <v-list-item-action
                          v-if="project.memberNo === project.manager"
                        >
                          <v-btn icon @click.prevent="deleteMember(index)">
                            <v-icon small color="grey lighten-1"
                              >mdi-close</v-icon
                            >
                          </v-btn>
                        </v-list-item-action>
                      </v-list-item>
                      <v-list-item
                        v-for="(mem, index) in newMemberList"
                        :key="mem.memberNo"
                      >
                        <v-list-item-avatar>
                          <v-avatar v-if="mem.imgCode">
                            <img :src="mem.imgCode" />
                          </v-avatar>
                          <v-avatar v-else color="grey">
                            <v-icon fab dark>mdi-account</v-icon>
                          </v-avatar>
                          <!-- <v-avatar color="grey" size="56" class="white--text">{{ mem.name }}</v-avatar> -->
                        </v-list-item-avatar>
                        <v-list-item-content>
                          <v-list-item-title>{{ mem.name }}</v-list-item-title>
                        </v-list-item-content>
                        <v-list-item-action
                          v-if="project.memberNo === project.manager"
                        >
                          <v-btn icon @click.prevent="deleteNewMember(index)">
                            <v-icon small color="grey lighten-1"
                              >mdi-close</v-icon
                            >
                          </v-btn>
                        </v-list-item-action>
                      </v-list-item>
                    </v-list>
                    <!-- <v-tooltip v-for="member in members" :key="member.id" bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-avatar v-if="member.imgCode" size="56" class="user-avatars">
                        <img :src="member.imgCode" v-bind="attrs" v-on="on" />
                      </v-avatar>
                      <v-avatar v-else size="56" class="user-avatars" color="grey">
                        <v-icon fab dark v-bind="attrs" v-on="on">mdi-account</v-icon>
                      </v-avatar>
                    </template>
                    <span>{{ member.name }}</span>
                  </v-tooltip>-->
                  </div>
                </div>
              </v-col>
            </v-row>
            <!-- </v-container> -->
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="grey darken-1" text @click="onClose">취소</v-btn>
            <v-btn
              v-if="project.memberNo === project.manager"
              color="blue darken-1"
              text
              type="submit"
              >저장</v-btn
            >
          </v-card-actions>
        </v-form>
      </v-card>
    </v-dialog>
    <!-- <project-info
      :openModal="isOpenProjectInfo"
      @close="isOpenProjectInfo = false"
    /> -->
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
import Draggable from "vuedraggable";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { eventBus } from "../main.js";

import TaskParent from "../components/project/TaskParent";
import AddSuperTask from "../components/project/AddTaskParent";
import UserSearch from "../components/user/UserSearchModal.vue";
// import ProjectInfo from "../components/project/ProjectInfo.vue";
// import AddSubTask from "../components/project/AddSubTask";
//
export default {
  components: {
    TaskParent,
    AddSuperTask,
    // AddSubTask,
    Draggable,
    UserSearch,
    // ProjectInfo,
    // TaskList,
  },
  data() {
    return {
      tabs: null,
      pid: 0,
      loading: false,
      selection: 1,
      active: false,
      isOpenProjectInfo: false,
      isOpenSearch: false,
      title: "",
      coverColor: "", //this.setCoverColor,
      description: "",
      usePublic: undefined,
      startDate: "", //new Date().toISOString().substr(0, 10),
      endDate: "",
      startDatePicker: false,
      endDatePicker: false,
      coverImg: undefined,
      coverImgFile: undefined,
      imgRules: [
        (value) =>
          !value ||
          value.size < 2000000 ||
          "Avatar size should be less than 2 MB!",
      ],
      titleRules: [
        (v) => !!v || "title is required",
        (v) =>
          (v && v.length <= 100) || "title must be less than 100 characters",
      ],
      privateRules: [(v) => !!v || "private is required"],
      valid: true,
      members: [],
      backupImg: "",
      exitsMemberList: [],
      removeMemberList: [],
      newMemberList: [],
    };
  },
  watch: {
    $route: "fetchData",
  },
  created() {
    console.log("## projectBoard : created ## ------------------");
    console.log("----------------------");
    console.log(this.$router);
    this.loading = true;
    console.log(this.$route.params.pid);
    this.fetchData().then(() => {
      console.log(">> 패치데이터 완료");
      this.loading = false;
      this.SET_HEADER_TITLE(this.project.title);
      this.SET_APPBAR_ICON("mdi-newspaper-variant-multiple-outline");
      ///////////////////PROJECT INFO SETTING///////////////////////////////////////////
      this.title = this.project.title;
      this.description = this.project.description;
      this.usePublic = this.project.usePublic ? "true" : "false";
      this.projectCover = this.project.imgNo;
      if (this.project.imgNo[0] == "#") this.coverColor = this.project.imgNo;
      else this.coverImg = this.project.imgNo;
      this.startDate = this.project.startDate.trim();
      this.endDate = this.project.endDate.trim();
      this.exitsMemberList = Object.assign([], this.memberList);
      //////////////////////////////////////////////////////////////
      console.log("## Socket connect...");
      let socket = new SockJS("http://localhost:9000/ws-stomp"); // new SockJS("/ws-stomp");
      this.ws = Stomp.over(socket);
      console.dir(this.ws);
      this.connect();
    });
    eventBus.$on("updateByBus", () => {
      console.log("이벤트버스를 통한 updateProject!");
      this.sendMessage();
    });
    // this.connect();
  },
  destroyed() {
    console.log("destroyed///!");
    this.SET_PROJECT({});
    this.SET_TASK_LIST([]);
    this.SET_LABEL_LIST([]);
    this.SET_MEMBER_LIST([]);
  },
  computed: {
    ...mapState({
      project: "project",
      taskList: "taskList",
      addSubTaskModal: "addSubTaskModal",
      addTaskModal: "addTaskModal",
      memberList: "memberList",
    }),
    getDragOptions() {
      return {
        animation: "200",
        ghostClass: "ghost",
        handle: ".task-list-header",
        group: "kanban-board-lists",
      };
    },
    getBoard() {
      console.log("getBoard 실행..");
      return this.project;
    },
    projectCoverComp() {
      console.log("comput...");
      let temp = {};
      if (this.projectCover[0] == "#") temp.coverColor = this.projectCover;
      //this.setProjectInfo.coverColor = this.project.imgNo;
      else temp.coverImg = this.projectCover;

      return temp;
    },
    lists: {
      get() {
        return this.taskList;
      },
      async set(value) {
        console.log("lists.set 실행!");
        console.log(value);
        await this.reorderTaskLists({
          boardId: this.param,
          lists: value,
        });
      },
    },
  },
  methods: {
    ...mapMutations([
      "SET_HEADER_TITLE",
      "SET_HEADER_TITLE",
      "SET_ADD_TASK_MODAL",
      "SET_PROJECT",
      "SET_TASK_LIST",
      "SET_LABEL_LIST",
      "SET_MEMBER_LIST",
      "SET_APPBAR_ICON",
    ]),
    ...mapActions(["FETCH_PROJECT", "UPDATE_PROJECT"]),
    onClose() {
      this.formClear();
      this.isOpenProjectInfo = false;
    },
    getMember(no) {
      // 멤버 no에 해당하는 멤버정보를 가져오는 메소드
      var mb = this.memberList.find((item) => {
        return item.memberNo == no;
      });
      return mb;
    },
    addMember(members) {
      console.log("추가된 멤버", members.length);
      console.dir(members);
      console.dir("기존 멤버", this.exitsMemberList);
      for (var i in this.exitsMemberList) {
        // console.log("---기존멤버:: ", this.exitsMemberList[i].userId);
        var idx = members.findIndex((item) => {
          // console.log("------비교할멤버::", item.id);
          return item.id == this.exitsMemberList[i].userId;
        });
        if (idx !== -1) {
          console.log("중복 제거할 멤버가 있음!: " + idx, members[idx]);
          console.log("members", members);
          members.splice(idx, 1);
        }
        // console.log("결과 : ", members);
        //this.newMemberList.push(lb);
        //this.labels += this.taskLabel[i].labelNo + ":";
      }
      // console.dir(members);
      // console.log(this.newMemberList);
      this.newMemberList = members;
      console.log("-----newMemberList결과---------");
      console.dir(this.newMemberList);
    },
    deleteMember(index) {
      console.log("??");
      if (this.exitsMemberList[index].memberNo === this.project.manager) {
        alert("관리자는 삭제할 수 없습니다.");
        return;
      }
      // let temp = Object.assign([], this.setProjectInfo.memberList);
      this.removeMemberList.push(this.exitsMemberList[index]);
      console.log(this.removeMemberList);
      this.exitsMemberList.splice(index, 1);
      // console.log(this.setProjectInfo.memberList);
    },
    deleteNewMember(index) {
      // console.log("??");
      // let temp = Object.assign([], this.setProjectInfo.memberList);
      // this.removeMemberList.push(this.newMemberList[index]);
      // console.log(this.removeMemberList);
      this.newMemberList.splice(index, 1);
      // console.log(this.setProjectInfo.memberList);
    },
    getImg() {
      console.log("#getImg...");
      // if (this.project.imgNo[0] == "#") return "";
      // // console.log(this.project.imgNo);
      // return this.project.imgNo; //"https://cdn.vuetifyjs.com/images/cards/house.jpg";
      // if (this.setProjectInfo.coverColor) "";
      // console.log(this.project.imgNo);
      return this.coverImg;
    },
    getColor() {
      //return this.projectCoverComp.coverColor;
      return this.coverColor;
    },
    setCoverColor() {
      console.log("#setCoverColor");
      this.coverImg = undefined;
      // console.log(this.projectCoverComp);
      // this.projectCoverComp.coverImg = undefined;
      // if (this.project.imgNo[0] == "#") this.coverColor = this.project.imgNo;
      // else this.coverImg = this.project.imgNo;
      // this.project.imgNo = this.coverColor;
      this.projectCover = this.projectCoverComp.coverColor;
    },
    createBase64Image(fileObject) {
      console.log("file object", fileObject);
      if (fileObject !== undefined) {
        console.log("111");
        const reader = new FileReader();
        // if (this.project.imgNo[0] == "#") this.backupColor = this.project.imgNo;
        // else this.backupImg = this.project.imgNo;
        this.backupImg = this.project.imgNo;

        reader.onload = (e) => {
          console.log("222");
          this.image = e.target.result;
          // this.backupImg = this.project.imgNo;
          this.coverImg = e.target.result;
          // console.log("image", this.image);
        };
        console.log("333");
        //console.log(this.image);
        //this.setProjectInfo.coverImg = this.image;
        reader.readAsDataURL(fileObject);
      } else {
        console.log("사진지우기");
        // console.log(this.backupImg);
        if (this.backupImg[0] === "#") {
          this.coverImg = undefined;
          this.coverColor = this.backupImg;
        } else {
          this.coverImg = this.backupImg;
        }
        // this.coverImg = this.backupImg;
        // this.setProjectInfo.coverImg = undefined;
      }
    },

    onSubmit() {
      // Create Project

      console.log("UPDATE Project");
      if (this.validate()) {
        let formData = new FormData();
        formData.append("id", this.$route.params.pid);
        formData.append("title", this.title);
        formData.append("description", this.description);
        formData.append("usePublic", this.usePublic);
        formData.append("startDate", this.startDate);
        formData.append("endDate", this.endDate);
        if (this.coverImg !== undefined) {
          console.log("커버 이미지 있음! ------------");
          console.log(this.coverImg);
          formData.append("imgNo", this.coverImg);
        } else {
          formData.append("imgNo", this.coverColor);
        }
        var newMems = this.newMemberList.map(function(o) {
          return o.id;
        });
        var delMems = this.removeMemberList.map(function(o) {
          return o.userId;
        });
        console.log("추가할 멤버 : ");
        console.dir(newMems);
        console.log("삭제할 멤버 : ");
        console.dir(delMems);
        // var mems = [];
        // for (var a in this.members) {
        //   if (this.userInfo.id !== this.members[a].id)
        //     mems.push(this.members[a].id);
        // }
        formData.append("newMems", newMems);
        formData.append("delMems", delMems);

        this.UPDATE_PROJECT(formData).then(() => {
          this.SET_HEADER_TITLE(this.project.title);
          console.log("--# UPDATE PROJECT 완료");
          console.dir(this.project);
          this.isOpenProjectInfo = false;
          this.newMemberList = [];
          this.removeMemberList = [];
          this.backupImg = "";
          // this.onClose(); //원래값으로 돌려주는작업!!
        });
        // this.$emit("close");
      }
    },
    connect() {
      console.log("## 소켓 연결 시작 ## -------------------------");
      console.log("memberNo:" + this.project.memberNo);
      const stomp = this.ws;
      const pid = this.$route.params.pid;
      const project = this.project;
      const update = this.fetchData;
      stomp.connect(
        {},
        function(frame) {
          console.log("소켓연결 성공!!-----------------------");
          console.log(frame);
          stomp.subscribe(`/sub/project/room/${pid}`, function(message) {
            console.log("## STOMP 수신 결과 ## --------------------------");
            console.log(message.body, project.memberNo);
            console.log(message.body != project.memberNo + "");
            console.log("-----------------------------");
            if (message.body !== project.memberNo + "") {
              console.log(" >> 다른사람이 업데이트함!");
              update();
            }
            // var recv = JSON.parse(message.body);
            //this.recvMessage(recv);
            console.log("test.", message.body);
            // console.log(JSON.stringify({ name: pid }));
          });
          // console.log("???");
          // stomp.send(
          //   "/pub/ws/update",
          //   JSON.stringify({ name: pid, sender: project.memberNo }),
          //   {}
          // );
        },
        function(error) {
          console.log("소켓 연결실패!------------------------", error);
        }
      );
      // this.stompClient.connect({},frame =>{
      //   this.
      // })
    },
    sendMessage: function() {
      console.log("## 서버로 메세지 송신! ## :", this.project.memberNo);
      this.ws.send(
        "/pub/ws/update",
        JSON.stringify({
          name: this.$route.params.pid,
          sender: this.project.memberNo,
        }),
        {}
      );
      // this.message = "";
    },
    update: function() {
      console.log("udpate!");
      this.sendMessage();
    },
    fetchData() {
      return this.FETCH_PROJECT(this.$route.params.pid);
    },
    // reserve() {
    //   this.loading = true;

    //   //setTimeout(() => (this.loading = false), 2000);
    // },
    validate() {
      return this.$refs.form.validate();
    },
    onStar() {
      this.active = !this.active;
      //this.sendMessage();
    },
    reorderTaskLists() {
      console.log("??reorderTaskList 실행..");
    },
    formClear() {
      this.title = this.project.title;
      this.description = this.project.description;
      this.usePublic = this.project.usePublic ? "true" : "false";
      this.projectCover = this.project.imgNo;
      if (this.project.imgNo[0] == "#") this.coverColor = this.project.imgNo;
      else this.coverImg = this.project.imgNo;
      this.startDate = this.project.startDate.trim();
      this.endDate = this.project.endDate.trim();
      this.exitsMemberList = Object.assign([], this.memberList);
      this.newMemberList = [];
      this.removeMemberList = [];
      this.backupImg = "";
    },
    showAddTask(taskType) {
      if (this.project.memberNo === -1) {
        alert("참여 멤버가 아닙니다 !");
        return "";
      }
      // console.log("testestsetset");
      // console.log(sortNo);
      if (taskType === "SUPER") {
        console.log("SUPER TASK ADD..");
        this.SET_ADD_TASK_MODAL(true);
        // this.SET_SUPER_TASK_ID(taskId);
        // this.SET_LAST_SUB_SORT_NO(sortNo);
      }
    },
  },
};
</script>

<style scoped>
.project-container {
  /* padding: 100px 30px 30px 30px; */
  background: #e6eaf0;
  min-height: 100%;
  padding: 0px;
  overflow-x: auto;
  /* height: 100%; */
}
.kanban-board-wrapper {
  display: flex;
  /* align-items: flex-start; */
  flex-direction: column;
  padding-top: 20px;
}
.flex-nowrap {
  position: relative;
  /* height: 100%; */
}
/* .list-row {
  overflow-x: scroll;
  height: 100%;
  flex-direction: column;
  flex-wrap: nowrap !important;
} */

.list-container {
  height: 100%;
  display: flex;
  /* align-items: flex-start; */
  flex-direction: column;
}

.super-task-card {
  background: #f6f8fc;
  height: fit-content;
  margin: 10px;
  padding: 10px;
  width: 335px;
  position: relative;
  border-radius: 5px;
  -webkit-box-shadow: 0px 0px 4px 0px rgba(134, 134, 162, 0.21);
  -moz-box-shadow: 0px 0px 4px 0px rgba(134, 134, 162, 0.21);
  box-shadow: 0px 0px 4px 0px rgba(134, 134, 162, 0.21);
}
.add-task-card-form .col {
  padding: 10px;
}
</style>
<style>
.v-radio .v-icon {
  margin: 0px;
  font-size: 32px;
}
.red-icon .v-icon {
  color: #ef9a9a;
}
.yellow-icon .v-icon {
  color: #ffcc80;
}
.green-icon .v-icon {
  color: #81c784;
}
.blue-icon .v-icon {
  color: #448aff;
}
.puple-icon .v-icon {
  color: #5c6bc0;
}
.grey-icon .v-icon {
  color: #546e7a;
}
section.project-container > header > div,
.v-toolbar__extension {
  height: 10px;
  padding: 0px !important;
}
</style>
