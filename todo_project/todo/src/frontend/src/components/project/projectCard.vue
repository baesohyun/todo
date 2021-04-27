<template>
  <v-col cols="12" xs="6" sm="6" md="4" lg="3">
    <v-card class="mx-auto project-card" :color="getColor()" height="100%">
      <router-link :to="`/projects/${project.id}`">
        <v-img
          v-if="project.imgNo"
          class="white--text align-end"
          height="100"
          :src="getImg()"
        >
          <!-- <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn icon>
              <v-icon>mdi-star</v-icon>
            </v-btn>
          </v-card-actions>-->
          <v-card-title>
            <v-icon dark small v-if="!project.usePublic">mdi-lock</v-icon>
            {{ project.title }}
          </v-card-title>
        </v-img>

        <v-progress-linear
          color="rgba(40, 40, 40, 0.3)"
          height="10"
          :value="project.progressRate"
        ></v-progress-linear>

        <v-card-text class="text--primary text-left pt-3">
          <p class="grey--text mb-0" style="font-size:12px">MEMBER</p>
          <v-chip>
            <v-avatar v-if="project.imgCode" left>
              <img :src="project.imgCode" />
            </v-avatar>
            <v-avatar v-else left>
              <v-icon>mdi-account-circle</v-icon>
            </v-avatar>
            {{ project.userId }}
          </v-chip>
          <span v-if="project.memberCnt > 1">
            외 {{ project.memberCnt - 1 }} 명</span
          >
        </v-card-text>
        <v-card-subtitle class="py-0" min-height="400px">
          <v-chip
            label
            small
            color="#cacaca"
            v-if="project.startDate.trim() || project.endDate.trim()"
          >
            <v-icon small left>mdi-clock-outline</v-icon>
            {{ project.startDate }} - {{ project.endDate }}
          </v-chip>
        </v-card-subtitle>
      </router-link>
    </v-card>
  </v-col>
</template>

<script>
export default {
  props: ["project"],
  data() {
    return {};
  },
  computed: {},
  // mounted() {
  //   console.log(this.project);
  // }
  methods: {
    getColor() {
      return this.project.imgNo;
    },
    getImg() {
      if (this.project.imgNo[0] == "#") return "";
      // console.log(this.project.imgNo);
      return this.project.imgNo; //"https://cdn.vuetifyjs.com/images/cards/house.jpg";
    },
  },
};
</script>

<style>
.project-card .v-card__subtitle {
  min-height: 40px;
  background-color: #ffffff;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
}
.project-card .v-card__text {
  background-color: #ffffff;
  /* border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px; */
}
.project-card .v-card__title {
  font-size: 1.3rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.project-card a {
  text-decoration: none;
}
</style>
