<template>
  <v-row>
    <v-col cols="2">
      <v-card outlined height="800" class="overflow-y-auto mx-2">
        <v-text-field
          class="searchInput"
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
        <div class="mx-0">
          <v-subheader class="indigo lighten-5" style="margin-top:8px"
            >사내 조직도</v-subheader
          >
          <v-treeview
            v-model="selection"
            :items="items"
            selectable
            selection-type="all"
            open-all
            open-on-click
            return-object
            @input="selectDept"
          ></v-treeview>
        </div>
      </v-card>
    </v-col>
    <v-col cols="10">
      <v-card outlined max-height="800" class="overflow-y-auto mx-0">
        <v-subheader class="indigo lighten-5" style="">검색 결과</v-subheader>
        <v-card flat>
          <v-data-table
            :headers="this.headers"
            :items="this.userList"
            :search="search"
            v-model="selected"
            :items-per-page="12"
          >
            <template v-slot:item.avatar="{ item }">
              <v-avatar size="32px" v-if="item.imgCode">
                <!-- <img :src="'data:image;base64,'+item.imgCode"> -->
                <img :src="item.imgCode" />
              </v-avatar>
              <v-avatar size="32px" v-else>
                <v-icon>mdi-account</v-icon>
              </v-avatar>
            </template>

            <!-- //////////////////////////////////////////////////// -->
            <template v-slot:top>
              <v-toolbar flat color="white" style="height:50px">
                <!-- 유저 추가/수정 모달 -->
                <p></p>
                <v-spacer></v-spacer>
                <v-dialog v-model="dialog" width="1000">
                  <template v-slot:activator="{ on, attrs }">
                    <template v-if="userInfo.id == 'admin'">
                      <!-- <v-btn 
                      color="primary"
                      dark
                      class="mb-2"
                      v-bind="attrs"
                      v-on="on"
                      icon
                      style="padding-right:30px"
                    >
                      <v-icon size=48>mdi-plus-circle-outline</v-icon>
                    </v-btn> -->
                      <v-btn
                        color="primary"
                        dark
                        class="mb-2"
                        v-bind="attrs"
                        v-on="on"
                        style="margin-right:20px"
                        >추가</v-btn
                      >
                    </template>
                  </template>

                  <v-card width="auto">
                    <v-card-title class="headline grey lighten-2" primary-title>
                      <span class="headline">{{ formTitle }}</span>
                      <v-spacer></v-spacer>
                      <v-btn color="blue darken-1" icon @click="close">
                        <v-icon color="black lighten-1">mdi-close</v-icon>
                      </v-btn>
                    </v-card-title>
                    <v-card-text>
                      <v-container>
                        <v-row>
                          <template v-if="formTitle == '사용자 등록'">
                            <v-col cols="12" sm="6" md="3">
                              <v-text-field
                                outlined
                                v-model="editedItem.id"
                                label="사용자 ID"
                              ></v-text-field>
                            </v-col>  
                          </template>
                          <template v-else>
                            <v-col cols="12" sm="6" md="3">
                              <v-text-field
                                outlined
                                v-model="editedItem.id"
                                label="사용자 ID"
                                disabled
                              ></v-text-field>
                            </v-col>
                          </template>
                          <v-col cols="12" sm="6" md="3">
                            <v-text-field
                              outlined
                              v-model="editedItem.name"
                              label="이름"
                            ></v-text-field>
                          </v-col>
                          <v-col cols="12" sm="6" md="3">
                            <v-select
                              v-model="editedItem.rank"
                              :items="ranks"
                              label="직급"
                              outlined
                            />
                          </v-col>
                          <v-col cols="12" sm="6" md="3">
                            <v-select
                              v-model="editedItem.deptName"
                              :items="deptList"
                              item-text="deptName"
                              label="부서"
                              outlined
                            />
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-card-text>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="blue darken-1" text @click="close"
                        >취소</v-btn
                      >
                      <v-btn color="blue darken-1" text @click="save"
                        >확인</v-btn
                      >
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
              <v-icon small class="mr-2" @click="editItem(item)"
                >mdi-pencil
              </v-icon>
              <v-icon small @click="deleteItem(item)">mdi-delete </v-icon>
            </template>
          </v-data-table>
        </v-card>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import * as api from "../../api/index.js";
