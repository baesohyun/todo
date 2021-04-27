<template>
  <v-menu offset-y :close-on-content-click="false">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" v-on="on" block depressed>
        <v-icon left>mdi-label-outline</v-icon>태그
      </v-btn>
    </template>
    <v-card class="my-chip-group">
      <v-card-title class="px-4 pb-0">
        라벨 추가
        <!-- ADD LABELS {{this.labels}} -->
      </v-card-title>
      <v-card-text>
        <template v-if="existTaskLabel">
          <v-chip-group v-model="existTaskLabel" column multiple>
            <v-chip
              v-for="label in labelList"
              :key="label.labelNo"
              :value="label.labelNo"
              filter
              label
              text-color="white"
              :color="label.labelColor"
              >{{ label.labelName }}</v-chip
            >
          </v-chip-group>
        </template>
        <template v-else>
          <v-chip-group v-model="taskLabel" @change="addLabel" column multiple>
            <v-chip
              v-for="label in labelList"
              :key="label.labelNo"
              :value="label.labelNo"
              filter
              text-color="white"
              label
              :color="label.labelColor"
              >{{ label.labelName }}</v-chip
            >
          </v-chip-group>
        </template>
        <v-menu offset-x :close-on-content-click="false" top v-model="newLabel">
          <template v-slot:activator="{ on, attrs }">
            <v-btn v-bind="attrs" v-on="on" block depressed>
              <v-icon left>mdi-plus</v-icon>CREATE LABEL
            </v-btn>
          </template>
          <v-card class="my-chip-group">
            <v-card-text>
              <v-form
                ref="labelForm"
                v-model="validLabel"
                @submit.prevent="onLabelSubmit"
                lazy-validation
              >
                <v-text-field
                  label="CREATE LABEL"
                  v-model="labelName"
                  :rules="labelNameRules"
                  :counter="20"
                  required
                ></v-text-field>
                <v-radio-group
                  v-model="labelColor"
                  row
                  required
                  :rules="labelColorRules"
                >
                  <v-radio
                    on-icon="mdi-check-circle"
                    off-icon="mdi-checkbox-blank-circle"
                    color="primary"
                    value="1976d2"
                    class="primary-icon"
                  ></v-radio>
                  <v-radio
                    on-icon="mdi-check-circle"
                    off-icon="mdi-checkbox-blank-circle"
                    color="secondary"
                    value="424242"
                    class="secondary-icon"
                  ></v-radio>
                  <v-radio
                    on-icon="mdi-check-circle"
                    off-icon="mdi-checkbox-blank-circle"
                    color="success"
                    value="4caf50"
                    class="success-icon"
                  ></v-radio>
                  <v-radio
                    on-icon="mdi-check-circle"
                    off-icon="mdi-checkbox-blank-circle"
                    color="info"
                    value="2196f3"
                    class="info-icon"
                  ></v-radio>
                  <v-radio
                    on-icon="mdi-check-circle"
                    off-icon="mdi-checkbox-blank-circle"
                    color="warning"
                    value="fb8c00"
                    class="warning-icon"
                  ></v-radio>
                  <v-radio
                    on-icon="mdi-check-circle"
                    off-icon="mdi-checkbox-blank-circle"
                    color="error"
                    value="ff5252"
                    class="error-icon"
                  ></v-radio>
                </v-radio-group>
                <v-btn type="submit" block depressed>
                  <v-icon left>mdi-plus</v-icon>CREATE
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-menu>
      </v-card-text>
    </v-card>
  </v-menu>
</template>

<script>
import { mapActions, mapState } from "vuex";

export default {
  props: ["pid", "clear", "existLabel"],
  data() {
    return {
      taskLabel: undefined,
      // taskLabelName: ["보류", "진행", "완료", "긴급", "대기"],
      // taskLabelColor: ["defualt", "primary", "success", "error", "warning"],
      validLabel: true,
      labelName: "",
      labelColor: "",
      labelNameRules: [
        (v) => !!v || "name is required",
        (v) => (v && v.length <= 20) || "name must be less than 20 characters",
      ],
      labelColorRules: [(v) => !!v || "color is required"],
      newLabel: false,
    };
  },
  created() {
    this.taskLabel = undefined;
  },
  computed: {
    ...mapState({
      labelList: "labelList",
    }),
    existTaskLabel: {
      get() {
        return this.existLabel;
      },
      set(newVal) {
        this.$emit("setLabel", newVal);
      },
    },
    // addLabel(){
    //   console.log(this.taskLabel)
    //   return this.labelList
    // }
  },
  updated() {
    console.log("업데이트");
    // if (this.clear) {
    //   console.log("라벨비워");
    //   this.taskLabel = undefined;
    // }
  },
  methods: {
    ...mapActions(["FETCH_LABEL", "ADD_NEW_LABEL"]),
    onLabelSubmit() {
      if (this.validate()) {
        console.log("vali check!!");
        const newLabelData = {
          labelName: this.labelName,
          labelColor: "#" + this.labelColor,
          projectId: this.pid,
        };
        console.log(newLabelData);
        this.ADD_NEW_LABEL(newLabelData).then(() => {
          this.FETCH_LABEL(this.pid);
          console.log("Success Label Submit!!");
        });

        this.newLabel = false;
        this.formClear();
      }
    },
    validate() {
      return this.$refs.labelForm.validate();
    },
    formClear() {
      this.$refs.labelForm.reset();
    },
    addLabel() {
      console.log("addLabel : ", this.taskLabel);
      this.$emit("addLabel", this.taskLabel);
      // for (var label in this.taskLabel) {
      //   console.log(this.labelList[label]);
      // }
    },
  },
};
</script>

<style></style>
