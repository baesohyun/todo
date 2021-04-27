<template>
  <v-dialog v-model="openModal" max-width="800" persistent>
    <v-card>
      <v-form
        ref="form"
        v-model="valid"
        @submit.prevent="onSubmit"
        lazy-validation
      >
        <v-card-title class="headline grey lighten-2" primary-title>
          PROJECT INFO
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
                    label="PROJECT TITLE"
                    v-model="setProjectInfo.title"
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
                    label="PROJECT DESCRIPTION"
                    v-model="setProjectInfo.description"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  PRIVATE
                  <v-radio-group
                    required
                    :readonly="project.memberNo !== project.manager"
                    row
                    :rules="privateRules"
                    v-model="setProjectInfo.usePublic"
                  >
                    <br />
                    <v-radio label="Public" value="true"></v-radio>
                    <v-spacer></v-spacer>
                    <v-radio label="Private" value="false"></v-radio>
                    <v-spacer></v-spacer>
                  </v-radio-group>
                </v-col>
                <v-col cols="12">
                  <p>DUERATION</p>
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
                        v-model="setProjectInfo.startDate"
                        label="Start date"
                        prepend-icon="mdi-calendar"
                        readonly
                        :clearable="project.memberNo === project.manager"
                        v-bind="attrs"
                        v-on="on"
                      ></v-text-field>
                    </template>
                    <v-date-picker
                      v-model="setProjectInfo.startDate"
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
                        v-model="setProjectInfo.endDate"
                        label="End date"
                        prepend-icon="mdi-calendar"
                        readonly
                        :clearable="project.memberNo === project.manager"
                        v-bind="attrs"
                        v-on="on"
                      ></v-text-field>
                    </template>
                    <v-date-picker
                      v-model="setProjectInfo.endDate"
                      no-title
                      @input="endDatePicker = false"
                    ></v-date-picker>
                  </v-menu>
                </v-col>
                <v-col cols="12" sm="6">
                  <p>PROJECT COVER</p>
                  <v-card
                    height="65%"
                    class="mx-auto project-card"
                    :color="getColor()"
                  >
                    <v-img
                      v-if="setProjectInfo.coverImg"
                      height="100"
                      :src="getImg()"
                    ></v-img>
                  </v-card>
                </v-col>
                <v-col cols="12" sm="6">
                  <!-- <v-row> -->
                  <br />
                  <v-radio-group
                    v-model="setProjectInfo.coverColor"
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
                    placeholder="cover Image"
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
                <span class="pl-4">{{ memberList.length }}명</span>
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
                  <v-icon>mdi-account-plus</v-icon>
                </v-btn>
                <user-search-modal
                  :openModal="isOpenSearch"
                  @close="isOpenSearch = false"
                  @addMember="addMember"
                />
                <div
                  v-if="memberList !== []"
                  max-height="100%"
                  style="height:470px"
                  class="overflow-y-auto mt-3"
                >
                  <v-list>
                    <v-list-item v-for="mem in memberList" :key="mem.memberNo">
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
                        <v-list-item-title>{{ mem.userId }}</v-list-item-title>
                      </v-list-item-content>
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
          <v-btn color="blue darken-1" text @click="onClose">Cancle</v-btn>
          <v-btn color="blue darken-1" text type="submit">SAVE</v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions, mapState } from "vuex";
import UserSearchModal from "../user/UserSearchModal.vue";

