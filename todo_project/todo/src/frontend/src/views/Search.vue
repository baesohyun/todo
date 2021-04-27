<template>
  <div>
    <v-container class="search-container" fluid tag="section">
      <v-tabs v-model="tabs" color="indigo lighten-1">
        <v-tab>프로젝트 및 업무 검색</v-tab>
        <v-tab>사용자 검색 및 조직도 조회</v-tab>
      </v-tabs>

      <v-tabs-items v-model="tabs" class="search-tab">
        <v-tab-item>
          <v-divider></v-divider>
          <project-search class="project-search"></project-search>
        </v-tab-item>
        <v-tab-item>
          <v-divider></v-divider>
          <user-search class="user-search"></user-search>
        </v-tab-item>
      </v-tabs-items>
    </v-container>
  </div>
</template>

<script>
import ProjectSearch from "../components/project/ProjectSearch.vue";
import UserSearch from "../components/user/UserSearch.vue";
import { mapMutations } from "vuex";
export default {
  components: {
    ProjectSearch,
    UserSearch,
  },
  data() {
    return {
      tabs: null,
    };
  },
  created() {
    this.SET_HEADER_TITLE("검색");
    this.SET_APPBAR_ICON("mdi-magnify");
  },
  methods: {
    ...mapMutations(["SET_HEADER_TITLE", "SET_APPBAR_ICON"]),
  },
};
</script>

<style scoped>
.search-container {
  min-height: 100%;
  padding: 0px;
}
</style>
