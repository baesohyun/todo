<template>
  <v-navigation-drawer
    id="core-navigation-drawer"
    app
    :dark="true"
    color="rgb(106, 115, 136)"
    :right="$vuetify.rtl"
    permanent
    v-model="drawer"
    :mini-variant.sync="mini"
  >
    <v-divider class="mb-1" />
    <v-list dense nav>
      <v-list-item>
        <router-link :to="`/dashboard`">
          <v-list-item-avatar
            class="align-self-center mx-auto"
            color="white"
            contain
            size="35"
          >
            <v-img src="../../assets/inzent-logo.png" />
          </v-list-item-avatar>
        </router-link>
        <v-list-item-title class="sideTitle text-center"
          >INZENT-TODO</v-list-item-title
        >
        <v-btn icon @click.stop="mini = !mini">
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>
      </v-list-item>
    </v-list>
    <v-divider class="mb-2" />

    <v-list expand>
      <v-list-item-group v-model="activeMenu" color="defualt">
        <template v-if="userInfo.userType == 'u'">
          <v-list-item
            v-for="(menu, i) in menus1"
            :key="`menu-${i + 1}`"
            :to="menu.to"
          >
            <v-list-item-icon>
              <v-icon v-text="menu.icon"></v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title v-text="menu.text"></v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
        <template v-else>
          <v-list-item
            v-for="(menu, i) in menus2"
            :key="`menu-${i + 1}`"
            :to="menu.to"
          >
            <v-list-item-icon>
              <v-icon v-text="menu.icon"></v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title v-text="menu.text"></v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
        <div class="v-footer-item">
          <template v-if="userInfo.userType == 'u'">
            <!-- <v-list-item to="/profile"> -->
            <v-list-item @click.prevent="mypageModal = true">
              <v-list-item-icon>
                <v-icon>mdi-account</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>내 정보</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </template>
          <v-list-item @click.prevent="logoutModal = true">
            <v-list-item-icon>
              <v-icon>mdi-location-exit</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>로그아웃</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </div>
      </v-list-item-group>
    </v-list>
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
    <my-page :mypageModal="mypageModal" @close="mypageModal = false"></my-page>
  </v-navigation-drawer>
</template>

<script>
import { mapState, mapMutations } from "vuex";
import MyPage from "../user/MyPage.vue";

export default {
  components: {
    MyPage,
  },
  data: () => ({
    name: "Sidebar",
    activeMenu: undefined,
    drawer: true,
    mini: false,
    menus1: [
      {
        icon: "mdi-view-dashboard",
        text: "대시보드",
        to: "/dashboard",
      },
      {
        icon: "mdi-newspaper-variant-multiple-outline",
        text: "프로젝트",
        to: "/projects",
      },
      {
        icon: "mdi-calendar-month-outline",
        text: "스케쥴",
        to: "/schedule",
      },
      {
        icon: "mdi-magnify",
        text: "검색",
        to: "/search",
      },
      {
        icon: "mdi-archive",
        text: "보관함",
        to: "/archive",
      },
      {
        icon: "mdi-email-newsletter",
        text: "업무보고",
        to: "/report",
      },
    ],
    menus2: [
      {
        icon: " mdi-account-multiple",
        text: "사원관리",
        to: "/user_management",
      },
      // {
      //   icon: "mdi-newspaper-variant-multiple-outline",
      //   text: "PROJECT MANAGEMENT",
      //   to: "/project_management",
      // },
    ],
    mypageModal: false,
    logoutModal: false,
  }),
  computed: {
    ...mapState({
      // isShowSubMenu: "isShowSubMenu"
      userInfo: "userInfo",
    }),
  },
  methods: {
    ...mapMutations(["SET_IS_SHOW_SUB_MENU"]),
    // onShowSubMenu(key) {
    //   // console.log(key)
    //   this.SET_IS_SHOW_SUB_MENU({
    //     isShowSubMenu: this.isShowSubMenu,
    //     activeMenu: key
    //   });
    // },
    setActiveMenu(key) {
      // console.log(key)
      this.SET_ACTIVE_MENU(this.menus[key].text);
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
#core-navigation-drawer .v-list {
  padding: 0px;
  left: -12px;
  min-height: 56px;
}
#core-navigation-drawer div.v-list-item__title {
  font-size: 1rem !important;
}
#core-navigation-drawer .v-footer-item {
  position: fixed;
  width: 100%;
  bottom: 10px;
}
.sideTitle {
  cursor: default;
}
</style>