export default {
  props: ["openModal"],
  components: {
    UserSearchModal,
  },
  data: () => ({
    isOpenSearch: false,
    dialog: false,
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
      (v) => (v && v.length <= 100) || "title must be less than 100 characters",
    ],
    privateRules: [(v) => !!v || "private is required"],
    valid: true,
    members: [],
    toggleTitle: false,
    toggleDesc: false,
    backupImg: "",
  }),
  created() {
    // console.log("ceretae,,,");
  },
  mounted() {
    // console.log("mounted..!!!!!!!!!!!!");
    // this.title = this.project.title;
    // this.description = this.project.description;
    // this.usePublic = this.project.usePublic ? "true" : "false";
    // this.coverColor = this.project.imgNo[0] == "#" ? this.project.imgNo : "";
    // this.startDate = this.project.startDate.trim();
    // this.endDate = this.project.endDate.trim();
  },
  beforeUpdate() {
    // console.log("before Update");
  },
  update() {
    // console.log("Update!!");
  },
  methods: {
    ...mapActions(["ADD_PROJECT"]),
    onSubmit() {
      // Create Project

      console.log("UPDATE Project");
      console.log(this.setProjectInfo);
      // console.log("title", this.title);
      // console.log("desc", this.description);
      // console.log("usePublic", this.usePublic);
      // console.log("origin-public", this.project.usePublic);
      console.log("------------");
      console.log(this.project);
      // if (this.validate()) {
      //   let formData = new FormData();
      //   formData.append("title", this.title);
      //   formData.append("description", this.description);
      //   formData.append("usePublic", this.usePublic);
      //   formData.append("startDate", this.startDate);
      //   formData.append("endDate", this.endDate);
      //   if (this.coverImg !== undefined) {
      //     console.log("test------------");
      //     console.log(this.coverImg);
      //     formData.append("coverImg", this.coverImg);
      //   }
      //   formData.append("coverColor", this.coverColor);
      //   var mems = [];
      //   for (var a in this.members) {
      //     if (this.userInfo.id !== this.members[a].id)
      //       mems.push(this.members[a].id);
      //   }
      //   formData.append("members", mems);

      //   this.ADD_PROJECT(formData).then(data => {
      //     // console.log("-----");
      //     // console.log(data);
      //     this.$router.push(`/projects/${data.id}`);
      //   });
      //   this.formClear();
      //   this.$emit("close");
      // }
    },
    setCoverColor() {
      console.log(this.setProjectInfo.coverColor);
      this.project.imgNo = this.setProjectInfo.coverColor;
    },
    onClose() {
      // this.openModal = false;
      this.formClear();
      this.$emit("close");
    },
    remove(item) {
      const index = this.members.indexOf(item.name);
      if (index >= 0) this.members.splice(index, 1);
    },
    formClear() {
      this.title = "";
      this.description = "";
      this.toggleTitle = false;
      this.toggleDesc = false;
      this.startDate = "";
      this.endDate = "";
      this.coverColor = "";
      this.setProjectInfo.coverImg = this.backupImg;
      // this.coverColor = "1976d2";
    },
    validate() {
      return this.$refs.form.validate();
    },
    addMember(members) {
      console.log("addMember", members.length);
      this.members = members;
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
          this.project.imgNo = e.target.result;
          console.log("image", this.image);
        };
        console.log("333");
        //console.log(this.image);
        //this.setProjectInfo.coverImg = this.image;
        reader.readAsDataURL(fileObject);
      } else {
        console.log("사진지우기");
        this.project.imgNo = this.backupImg;
        // this.setProjectInfo.coverImg = undefined;
      }
    },
    getImg() {
      // if (this.project.imgNo[0] == "#") return "";
      // // console.log(this.project.imgNo);
      // return this.project.imgNo; //"https://cdn.vuetifyjs.com/images/cards/house.jpg";
      if (this.setProjectInfo.coverColor) "";
      // console.log(this.project.imgNo);
      return this.setProjectInfo.coverImg;
    },
    getColor() {
      return this.setProjectInfo.coverColor;
    },
    getMember(no) {
      var mb = this.memberList.find((item) => {
        return item.memberNo == no;
      });
      return mb;
    },
    backupTitle() {
      this.bTitle = this.project.title;
      console.log("----------");
      console.log(this.bTitle);
    },
    editTitle() {
      console.log("editTitle..!!");
      if (this.project.memberNo === this.project.manager) {
        this.toggleTitle = !this.toggleTitle;
        this.title = this.project.title;
      }
    },
    editDescription() {
      console.log("editDescription..!!");
      if (this.project.memberNo === this.project.manager) {
        this.toggleDesc = !this.toggleDesc;
        this.description = this.project.description;
      }
    },
    editPublic() {
      console.log("edit Public");
    },
    // getImgCode(item) {
    //   return "data:image;base64," + item.imgCode;
    // }
  },
  computed: {
    ...mapState({
      userInfo: "userInfo",
      project: "project",
      memberList: "memberList",
      // user 멤버 no가져오기..
    }),
    isCoverImg() {
      return this.coverImgFile === undefined ? false : true;
    },
    setProjectInfo() {
      console.log("setProjectInfo..");
      const projectInfo = {};
      projectInfo.title = this.project.title;
      projectInfo.description = this.project.description;
      projectInfo.usePublic = this.project.usePublic ? "true" : "false";
      if (this.project.imgNo[0] == "#")
        projectInfo.coverColor = this.project.imgNo;
      else projectInfo.coverImg = this.project.imgNo;
      projectInfo.startDate = this.project.startDate.trim();
      projectInfo.endDate = this.project.endDate.trim();

      return projectInfo;
    },
  },
  // setCoverColor: {
  //   get() {
  //     console.log("get");
  //     return this.project.imgNo[0] == "#" ? this.project.imgNo[0] : undefined;
  //   },
  //   set(newVal) {
  //     console.log("set");
  //     this.coverColor = newVal;
  //   },
  // },
};
</script>

<style>
.v-input--radio-group.v-input--radio-group--row .v-radio {
  margin-right: 0px;
}
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
.user-avatars {
  margin-left: -15px;
}
</style>
