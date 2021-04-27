<template>
  <v-app id="inspire">
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
          <v-col class="pl-0" cols="6" sm="7" md="7">
            <v-card>
              <v-card-text class="pa-0 ma-0">
                <v-row class="pa-0 ma-0" style="height:400px">
                  <v-col
                    class="login-side pa-0 ma-0 d-flex align-center"
                    cols="4"
                    justify="center"
                  >
                    <!-- <v-img :aspect-ratio="16/9" src="../assets/loginpage.png"></v-img> -->
                    <v-img width="200" src="../assets/Inzent2.png"></v-img>
                    <!-- <v-img width="300" src="../assets/loginpage.png"></v-img> -->
                    <!-- <div style="display:inline-block">
                      <v-img :aspect-ratio="16/9" src="https://cdn.vuetifyjs.com/images/parallax/material.jpg">
                        <v-row` align="end" class="lightbox white--text pa-2 fill-height">
                          <v-col>
                            <div class="subheading">Jonathan Lee</div>
                            <div class="body-1">heyfromjonathan@gmail.com</div>
                          </v-col>
                        </v-row>
                      </v-img>`
                    </div> -->
                  </v-col>
                  <v-col cols="7" class="ma-auto">
                    <h2>LOGIN</h2>
                    <br /><br />
                    <v-form
                      ref="form"
                      v-model="valid"
                      lazy-validation
                      @submit.prevent="onSubmit"
                    >
                      <p
                        v-if="error"
                        style="font-size:15px; color: red;"
                        align="center"
                      >
                        아이디 또는 패스워드가 틀렸습니다.
                      </p>
                      <v-text-field
                        v-model="id"
                        :rules="idRules"
                        label="id"
                        name="id"
                        prepend-icon="mdi-account"
                        type="text"
                      ></v-text-field>

                      <v-text-field
                        v-model="password"
                        :rules="passwordRules"
                        id="password"
                        label="Password"
                        name="password"
                        prepend-icon="mdi-lock"
                        type="password"
                        class="pwd"
                      ></v-text-field>
                      <v-card-actions>
                        <v-checkbox
                          v-model="idSave"
                          color="rgb(134, 138, 140)"
                          label="아이디 저장"
                          style="margin-right:15px"
                        ></v-checkbox>
                        <v-checkbox
                          v-model="autoLogin"
                          color="rgb(134, 138, 140)"
                          label="자동 로그인"
                          style="margin-right:15px"
                        ></v-checkbox>
                        <v-spacer></v-spacer>
                        <v-btn
                          color="primary"
                          class="font-weight-bold"
                          text
                          type="submit"
                          >로그인</v-btn
                        >
                      </v-card-actions>
                    </v-form>
                  </v-col>
                </v-row>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import { mapState } from "vuex";

export default {
  // props: {
  //   source: String
  // },
  data: () => ({
    valid: true,
    id: "",
    idRules: [(v) => !!v || "id is required"],
    password: "",
    passwordRules: [(v) => !!v || "password is required"],
    returnPath: "",
    error: "",
    idSave: false,
    autoLogin: false,
  }),
  created() {
    this.returnPath = this.$route.query.returnPath || "/";
    // cookie에서 idSave가 유효하면
    if (this.$cookie.get("idSave")) {
      // 로그인폼 불러올 때, 아이디 체크 true, id값 true
      this.idSave = true;
      this.id = this.$cookie.get("id");
    }
  },
  computed: {
    ...mapState({
      accessToken: "accessToken",
      userInfo: "userInfo",
    }),
  },
  methods: {
    onSubmit() {
      const { id, password, autoLogin } = this;
      this.$store
        .dispatch("LOGIN", { id, password, autoLogin})
        .then(() => {
          console.log("submit ok--------------------------------------------");
          if (this.idSave == true) {
            this.$cookie.set("id", id, 1);
            this.$cookie.set("idSave", this.idSave, 1);
          } else {
            this.$cookie.delete("id");
            this.$cookie.delete("idSave");
          }

          

          if (this.userInfo.userType == "s") {
            this.$router.push("/user_management");
          } else {
            this.$router.push(this.returnPath);
          }
        })
        .catch(() => {
          console.log(
            "submit error--------------------------------------------"
          );
          this.error = true;
        });
    },
  },
};
</script>

<style>
.login-side {
  background-color: rgb(106, 115, 136);
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}
.v-text-field__slot {
  font-family: sans-serif;
}
</style>
