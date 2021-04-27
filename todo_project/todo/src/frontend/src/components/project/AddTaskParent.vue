<template>
  <v-dialog v-model="openModal" max-width="800" persistent>
    <v-card class="add-task-card-form">
      <v-form
        ref="form"
        v-model="valid"
        @submit.prevent="onSubmit"
        lazy-validation
      >
        <v-card-title class="headline grey lighten-2 py-2" primary-title>
          <h4 v-if="taskSuperId">하위 업무 추가</h4>
          <h4 v-else>상위 업무 추가</h4>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" icon @click="onClose">
            <v-icon color="grey darken-1">mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="py-0 px-3">
          <!-- <v-container> -->
          <v-row style="max-height:750px" class="overflow-y-auto">
            <v-col cols="9" class="formFieldCol">
              <v-col cols="12">
                <v-text-field
                  label="업무 타이틀*"
                  v-model="title"
                  :rules="titleRules"
                  required
                  dense
                  auto-grow
                  outlined
                  hide-details
                ></v-text-field>
              </v-col>
              <v-row class="px-5 py-0">
                <v-col cols="12" class="pb-3">
                  <!-- <p><v-icon left small>mdi-lock</v-icon>공개 여부*</p> -->
                  <v-radio-group
                    v-model="usePublic"
                    required
                    hide-details
                    @change="changePublic"
                    row
                    :rules="privateRules"
                  >
                    <span class="additional-title">
                      <v-icon small left>mdi-lock-outline</v-icon>공개여부
                    </span>
                    <v-radio
                      :disabled="!taskSuperPublic"
                      label="공개"
                      value="true"
                      class="mr-5"
                    ></v-radio>
                    <v-radio
                      :disabled="!taskSuperPublic"
                      label="비공개"
                      value="false"
                    ></v-radio>
                    <v-spacer></v-spacer>
                  </v-radio-group>
                </v-col>
                <v-col cols="12" class="py-3" v-if="managers.length">
                  <!-- <p><v-icon left small>mdi-account</v-icon>담당자</p> -->
                  <span class="additional-title">
                    <v-icon small left>mdi-account-outline</v-icon>담당자
                  </span>
                  <div
                    v-if="managers.length"
                    class="pl-4"
                    style="display:inline-block"
                  >
                    <v-tooltip
                      v-for="member in managers"
                      :key="member.id"
                      bottom
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-avatar
                          v-if="member.imgCode"
                          size="32"
                          class="user-avatars"
                        >
                          <img :src="member.imgCode" v-bind="attrs" v-on="on" />
                        </v-avatar>
                        <v-avatar
                          v-else
                          size="32"
                          class="user-avatars"
                          color="grey"
                        >
                          <v-icon fab dark v-bind="attrs" v-on="on"
                            >mdi-account</v-icon
                          >
                        </v-avatar>
                      </template>
                      <span>{{ member.name }}</span>
                    </v-tooltip>
                    <span class="pl-4">{{ managers.length }}명</span>
                  </div>
                </v-col>
                <v-col cols="12" class="py-3" v-if="endDate || startDate">
                  <span class="additional-title">
                    <v-icon small left>mdi-calendar-month-outline</v-icon>기한
                  </span>
                  <v-chip label small color="#cacaca">
                    <v-icon small left>mdi-clock-outline</v-icon>
                    {{ startDate }} ~ {{ endDate }}
                  </v-chip>
                </v-col>
                <v-col cols="12" class="py-3" v-if="taskState >= 0">
                  <span class="additional-title">
                    <v-icon small left>mdi-rotate-left</v-icon>상태
                  </span>
                  <v-chip filter :color="state[taskState].color">
                    {{ state[taskState].name }}
                  </v-chip>
                </v-col>
                <v-col cosl="12" class="py-3" v-if="taskLabel.length">
                  <span class="additional-title">
                    <v-icon small left>mdi-label-outline</v-icon>태그
                  </span>
                  <v-chip
                    v-for="(label, i) in taskLabel"
                    :key="i"
                    :color="label.labelColor"
                    dark
                    small
                    label
                    class="ma-1"
                    >{{ label.labelName }}</v-chip
                  >
                </v-col>
              </v-row>
              <v-col cols="12">
                <v-textarea
                  label="내용"
                  v-model="description"
                  auto-grow
                  dense
                  outlined
                ></v-textarea>
              </v-col>
              <v-col cols="12">
                <v-file-input
                  chips
                  multiple
                  label="파일 추가"
                  v-model="attachFiles"
                ></v-file-input>
              </v-col>
              <v-col cols="12" v-if="existCheckLists.length" class="pt-3">
                <v-slide-x-transition group>
                  <v-card
                    flat
                    class="mx-3 mb-2"
                    v-for="(checkList, index) in existCheckLists"
                    :key="index"
                  >
                    <v-card-title class="px-0">
                      <v-icon>mdi-checkbox-marked-outline</v-icon
                      ><span>{{ checkList.title }}</span>
                      <v-spacer></v-spacer>
                      <v-btn icon small @click="showNewCheckItem(index)"
                        ><v-icon small>mdi-plus</v-icon></v-btn
                      >
                      <v-btn icon small @click="deleteCheckList(index)"
                        ><v-icon small>mdi-trash-can-outline</v-icon></v-btn
                      >
                    </v-card-title>
                    <v-progress-linear
                      v-model="checkList.progressRate"
                      color="indigo lighten-24"
                    ></v-progress-linear>
                    <v-card-text class="px-0">
                      <v-text-field
                        v-show="showNewCheckItems[index]"
                        label="NEW CHECK ITEM"
                        v-model="newCheckItem"
                        required
                        dense
                        auto-grow
                        outlined
                        hide-details
                      >
                        <v-icon slot="prepend">mdi-plus</v-icon>
                        <v-icon
                          v-if="newCheckItem.trim()"
                          slot="append"
                          @click="addNewCheckItem(index)"
                          color="green"
                          >mdi-check</v-icon
                        >
                      </v-text-field>
                      <v-slide-x-transition group>
                        <div
                          v-for="(item, i) in getCheckListItems(
                            checkList.checkListItems
                          )"
                          :key="i"
                          class="px-0"
                        >
                          <v-btn
                            disabled
                            @click="check(item.item_no, checkList.listNo)"
                            icon
                          >
                            <v-icon
                              v-if="item.is_checked"
                              color="indigo lighten-1"
                              >mdi-checkbox-marked</v-icon
                            >
                            <v-icon v-else>mdi-checkbox-blank-outline</v-icon>
                          </v-btn>
                          <p style="display:inline-block">{{ item.title }}</p>
                          <v-btn
                            icon
                            small
                            style="float:right"
                            @click="deleteCheckItem(i, index)"
                          >
                            <v-icon color="grey lighten-1" small
                              >mdi-window-close</v-icon
                            >
                          </v-btn>
                        </div>
                      </v-slide-x-transition>
                      <!-- <pre>{{ checkList }}</pre> -->
                    </v-card-text>
                  </v-card>
                </v-slide-x-transition>
              </v-col>
            </v-col>
            <v-col cols="3" class="createTaskside pa-0">
              <v-subheader>추가 옵션</v-subheader>
              <v-list-item>
                <v-btn
                  block
                  depressed
                  @click.prevent="isOpenAddMember = true"
                  :disabled="usePublic == 'false' || !taskSuperPublic"
                >
                  <v-icon left>mdi-account-plus</v-icon>담당자
                </v-btn>
                <add-member
                  :openModal="isOpenAddMember"
                  @close="isOpenAddMember = false"
                  @addMember="addMember"
                />
              </v-list-item>
              <v-list-item>
                <v-menu offset-y :close-on-content-click="false">
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn v-bind="attrs" v-on="on" block depressed>
                      <v-icon left>mdi-rotate-left</v-icon>상태
                    </v-btn>
                  </template>
                  <v-card class="my-chip-group">
                    <v-card-title>TASK STATE</v-card-title>
                    <v-card-text>
                      <v-chip-group v-model="taskState" column>
                        <v-chip
                          v-for="(st, i) in state"
                          :key="i"
                          filter
                          label
                          :color="st.color"
                          >{{ st.name }}</v-chip
                        >
                      </v-chip-group>
                    </v-card-text>
                  </v-card>
                </v-menu>
              </v-list-item>
              <v-list-item>
                <label-menu :pid="this.project.id" @addLabel="addLabel" />
              </v-list-item>
              <v-list-item>
                <date-menu
                  @addStartDate="addStartDate"
                  @addEndDate="addEndDate"
                />
              </v-list-item>
              <v-list-item>
                <v-menu
                  offset-x
                  :close-on-content-click="false"
                  top
                  v-model="newCheckList"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn v-bind="attrs" v-on="on" block depressed>
                      <v-icon left>mdi-format-list-bulleted</v-icon>체크리스트
                    </v-btn>
                  </template>
                  <v-card class="my-chip-group">
                    <v-card-title class="px-4 pb-0">
                      체크리스트 추가
                      <!-- ADD LABELS {{this.labels}} -->
                    </v-card-title>
                    <v-card-text>
                      <v-form
                        @submit.prevent="onCheckListSubmit"
                        lazy-validation
                      >
                        <v-text-field
                          label="체크리스트 이름"
                          v-model="checkListName"
                          :counter="20"
                          required
                        ></v-text-field>
                        <v-btn type="submit" block depressed>
                          <v-icon left>mdi-plus</v-icon>생성
                        </v-btn>
                      </v-form>
                    </v-card-text>
                  </v-card>
                </v-menu>
              </v-list-item>
              <!-- <v-subheader>ACTIONS</v-subheader>
              <v-list-item>
                <v-btn block depressed>
                  <v-icon left>mdi-eye</v-icon>WATCH
                </v-btn>
              </v-list-item>
              <v-list-item>
                <v-btn block depressed>
                  <v-icon left>mdi-archive-outline</v-icon>ARCHIVE
                </v-btn>
              </v-list-item>-->
            </v-col>
          </v-row>
          <!-- </v-container> -->
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="darken-1" text @click="onClose">취소</v-btn>
          <v-btn color="blue darken-1" text type="submit">생성</v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
