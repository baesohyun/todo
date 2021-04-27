<template>
  <div>
    <li class="sub-task-item" :class="`state${item.state}`">
      <router-link :to="`/projects/${board.id}/task/${item.taskId}`">
        <v-card
          class="rounded-lg mx-auto my-0"
          width="300"
          flat
          draggable="false"
        >
          <v-card-title>
            <v-icon small v-if="!item.usePublic">mdi-lock</v-icon>
            <span class="task-title">{{ item.title }}</span>
            <!-- {{ item.sortNo }}\\{{item.taskId}} -->
            <v-spacer></v-spacer>
            <!-- <v-btn icon @click.prevent="onStar"> -->
            <v-icon
              class="pt-1 pr-1"
              v-if="item.starred !== 0"
              color="amber darken-1"
              >mdi-star-outline</v-icon
            >
            <!-- </v-btn> -->
          </v-card-title>
          <v-card-subtitle
            class="pa-0 px-2 pt-2"
            v-if="item.startDate || item.endDate"
          >
            <v-chip label small color="#cacaca">
              <v-icon small left>mdi-clock-outline</v-icon>
              {{ item.startDate }} - {{ item.endDate }}
            </v-chip>
          </v-card-subtitle>
          <v-card-text class="px-2 pb-2">
            <v-row class="mx-0 mt-2">
              <v-col cols="auto" class="pa-0" v-if="item.labels">
                <v-chip
                  v-for="label in setLabel(item.labels)"
                  :key="label.lableNo"
                  filter
                  small
                  dark
                  class="mr-1"
                  :color="label.labelColor"
                  >{{ label.labelName }}</v-chip
                >
                <!-- <v-chip-group
                v-model="selection"
                active-class="deep-purple accent-4 white--text"
                column
              >
                <v-chip small>front</v-chip>
                <v-chip small>design</v-chip>
                </v-chip-group>-->
              </v-col>
            </v-row>
            <v-row class="mx-0 mt-2">
              <p class="mt-4 mb-0">
                <v-icon small>mdi-paperclip</v-icon>
                {{ item.fileCnt }}
                <v-icon small>mdi-comment-text-outline</v-icon>
                {{ item.commentCnt }}
              </p>
              <v-spacer></v-spacer>
              <v-tooltip
                v-for="manager in getManger(item.managerString)"
                :key="manager.memberNo"
                bottom
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-avatar
                    v-if="manager.imgCode"
                    size="36"
                    class="user-avatars"
                  >
                    <img :src="manager.imgCode" v-bind="attrs" v-on="on" />
                  </v-avatar>
                  <v-avatar v-else class="user-avatars" size="36" color="grey">
                    <v-icon fab dark v-bind="attrs" v-on="on"
                      >mdi-account</v-icon
                    >
                  </v-avatar>
                </template>
                <span>{{ manager.name }}</span>
              </v-tooltip>
              <!-- <v-avatar
              color="indigo"
              size="36"
              v-for="manager in getManger(item.manager)"
              :key="manager.memberNo"
            >
              <v-icon dark>mdi-account-circle</v-icon>
              </v-avatar>-->
            </v-row>
          </v-card-text>
        </v-card>
      </router-link>
    </li>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "Taskitem",
  props: ["item", "list", "board"],
  data() {
    return {
      selection: 1,
      active: false,
    };
  },
  computed: {
    ...mapState({
      labelList: "labelList",
      memberList: "memberList",
    }),
  },
  created() {},
  methods: {
    getLabelNo(labelString) {
      if (labelString !== null) {
        var labelsNo = labelString.split(":");
        const labels = [];
        labelsNo = labelsNo.slice(0, labelsNo.length - 1);
        for (var no in labelsNo) {
          var lb = this.labelList.find((item) => {
            return item.labelNo == labelsNo[no];
          });
          labels.push(lb);
        }
        return labels;
      }
    },
    getManger(managerString) {
      if (managerString !== null) {
        var managerNos = JSON.parse(managerString);
        const managers = [];
        for (var no in managerNos) {
          var mb = this.memberList.find((item) => {
            return item.memberNo == managerNos[no];
          });
          managers.push(mb);
        }
        return managers;
      }
    },
    onStar() {
      this.active = !this.active;
    },
    setLabel(labelString) {
      const labelArr = JSON.parse(labelString); // labelArr Json String으로 변환할것 -> DB에 저장하는값
      var label = []; // 라벨로 보여줄 객체뽑아올곳
      for (var i in labelArr) {
        var lb = this.labelList.find((item) => {
          return item.labelNo == labelArr[i];
        });
        label.push(lb);
        //this.taskLabel[i] = this.labelList[labelArr[i]];
        //this.labels += this.taskLabel[i].labelNo + ":";
      }
      return label;
    },
    // getImgCode(item) {
    //   return "data:image;base64," + item.imgCode;
    // }
  },
};
</script>

<style>
/* .card-task-child {
  border: solid 0px #132343 !important;
  border-left-width: 15px !important;
  border-left-color: #eb7f7f !important;
}*/
.sub-task-item {
  background: #fff;
  padding: 3px;
  border-radius: 5px;
  margin-bottom: 10px;
  -webkit-box-shadow: 0px 0px 4px 0px rgba(134, 134, 162, 0.21);
  -moz-box-shadow: 0px 0px 4px 0px rgba(134, 134, 162, 0.21);
  box-shadow: 0px 0px 4px 0px rgba(134, 134, 162, 0.21);
  border-left: solid 8px;
}
.stateH {
  border-color: #e0e0e0;
}
.stateP {
  border-color: #2196f3;
}
.stateC {
  border-color: #4caf50;
}
.stateE {
  border-color: #ff5252;
}
.stateW {
  border-color: #fb8c00;
}

.sub-task-item .v-card__title {
  font-size: 1em;
  padding: 0px 0px 10px 10px;
}
.task-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 82%;
}
.user-avatars {
  margin-left: -15px;
}
li a {
  text-decoration: none;
}

.router-link-exact-active {
  text-decoration: none;
}
</style>