import { mapState, mapActions } from "vuex";
export default {
  data: () => ({
    search: "",
    selection: [],
    items: [],
    selected: [],
    userHeaders: [
      {
        text: "사진",
        align: "center",
        sortable: false,
        value: "avatar",
      },
      { text: "이름", align: "center", value: "name" },
      { text: "부서", align: "center", value: "deptName" },
      { text: "직급", align: "center", value: "rank" },
      { text: "연락처", align: "center", value: "phone" },
      { text: "이메일", align: "center", value: "email" },
    ],
    adminHeader: [
      {
        text: "사진",
        align: "center",
        sortable: false,
        value: "avatar",
      },
      { text: "이름", align: "center", value: "name" },
      { text: "부서", align: "center", value: "deptName" },
      { text: "직급", align: "center", value: "rank" },
      { text: "연락처", align: "center", value: "phone" },
      { text: "이메일", align: "center", value: "email" },
      { text: "관리", align: "center", value: "actions", sortable: false },
    ],
    deptList: [],
    userList: [],
    myTree: "",
    editedIndex: -1,
    editedItem: {
      id: "",
      name: "",
      rank: "",
      deptName: "",
    },
    defaultItem: {
      id: "",
      name: "",
      rank: "",
      deptName: "",
    },
    ranks: ["부장", "과장", "차장", "대리", "사원"],
    dialog: "",
  }),
  created() {
    this.facthData();
  },
  computed: {
    ...mapState({
      userInfo: "userInfo",
    }),

    headers() {
      let headers = [
        {
          text: "사진",
          align: "center",
          sortable: false,
          value: "avatar",
        },
        { text: "이름", align: "center", value: "name" },
        { text: "부서", align: "center", value: "deptName" },
        { text: "직급", align: "center", value: "rank" },
        { text: "연락처", align: "center", value: "phone" },
        { text: "이메일", align: "center", value: "email" },
      ];
      if (this.userInfo.id === "admin") {
        headers.push({
          text: "관리",
          align: "center",
          value: "actions",
          sortable: false,
        });
      }
      return headers;
    },

    formTitle() {
      return this.editedIndex === -1 ? "사용자 등록" : "사용자 수정";
    },
  },
  watch: {
    dialog(val) {
      val || this.close();
    },
  },
  methods: {
    ...mapActions(["INSERT_USER", "MODIFY_USER", "DELETE_USER"]),

    facthData() {
      api.user.fatch_dept().then((data) => {
        console.log("action: FATCH_DEPTLIST 완료", data);

        this.deptList = data;
        let list = data;

        console.log("list", list);
        list.forEach((item) => {
          for (let i = 0; i < list.length; i++) {
            if (item.parentDeptCode === list[i].deptCode) {
              item.parentId = list[i].id;
              break;
            }
          }
        });
        console.log("foreach list", list);

        let myTree = null;
        list.forEach((item) => {
          if (myTree === null) {
            myTree = { id: item.id, name: item.deptName };
            console.log(";", myTree);
          } else {
            this.Recursive(item, myTree);
          }
        });

        let treeArr = [];
        treeArr.push(myTree);

        this.items = treeArr;
        this.myTree = myTree;
      });

      this.fatchUser();
    },
    Recursive(item, tree) {
      //tree Id 와 item parentId가 같다면
      if (tree.id === item.parentId) {
        if (typeof tree.children === "undefined") {
          // tree에 children이 없다면
          tree.children = [];
          tree.children.push({ id: item.id, name: item.deptName });
        } else {
          // tree에 children이 있다.
          tree.children.push({ id: item.id, name: item.deptName });
        }
      } else {
        //tree Id와 item parentId가 다름
        if (typeof tree.children === "undefined") {
          // tree에 children이 없다면
          return;
        } else {
          // tree에 children이 있다면
          tree.children.forEach((child) => {
            this.Recursive(item, child);
          });
        }
      }
    },

    fatchUser() {
      if (this.userInfo.id === "admin") {
        let depts = [
          "1100",
          "1200",
          "1300",
          "2100",
          "2300",
          "2210",
          "2220",
          "3200",
          "3300",
          "3110",
          "3120",
        ];
        api.user.fatch_user(depts).then((data) => {
          this.userList = data;
        });
      } else {
        api.user.fatch_user().then((data) => {
          this.userList = data;
        });
      }
    },

    selectDept() {
      const selectedDept = [];
      for (let i = 0; i < this.selection.length; i++) {
        var dptmt = this.deptList.find((item) => {
          return item.id == this.selection[i].id;
        });
        selectedDept.push(dptmt.deptCode);
      }

      console.log("selectDept", selectedDept);

      api.user.fatch_user(selectedDept).then((data) => {
        this.userList = data;
      });
    },

    editItem(item) {
      this.editedIndex = this.userList.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      const index = this.userList.indexOf(item);
      confirm(item.name + " 사원을 삭제하시겠습니까??") &&
        this.userList.splice(index, 1);
      this.DELETE_USER(item.id);
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.editedIndex > -1) {
        // 수정
        this.MODIFY_USER(this.editedItem).then(() => {
          // Object.assign(this.userList[this.editedIndex], this.editedItem)
        });
      } else {
        // 추가
        console.log(this.editedItem);
        this.INSERT_USER(this.editedItem).then(() => {
          this.userList.push(this.editedItem);
        });
      }
      this.close();
      this.fatchUser();
    },
  },
};
</script>

<style>
.searchInput {
  padding: 5px;
  margin: 8px 8px 0px 8px;
}
.col-3 {
  padding-right: 0px;
}
.col-9 {
  padding-left: 0px;
}
.v-treeview-node__root {
  min-height: 35px;
}
</style>
