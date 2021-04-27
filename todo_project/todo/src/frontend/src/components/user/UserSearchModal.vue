<template>
  <v-dialog v-model="openModal" persistent max-width="1300">
    <v-card class="mx-auto" max-width="1300">
      <v-card-title class="headline grey lighten-2 py-2" primary-title>
        ADD MEMBER
        <v-spacer></v-spacer>
        <v-btn color="grey darken-1" icon @click="onClose">
          <v-icon color="grey darken-1">mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-divider></v-divider>
      <v-container fluid>
        <v-row style="min-height:660px">
          <v-col cols="2" class="px-0">
            <v-treeview
              v-model="selection"
              :items="items"
              selectable
              selection-type="leaf"
              return-object
              open-all
              open-on-click
              @input="selectDept"
            ></v-treeview>
          </v-col>
          <v-divider vertical></v-divider>
          <v-col>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            <v-data-table
              :headers="headers"
              :items="this.userList"
              :search="search"
              v-model="selected"
              show-select
              @input="test"
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
            </v-data-table>
          </v-col>
          <v-divider vertical></v-divider>
          <v-col class="pa-6" cols="2">
            <template v-if="!selected.length">No one selected.</template>
            <v-sheet class="pa-0">
              <v-chip
                v-for="mem in selected"
                :key="mem.id"
                class="ma-2"
                close
                @click:close="remove(mem)"
              >
                <v-avatar v-if="mem.imgCode" left>
                  <img :src="mem.imgCode" />
                </v-avatar>
                <v-avatar v-else>
                  <v-icon>mdi-account</v-icon>
                </v-avatar>
                {{ mem.name }}
              </v-chip>
            </v-sheet>
          </v-col>
        </v-row>
        <v-row>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="onSubmit">확인</v-btn>
          <v-btn color="blue darken-1" text @click="onClose">취소</v-btn>
        </v-row>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  props: ["openModal"],
  data: () => ({
    dialog: false,
    selection: [],
    items: [],
    search: "",
    // open: undefined,
    selected: [],
    headers: [
      {
        text: "사진",
        align: "start",
        sortable: false,
        value: "avatar",
      },
      { text: "이름", value: "name" },
      { text: "부서", value: "deptName" },
      { text: "직급", value: "rank" },
      { text: "연락처", value: "phone" },
      { text: "이메일", value: "email" },
    ],
    model: null,
    myTree: "",
  }),
  created() {
    this.fatch_data();
    // console.log("this.memberList", this.memberList);
    console.log("## created User SEARCH");
    // if (this.memberList.length) {
    //   console.log("---------멤버리스트가 있냐?-------");
    //   // console.log("memberList :", this.memberList);
    //   this.selected = Object.assign([], this.memberList);
    //   // this.selected = this.memberList;
    //   // console.log("selected :", this.memberList);
    // } else {
    //   console.log("---------멤버리스트없다!!?-------");
    // }
  },
  computed: {
    ...mapState({
      deptList: "deptList",
      userList: "userList",
      memberList: "memberList",
    }),
  },
  methods: {
    ...mapActions(["FATCH_DEPTLIST", "FATCH_USERLIST"]),

    fatch_data() {
      this.FATCH_DEPTLIST().then(() => {
        let list = this.deptList;

        // console.log("list", list);
        list.forEach((item) => {
          for (let i = 0; i < list.length; i++) {
            if (item.parentDeptCode === list[i].deptCode) {
              item.parentId = list[i].id;
              break;
            }
          }
        });
        // console.log("foreach list", list);

        let myTree = null;
        list.forEach((item) => {
          if (myTree === null) {
            myTree = { id: item.id, name: item.deptName };
            console.log(";", myTree);
          } else {
            this.Recursive(item, myTree);
          }
        });
        // console.log("myTree", myTree)

        let treeArr = [];
        treeArr.push(myTree);

        this.items = treeArr;
        this.myTree = myTree;
      });
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
    test(val) {
      console.log("test ", val);
      // this.selected = val.slice(1);
      console.log("selected ", this.selected);
    },
    onClose() {
      console.log("## onclose실행");
      if (this.memberList.length) {
        console.log("---------멤버리스트가 있냐?-------");
        console.log("memberList :", this.memberList);
        // this.selected = Object.assign([], this.memberList);
        // console.log("selected :", this.memberList);
        this.selected = [];
        /* this.selected = [];
        this.selected = Object.assign([], this.memberList); */
        console.log("selected :", this.selected);
      }
      this.$emit("close");
    },
    onSubmit() {
      console.log(this.selected);
      // console.log(this.open);
      this.$emit("addMember", this.selected);
      this.$emit("close");
    },
    remove(item) {
      // console.log(item);
      console.log("remove item실행..");
      const index = this.selected.indexOf(item);
      // console.log("index: " + index);
      if (index >= 0) this.selected.splice(index, 1);
    },

    selectDept() {
      const selectedDept = [];
      for (let i = 0; i < this.selection.length; i++) {
        var dptmt = this.deptList.find((item) => {
          return item.id == this.selection[i].id;
        });
        selectedDept.push(dptmt.deptCode);
      }

      // console.log("myTree", this.myTree);
      // console.log("chlidren id " + this.myTree.children[0].id);
      // console.log(selectedDept);

      this.FATCH_USERLIST(selectedDept);
    },
    // getImgCode(item) {
    //   return "data:image;base64," + item.imgCode;
    // }
  },
};
</script>

<style>
.v-treeview-node__root {
  min-height: 35px;
}
</style>
