<template>
  <div>
      <v-container class="user-management" fluid>
          <user-search class="user-search"></user-search>
      </v-container>
  </div>
</template>

<script>
import UserSearch from "../components/user/UserSearch.vue"
import { mapMutations } from "vuex"
export default {
    components: {
        UserSearch,
    },
    created() {
        this.SET_HEADER_TITLE("사원 관리");
    },
    methods: {
        ...mapMutations(["SET_HEADER_TITLE"])
    }
}
</script>

<style>

</style>