import LabelMenu from "./LabelMenu.vue";
import DateMenu from "./DateMenu.vue";
import AddMember from "./AddMember.vue";
import { eventBus } from "../../main.js";

export default {
  props: ["openModal"],
  components: {
    LabelMenu,
    DateMenu,
    AddMember,
  },
  computed: {
    ...mapState({
      project: "project",
      labelList: "labelList",
      taskSuperId: "taskSuperId",
      lastSubSortNo: "lastSubSortNo",
      taskSuperPublic: "taskSuperPublic",
      // user 멤버 no가져오기..
    }),
    // getTaskLabel() {
    //   return this.taskLabel;
    // }
  },
  data: () => ({
    clear: false,
    dialog: false,
    isOpenAddMember: false,
    title: "",
    description: "",
    usePublic: "true",
    attachFiles: [],
    startDate: "", //new Date().toISOString().substr(0, 10),
    endDate: "",
    dates: [],
    startDatePicker: false,
    endDatePicker: false,
    // taskState: undefined,
    taskState: -1,
    state: [
      { name: "보류", color: "defualt", type: "H" },
      { name: "진행", color: "primary", type: "P" },
      { name: "완료", color: "success", type: "C" },
      { name: "긴급", color: "error", type: "E" },
      { name: "대기", color: "warning", type: "W" },
    ],
    // taskStateName: ["보류", "진행", "완료", "긴급", "대기"],
    // taskStateColor: ["defualt", "primary", "success", "error", "warning"],
    taskLabel: [],
    labels: "",
    imgRules: [
      (value) =>
        !value ||
        value.size < 2000000 ||
        "Avatar size should be less than 2 MB!",
    ],
    menu: false,
    titleRules: [
      (v) => !!v || "title is required",
      (v) => (v && v.length <= 100) || "title must be less than 100 characters",
    ],
    descRules: [
      (v) =>
        (v && v.length <= 100) ||
        "description must be less than 100 characters",
    ],
    privateRules: [(v) => !!v || "private is required"],
    valid: true,
    managers: [],
    existCheckLists: [],
    checkListName: "",
    newCheckList: false,
    showNewCheckItems: [],
    newCheckItem: "",
  }),

  methods: {
    ...mapActions([
      "ADD_SUPER_TASK",
      "ADD_SUB_TASK",
      "ADD_CHECK_LIST",
      "ADD_NEW_CHECK_ITEM",
      "FETCH_CHECK_LISTS",
      "SET_CHECK_ITEM",
      "DELETE_CHECK_ITEM",
      "DELETE_CHECK_LIST",
      "DELETE_FILE",
    ]),
    ...mapMutations([
      "SET_ADD_TASK_MODAL",
      "SET_SUPER_TASK_ID",
      "SET_LAST_SUB_SORT_NO",
      "SET_SUPER_TASK_PUBLIC",
    ]),
    changePublic() {
      console.log("changePublic");
      this.managers = [];
    },
    onSubmit() {
      // Create Project
      console.log("..ADD TASK..");
      console.log("taskSuperId", this.taskSuperId);
      if (this.validate()) {
        let formData = new FormData();
        console.log("-----test-----");
        if (this.taskSuperPublic == false) this.usePublic = false;
        formData.append("projectId", this.project.id);
        formData.append("title", this.title);
        formData.append("description", this.description);
        formData.append("usePublic", this.usePublic);
        formData.append("memberNo", this.project.memberNo);
        formData.append("labels", this.labels);
        formData.append(
          "state",
          this.taskState >= 0 ? this.state[this.taskState].type : "H"
        );
        if (this.attachFiles.length !== 0) {
          for (var i = 0, file; (file = this.attachFiles[i]); i++) {
            formData.append("attachFiles", file);
          }
        }
        formData.append("startDate", this.startDate);
        formData.append("endDate", this.endDate);

        formData.append(
          "manager",
          this.managers.map(function(o) {
            return o.memberNo;
          })
        );
        if (this.existCheckLists.length !== 0) {
          console.log("체크리스트있음");
          formData.append("checkLists", JSON.stringify(this.existCheckLists));
        }
        // if (this.existCheckLists.length !== 0) {
        //   for (var j = 0, chk; (chk = this.existCheckLists[j]); j++) {
        //     formData.append("checkLists[" + j + "].title", chk.title);
        //     formData.append("checkLists[" + j + "].taskId", chk.taskId);
        //     formData.append("checkLists[" + j + "].memberNo", chk.memberNo);
        //     formData.append(
        //       "checkLists[" + j + "].checkListItems",
        //       chk.checkListItems
        //     );
        //   }
        // }
        if (this.taskSuperId) {
          console.log("ADD SUB ~!!");
          console.log(this.lastSubSortNo);
          formData.append("taskSuperId", this.taskSuperId);

          formData.append("sortNo", this.lastSubSortNo);
          this.ADD_SUB_TASK(formData).then(() => {
            console.log("------------------");
            console.log("ADD SUB  완료");
            this.$emit("update");
            //this.$router.push(`/projects/${data.id}`);
          });
        } else {
          console.log("ADD SUPER ~!!");
          this.ADD_SUPER_TASK(formData).then(() => {
            console.log("------------------");
            console.log("ADD  SUPER 완료");
            this.$emit("update");
            //this.$router.push(`/projects/${data.id}`);
          });
        }
        this.onClose();
      }
    },
    showNewCheckItem(index) {
      // if (this.project.memberNo === -1) {
      //   alert("참여 멤버가 아닙니다 !");
      //   return "";
      // }
      this.showNewCheckItems = [];
      this.newCheckItem = "";
      this.showNewCheckItems[index] = true;
    },
    getCheckListItems(listStr) {
      return JSON.parse(listStr);
    },
    onCheckListSubmit() {
      // if (this.project.memberNo === -1) {
      //   alert("참여 멤버가 아닙니다 !");
      //   return "";
      // }
      let data = {
        taskId: this.$route.params.tid,
        title: this.checkListName,
        memberNo: this.project.memberNo,
        checkListItems: "[]",
      };
      console.log("checkList Submit: " + this.checkListName);
      // this.ADD_CHECK_LIST(data).then(() => {
      //   this.FETCH_CHECK_LISTS(this.$route.params.tid).then((data) => {
      //     this.existCheckLists = data;
      //   });
      // });
      this.newCheckList = false;
      this.existCheckLists.push(data);
      console.log("-----------------");
      console.log(this.existCheckLists);
      this.checkListName = "";
      //this.formClear();
    },
    check(itemNo, listNo) {
      if (this.project.memberNo === -1) {
        alert("참여 멤버가 아닙니다 !");
        return "";
      }
      console.log("체크!+", itemNo, this.project.memberNo);
      let data = {
        itemNo: itemNo,
        listNo: listNo,
      };
      this.SET_CHECK_ITEM(data).then(() => {
        this.FETCH_CHECK_LISTS(this.$route.params.tid).then((data) => {
          this.existCheckLists = data;
        });
      });
    },
    deleteCheckItem(itemIndex, listIndex) {
      let temp = JSON.parse(this.existCheckLists[listIndex].checkListItems);
      temp.splice(itemIndex, 1);
      this.existCheckLists[listIndex].checkListItems = JSON.stringify(temp);
    },
    deleteCheckList(index) {
      this.existCheckLists.splice(index, 1);
      // let data = {

      //   listNo: listNo,
      // };
      // this.DELETE_CHECK_LIST(data).then(() => {
      //   this.FETCH_CHECK_LISTS(this.$route.params.tid).then((data) => {
      //     this.existCheckLists = data;
      //   });
      // });
    },
    addNewCheckItem(index) {
      console.log("뉴 체크 아이템!");
      let newItem = {
        taskId: this.$route.params.tid,
        listNo: index,
        memberNo: this.project.memberNo,
        title: this.newCheckItem,
      };
      console.log(newItem);
      let temp = this.getCheckListItems(
        this.existCheckLists[index].checkListItems
      );
      temp.push(newItem);
      this.existCheckLists[index].checkListItems = JSON.stringify(temp);
      this.showNewCheckItems = [];
      this.newCheckItem = "";
      // this.ADD_NEW_CHECK_ITEM(newItem).then(() => {
      //   this.FETCH_CHECK_LISTS(this.$route.params.tid).then((data) => {
      //     console.log("-----------");
      //     console.log(data);
      //     this.existCheckLists = data;
      //     this.showNewCheckItems = [];
      //     this.newCheckItem = "";
      //   });
      // });
    },

    onClose() {
      // this.openModal = false;
      this.SET_ADD_TASK_MODAL(false);
      this.formClear();
      // this.$emit("close");
      this.SET_SUPER_TASK_ID("");
      this.SET_SUPER_TASK_PUBLIC(true);
      this.SET_LAST_SUB_SORT_NO(0);
      eventBus.$emit("clearForm");
    },
    remove(item) {
      const index = this.managers.indexOf(item.name);
      if (index >= 0) this.managers.splice(index, 1);
    },
    formClear() {
      //this.$refs.form.reset();
      this.title = "";
      this.description = "";
      this.taskLabel = [];
      this.taskState = undefined;
      this.startDate = "";
      this.endDate = "";
      this.labels = "";
      this.managers = [];
      this.usePublic = "true";
      this.attachFiles = [];
      this.existCheckLists = [];
      this.showNewCheckItems = [];
      this.checkListName = "";
      this.newCheckItem = "";
      this.$refs.form.resetValidation();
      // this.clearLabel = true;
      // this.clearLabel = false;
    },
    validate() {
      return this.$refs.form.validate();
    },
    addLabel(labelArr) {
      this.labels = JSON.stringify(labelArr); // labelArr Json String으로 변환할것 -> DB에 저장하는값
      this.taskLabel = []; // 라벨로 보여줄 객체뽑아올곳
      for (var i in labelArr) {
        var lb = this.labelList.find((item) => {
          return item.labelNo == labelArr[i];
        });
        this.taskLabel.push(lb);
        //this.taskLabel[i] = this.labelList[labelArr[i]];
        //this.labels += this.taskLabel[i].labelNo + ":";
      }
      console.log("-----addLabel-------");
      // console.log(this.taskLabel);
      console.log(this.labels);
    },
    addStartDate(date) {
      this.startDate = date;
    },
    addEndDate(date) {
      this.endDate = date;
    },
    addMember(managers) {
      console.log("addMember");
      console.log(managers);
      this.managers = managers;
    },
    // getImgCode(item) {
    //   return "data:image;base64," + item.imgCode;
    // }
  },
};
</script>

<style>
span.additional-title {
  font-size: 14px;
  margin-bottom: 5px;
  margin-left: 5px;
  display: inline-block;
  width: 150px;
  /* font-weight: 600; */
  color: dimgrey;
}
.add-task-card-form .col {
  padding: 0px;
}
.add-task-card-form .formFieldCol {
  padding: 16px;
}
.v-input--radio-group.v-input--radio-group--row .v-radio {
  margin-right: 0px;
}
.v-radio .v-icon {
  margin: 0px;
  font-size: 32px;
}
.primary-icon .v-icon {
  color: #1976d2;
}
.secondary-icon .v-icon {
  color: #424242;
}
.success-icon .v-icon {
  color: #4caf50;
}
.info-icon .v-icon {
  color: #2196f3;
}
.warning-icon .v-icon {
  color: #fb8c00;
}
.error-icon .v-icon {
  color: #ff5252;
}
.createTaskside {
  background-color: #d2d2d2;
}
.v-expansion-panel-content__wrap {
  padding: 10px;
}
.my-chip-group .v-slide-group__content {
  display: grid;
  margin: 0px;
}
.my-chip-group .v-chip {
  margin: 4px;
}
</style>
