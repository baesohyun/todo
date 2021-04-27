<template>
  <v-container fluid tag="section">
    <v-row class="archive-row">
      <v-col cols="12" md="6">
        <sub-archive />
      </v-col>
      <v-col cols="12" md="6">
        <super-archive />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SubArchive from "../components/archive/SubArchive.vue";
import SuperArchive from "../components/archive/SuperArchive.vue";
import { mapMutations } from "vuex";

export default {
  components: {
    SubArchive,
    SuperArchive,
  },
  created() {
    this.SET_HEADER_TITLE("보관함");
    this.SET_APPBAR_ICON("mdi-archive");
  },
  methods: {
    ...mapMutations(["SET_HEADER_TITLE", "SET_APPBAR_ICON"]),
  },
};
</script>
<style scoped>
.archive-row {
  margin-right: 20px;
  margin-left: 20px;
  margin-top: 30px;
}
</style>
