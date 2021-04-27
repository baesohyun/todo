import Vue from "vue";
import App from "./App.vue";
import router from "./routes";
import store from "./store";
import vuetify from "./plugins/vuetify";
import VueCookie from "vue-cookie";
import "./sass/tiptap/main.scss";
// Vue.config.productionTip = false;
Vue.use(VueCookie);

Vue.config.productionTip = false;

export const eventBus = new Vue();

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
