<template>
  <v-app-bar id="app-bar" absolute app color="transparent" flat height="50">
    <v-toolbar-title class="font-weight-light">
      <v-icon left>{{ appBarIcon }}</v-icon>
      {{ headerTitle }}
    </v-toolbar-title>
    <v-spacer />

    <v-menu
      bottom
      right
      offset-y
      origin="top right"
      transition="scale-transition"
    >
      <template v-slot:activator="{ attrs, on }">
        <v-btn
          depressed
          class="mt-2 mr-5 px-3"
          min-width="0"
          color="white"
          v-bind="attrs"
          v-on="on"
        >
          <v-avatar size="30px" class="mr-3">
            <img :src="userInfo.imgCode" />
          </v-avatar>
          {{ userInfo.id }}
        </v-btn>
      </template>
      <v-list :tile="false" nav>
        <div>
          <template v-if="userInfo.id == 'admin'">
            <app-bar-item
              v-for="(n, i) in adminNofifications"
              :key="`item-${i}`"
            >
              <v-list-item style="margin-bottom:0px" @click="openModal(n)">
                <v-list-item-title v-text="n" />
              </v-list-item>
            </app-bar-item>
          </template>
          <template v-else>
            <app-bar-item
              v-for="(n, i) in userNotifications"
              :key="`item-${i}`"
            >
              <v-list-item style="margin-bottom:0px" @click="openModal(n)">
                <v-list-item-title v-text="n" />
              </v-list-item>
            </app-bar-item>
          </template>
        </div>
      </v-list>
    </v-menu>

    <!-- 마이페이지 모달 -->
    <my-page :mypageModal="mypageModal" @close="mypageModal = false"></my-page>
    <!-- 로그아웃 모달 -->
    <v-dialog v-model="logoutModal" max-width="230">
      <v-card>
        <v-card-title></v-card-title>
        <v-card-text>로그아웃 하시겠습니까?</v-card-text>
        <v-card-actions class="justify-center">
          <v-btn color="green darken-1" text @click="logoutModal = false"
            >아니요</v-btn
          >
          <v-btn color="green darken-1" text @click.prevent="logout">예</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app-bar>
</template>

<script>
// Components
import { VHover, VListItem } from "vuetify/lib";
import MyPage from "../user/MyPage.vue";

// Utilities
import { mapState, mapMutations } from "vuex";

export default {
  name: "DashboardCoreAppBar",

  components: {
    AppBarItem: {
      render(h) {
        return h(VHover, {
          scopedSlots: {
            default: ({ hover }) => {
              return h(
                VListItem,
                {
                  attrs: this.$attrs,
                  class: {
                    "black--text": !hover,
                    "white--text secondary elevation-12": hover,
                  },
                  props: {
                    activeClass: "",
                    dark: hover,
                    link: true,
                    ...this.$attrs,
                  },
                },
                this.$slots.default
              );
            },
          },
        });
      },
    },
    MyPage,
  },

  props: {
    value: {
      type: Boolean,
      default: false,
    },
  },

  data: () => ({
    userNotifications: ["내 정보", "로그아웃"],
    adminNofifications: ["로그아웃"],
    search: "",
    mypageModal: false,
    logoutModal: false,
  }),

  computed: {
    ...mapState({
      activeMenu: "activeMenu",
      headerTitle: "headerTitle",
      appBarIcon: "appBarIcon",
      userInfo: "userInfo",
    }),
  },

  methods: {
    ...mapMutations({
      setDrawerSub: "SET_DRAWER_SUB",
    }),
    openModal(item) {
      if (item === "내 정보") {
        this.mypageModal = true;
      } else {
        this.logoutModal = true;
      }
    },
    logout() {
      this.$store.commit("LOGOUT");
      this.$router.push("/login");
      this.logoutModal = false;
    },
  },
};
</script>
<style scoped>
.v-toolbar__title {
  font-weight: 600 !important;
  color: dimgrey;
}
</style>
