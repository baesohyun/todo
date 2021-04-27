<template>
  <v-menu offset-y :close-on-content-click="false">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" v-on="on" block depressed>
        <v-icon left>mdi-calendar-month-outline</v-icon>기간
      </v-btn>
    </template>
    <v-card class="my-chip-group">
      <v-card-title>DUE DATE</v-card-title>
      <v-card-text>
        <v-menu
          v-model="startDatePicker"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-if="existStartDateModel"
              v-model="existStartDateModel"
              label="Start date"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
              clearable
              @click:clear="inputStartDate(-1)"
            ></v-text-field>
            <v-text-field
              v-else
              v-model="startDate"
              label="Start date"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
              clearable
              @click:clear="inputStartDate(-1)"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="startDate"
            no-title
            @input="inputStartDate"
          ></v-date-picker>
        </v-menu>
        <br />
        <v-menu
          v-model="endDatePicker"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-if="existEndDateModel"
              v-model="existEndDateModel"
              label="End date"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
              clearable
              @click:clear="inputEndDate(-1)"
            ></v-text-field>
            <v-text-field
              v-else
              v-model="endDate"
              label="End date"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
              clearable
              @click:clear="inputEndDate(-1)"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="endDate"
            no-title
            @input="inputEndDate"
          ></v-date-picker>
        </v-menu>
      </v-card-text>
    </v-card>
  </v-menu>
</template>

<script>
import { eventBus } from "../../main.js";
export default {
  props: ["existStartDate", "existEndDate"],
  data() {
    return {
      //   dates: [],
      //   dateMenu: false,
      startDate: "", //new Date().toISOString().substr(0, 10),
      endDate: "",
      startDatePicker: false,
      endDatePicker: false,
    };
  },
  computed: {
    existStartDateModel: {
      get() {
        return this.existStartDate;
      },
      set(newVal) {
        this.$emit("addStartDate", newVal);
      },
    },
    existEndDateModel: {
      get() {
        return this.existEndDate;
      },
      set(newVal) {
        this.$emit("addEndDate", newVal);
      },
    },
  },
  created() {
    eventBus.$on("clearForm", () => {
      this.startDate = "";
      this.endDate = "";
    });
  },
  methods: {
    inputStartDate(val) {
      if (val === -1) {
        this.startDate = "";
      }
      this.startDatePicker = false;
      this.$emit("addStartDate", this.startDate);
    },
    inputEndDate(val) {
      if (val === -1) {
        this.endDate = "";
      }
      this.endDatePicker = false;
      this.$emit("addEndDate", this.endDate);
    },
  },
};
</script>

<style></style>
