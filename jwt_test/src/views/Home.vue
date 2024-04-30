<template>
  <div class="home">
    <div>Welcome!</div>
  </div>
</template>

<script>

import axios from 'axios';
import qs from 'qs'

const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

export default {
  name: 'Home',

  data() {
    return {
      tokenForm:{
            token: ''
        },
      idCheck: '',
    }
  },

   mounted() {
    this.fetchname();
  },
  methods: {
    fetchname() {
      this.tokenForm.token = localStorage.getItem("user");
      const params = qs.stringify(this.tokenForm);
      console.log(this.tokenForm.token);
      console.log(params);
      //console.log("12345");
      requestInstance.post('/userrequest/afterlogin', params).then(
          response => {
            this.idCheck = response.data.id;
            console.log("USER ID IS");
            console.log(this.idCheck);
            localStorage.setItem("userid", this.idCheck);
          },
          response => {
            console.log("error");
            alert("User information request failed");
            this.$router.push("/login")
          }
      );
    },
  }
}
</script>
