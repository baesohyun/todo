<template>
  <v-dialog v-model="openModal" max-width="800" persistent>
    <v-card class="addTaskCard">
      <v-form ref="form" v-model="valid" @submit.prevent="onSubmit" lazy-validation>
        <v-card-title class="headline grey lighten-2" primary-title>
          CREATE SUB TASK
          <v-spacer></v-spacer>
        </v-card-title>
        <v-card-text class="py-0 px-3 add-task-card-form">
          <!-- <v-container> -->
          <v-row>
            <v-col cols="8">
              <v-col cols="12">
                <v-text-field label="TASK TITLE*" v-model="title" :rules="titleRules" required></v-text-field>
              </v-col>
              <v-col cols="12">
                PRIVATE*
                <v-radio-group v-model="usePublic" required row :rules="privateRules">
                  <br />
                  <v-radio label="Public" value="true"></v-radio>
                  <v-spacer></v-spacer>
                  <v-radio label="Private" value="false"></v-radio>
                  <v-spacer></v-spacer>
                </v-radio-group>
              </v-col>
              <v-col cols="12">
                <v-text-field label="DESCRIPTION" v-model="description"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-file-input chips multiple label="File input" v-model="attachFiles"></v-file-input>
              </v-col>
            </v-col>
            <v-col cols="4" class="createTaskside pa-0">
              <v-subheader>ADD OPTIONS</v-subheader>
              <v-expansion-panels accordion tile dense>
                <v-expansion-panel>
                  <v-expansion-panel-header>MEMBERS</v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <v-list dense>
                      <v-list-item>
                        <v-list-item-avatar class="align-self-center" color="white" contain>
                          <v-img src="https://randomuser.me/api/portraits/men/85.jpg" />
                        </v-list-item-avatar>
                        <v-list-item-title class="display-1">INZENT-TODO</v-list-item-title>
                      </v-list-item>
                      <v-btn text block>+ ADD MEMBER</v-btn>
                    </v-list>
                  </v-expansion-panel-content>
                </v-expansion-panel>
                <v-expansion-panel>
                  <v-expansion-panel-header>STATE</v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <v-chip-group v-model="state" column>
                      <v-chip filter label color="default">보류</v-chip>
                      <v-chip filter label color="primary">진행</v-chip>
                      <v-chip filter label color="success">완료</v-chip>
                      <v-chip filter label color="error">긴급</v-chip>
                      <v-chip filter label color="warning">대기</v-chip>
                    </v-chip-group>
                  </v-expansion-panel-content>
                </v-expansion-panel>
                <v-expansion-panel>
                  <v-expansion-panel-header>LABELS</v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <v-chip-group v-model="state" class="mx-0" column multiple>
                      <v-chip filter label color="default">보류</v-chip>
                      <v-chip filter label color="primary">진행</v-chip>
                      <v-chip filter label color="success">완료</v-chip>
                      <v-chip filter label color="error">긴급</v-chip>
                      <v-chip filter label color="warning">대기</v-chip>
                    </v-chip-group>
                    <v-btn text block>+ ADD LABELS</v-btn>
                  </v-expansion-panel-content>
                </v-expansion-panel>
                <v-expansion-panel>
                  <v-expansion-panel-header>DUE DATE</v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <v-menu
                      ref="menu"
                      v-model="menu"
                      :close-on-content-click="false"
                      transition="scale-transition"
                      offset-y
                      min-width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="dates"
                          label="Picker in menu"
                          prepend-icon="mdi-plus"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>
                      <v-date-picker v-model="dates" no-title scrollable range>
                        <v-spacer></v-spacer>
                        <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                        <v-btn text color="primary" @click="$refs.menu.save(dates)">OK</v-btn>
                      </v-date-picker>
                    </v-menu>
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-col>
          </v-row>
          <!-- </v-container> -->
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="onClose">Cancle</v-btn>
          <v-btn color="blue darken-1" text type="submit">Create</v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";

export default {
  props: ["openModal"],
  data: () => ({
    dialog: false,
    title: "",
    description: "",
    usePublic: undefined,
    attachFiles: [],
    startDate: "", //new Date().toISOString().substr(0, 10),
    endDate: "",
    dates: [],
    startDatePicker: false,
    endDatePicker: false,
    imgRules: [
      value =>
        !value ||
        value.size < 2000000 ||
        "Avatar size should be less than 2 MB!"
    ],
    menu: false,
    titleRules: [
      v => !!v || "title is required",
      v => (v && v.length <= 100) || "title must be less than 100 characters"
    ],
    // descRules: [
    //   (v) =>
    //     (v && v.length) <= 100 ||
    //     "description must be less than 100 characters",
    // ],
    privateRules: [v => !!v || "private is required"],
    valid: true,
    members: [],
    state: "",
    stateItems: ["진행", "보류", "긴급", "요청"]
  }),
  computed: {
    ...mapState({
      project: "project",
      taskSuperId: "taskSuperId",
      lastSubSortNo: "lastSubSortNo"
      // user 멤버 no가져오기..
    })
  },
  methods: {
    ...mapActions(["ADD_SUB_TASK"]),
    ...mapMutations(["SET_ADD_SUB_TASK_MODAL", "SET_SUPER_TASK_ID"]),
    onSubmit() {
      // Create Project
      console.log("add sub task..");
      console.log(this.state);
      if (this.validate()) {
        let formData = new FormData();
        console.log("-----test-----");
        console.log(this.project.id);
        console.log("lastSubSortNo", this.lastSubSortNo);
        formData.append("projectId", this.project.id);
        formData.append("taskSuperId", this.taskSuperId);
        formData.append("title", this.title);
        formData.append("description", this.description);
        formData.append("usePublic", this.usePublic);
        formData.append("memberNo", this.project.memberNo);
        formData.append("sortNo", this.lastSubSortNo);
        if (this.attachFiles.length !== 0) {
          for (var i = 0, file; (file = this.attachFiles[i]); i++) {
            formData.append("attachFiles", file);
          }
        }
        // formData.append("startDate", this.startDate);
        // formData.append("endDate", this.endDate);
        // formData.append("members", this.members);
        this.ADD_SUB_TASK(formData).then(() => {
          console.log("------------------");
          console.log("ADD SUB SUPER 완료");
          //this.$router.push(`/projects/${data.id}`);
        });
        this.SET_ADD_SUB_TASK_MODAL(false);
        this.formClear();
      }
    },
    onClose() {
      this.SET_ADD_SUB_TASK_MODAL(false);
      this.SET_SUPER_TASK_ID("");

      this.formClear();
      // this.$emit("close");
    },
    remove(item) {
      const index = this.members.indexOf(item.name);
      if (index >= 0) this.members.splice(index, 1);
    },
    formClear() {
      this.$refs.form.reset();
    },
    validate() {
      return this.$refs.form.validate();
    }
  }
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

.v-expansion-panel-content__wrap {
  padding: 0px 20px 0px 20px;
}
.add-task-card-form .v-slide-group__content {
  display: grid;
  margin-right: 0;
}
</style>
