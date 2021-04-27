<template>
  <v-container id="projectsMain" fluid tag="section">
    <h3 class="main-title">참여 중인 프로젝트</h3>
    <v-divider></v-divider>
    <v-row>
      <template v-for="p in projects.myProject">
        <project-card :key="p.id" :project="p" />
      </template>
    </v-row>
    <br />
    <h3 class="main-title">전체 프로젝트</h3>
    <v-divider></v-divider>
    <v-row>
      <v-col cols="12" xs="6" sm="6" md="4" lg="3">
        <v-card
          class="mx-auto"
          height="100%"
          @click.prevent="isOpenProject = true"
        >
          <v-card-text class="text--primary text-center">
            <h3 class="my-2 main-title">새 프로젝트</h3>
            <v-icon large blue>mdi-plus</v-icon>
          </v-card-text>
        </v-card>
      </v-col>
      <template v-for="p in projects.allProject">
        <project-card :key="p.id" :project="p" />
      </template>
    </v-row>
    <!-- </div> -->
    <add-project :openModal="isOpenProject" @close="isOpenProject = false" />
  </v-container>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
export default {
  name: "project-main",
  data: () => ({
    isOpenProject: false,
    loading: false,
  }),

  computed: {
    ...mapState({
      projects: "projects",
    }),
  },
  components: {
    ProjectCard: () => import("../components/project/projectCard.vue"),
    AddProject: () => import("../components/project/AddProject.vue"),
  },
  created() {
    console.log("프로젝트메인");
    this.fetchData();
    console.log(this.$route);
    this.SET_HEADER_TITLE("프로젝트 목록");
    this.SET_APPBAR_ICON("mdi-newspaper-variant-multiple-outline");
    //console.dir(this.projects);
  },
  methods: {
    ...mapMutations(["SET_HEADER_TITLE", "SET_APPBAR_ICON"]),
    ...mapActions(["FETCH_PROJECTS"]),
    openCreateBoard() {
      this.isOpenProject = true;
    },
    fetchData() {
      this.loading = true;
      this.FETCH_PROJECTS().finally(() => {
        this.loading = false;
      });
    },
  },
};
</script>

<style>
.main-title {
  color: #616161;
}
</style>
