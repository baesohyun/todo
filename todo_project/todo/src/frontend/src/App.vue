<template>
  <v-app>
    <app-bar v-if="isLoginPage()" />
    <side-bar v-if="isLoginPage()" />
    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
// import request from 'request'
// import {mapState} from 'vuex'

export default {
  name: "App",
  components: {
    SideBar: () => import("@/components/core/SideBar.vue"),
    AppBar: () => import("@/components/core/AppBar.vue"),
  },
  computed: {},
  methods: {
    /* 로그인 페이지에서는 Header, Navigator 숨기기 */
    isLoginPage() {
      return location.pathname !== "/login";
    },
  },
};
/* 
  components: {
  },

  data: () => ({
    //
  }),
  mounted(){
    console.log('test1')
    request('http://localhost:8080/api/hello', function(error, response, body){
      window.console.log('error:', error);
      window.console.log('statusCode:', response && response.statusCode);
      window.console.log('body:', body);
    });
  }
}
 */
</script>
<style>
@import url(//fonts.googleapis.com/earlyaccess/jejugothic.css);

* {
  /* background: #ffffff; */
  font-family: "Jeju Gothic", sans-serif;
}

/* html,
body {
  height: 100%;
} */
/* @font-face {
  font-family: "paybooc-Bold";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/paybooc-Bold.woff")
    format("woff");
  font-weight: lighter;
  font-style: normal;
}
@font-face {
  font-family: "NEXON Lv2 Gothic Bold";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@2.1/NEXON Lv2 Gothic Bold.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "NEXON Lv1 Gothic OTF Light";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@2.1/NEXON Lv1 Gothic OTF Light.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
} */

@font-face {
  font-family: "GmarketSansMedium";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "GmarketSansLight";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansLight.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "GmarketSansBold";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansBold.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
</style>
