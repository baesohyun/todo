<template>
  <v-dialog v-model="openModal" persistent max-width="1200">
    <v-card min-height="500">
      <v-card-title class="headline grey lighten-2 py-2" primary-title>
        ADD MEMBER
        <v-spacer></v-spacer>
        <v-btn color="grey darken-1" icon @click="onClose">
          <v-icon color="grey darken-1">mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <!-- <v-divider></v-divider> -->
      <v-card-text class="px-0 pb-0">
        <v-row height="100%" class="mx-5">
          <v-col>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              single-line
              hide-details
            ></v-text-field>
            <template v-if="selected">
              <!-- <p>selected</p> -->
              <!-- <p>selected length: {{selected.length}}</p> -->
              <v-data-table
                v-model="selected"
                :headers="headers"
                :items="memberList"
                :search="search"
                item-key="name"
                show-select
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
            </template>
            <template v-else-if="existTaskManagers">
              <v-data-table
                v-model="existTaskManagers"
                :headers="headers"
                :items="memberList"
                :search="search"
                item-key="name"
                show-select
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
            </template>
            <template v-else>
              <!-- <p>아예 새로만들때?</p> -->
              <v-data-table
                v-model="selected"
                :headers="headers"
                :items="memberList"
                :search="search"
                item-key="name"
                show-select
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
            </template>
          </v-col>
          <v-divider vertical></v-divider>
          <v-col class="px-6" cols="3">
            <v-card-title class="headline lighten-2 pb-2" primary-title
              >SELECTED MEMBER</v-card-title
            >
            <v-divider></v-divider>
            <!-- <p v-if="!selected.length && !existManagers">No one selected.</p> -->
            <v-sheet class="pa-4" v-if="selected">
              <!-- <p>selected</p> -->
              <!-- <v-chip-group column multiple v-model="selected"> -->
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
                <v-avatar v-else left>
                  <v-icon>mdi-account</v-icon>
                </v-avatar>
                {{ mem.name }}
              </v-chip>
              <!-- </v-chip-group> -->
            </v-sheet>
            <v-sheet class="pa-4" v-else>
              <!-- <p>no selected</p> -->
              <!-- <v-chip-group column multiple v-model="selected"> -->
              <v-chip
                v-for="mem in existManagers"
                :key="mem.id"
                class="ma-2"
                close
                @click:close="remove(mem)"
              >
                <v-avatar v-if="mem.imgCode" left>
                  <img :src="mem.imgCode" />
                </v-avatar>
                <v-avatar v-else left>
                  <v-icon>mdi-account</v-icon>
                </v-avatar>
                {{ mem.name }}
              </v-chip>
              <!-- </v-chip-group> -->
            </v-sheet>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey darken-1" text @click="onClose">취소</v-btn>
        <v-btn color="blue darken-1" text @click="onSubmit">확인</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState } from "vuex";
import { eventBus } from "../../main.js";

export default {
  props: ["openModal", "existManagers"],
  data: () => ({
    dialog: false,
    items: [],
    search: "",
    // open: undefined,
    selected: undefined,
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
    eventBus.$on("clearForm", () => {
      this.selected = undefined;
    });
  },
  destoryed() {
    console.log("유저 모달 삭제..");
    this.selected = undefined;
  },
  computed: {
    ...mapState({
      memberList: "memberList",
    }),
    existTaskManagers: {
      get() {
        return this.existManagers;
      },
      set(newVal) {
        console.log("existTaskManagers Setting,.,");
        console.log(newVal);
        this.selected = newVal; //업데이트 될 경우 변경될 값!
      },
    },
  },
  methods: {
    onClose() {
      this.selected = undefined;
      this.$emit("close");
    },
    onSubmit() {
      console.log(this.selected);
      // console.log(this.open);
      this.$emit("addMember", this.selected);
      this.$emit("close");
    },
    remove(item) {
      console.log("remove ...");
      // console.log(this.existTaskManagers);
      if (!this.selected) {
        this.selected = Object.assign([], this.existTaskManagers);
      }
      console.log(this.selected);
      const index = this.selected.indexOf(item);
      if (index >= 0) this.selected.splice(index, 1);
    },
    // getImgCode(item) {
    //   return "data:image;base64," + item.imgCode;
    // }
  },
};
</script>

<style></style>
