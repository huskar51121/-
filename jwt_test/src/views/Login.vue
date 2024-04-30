<template>
  <el-container>
    <div class="img_box"></div>
    <el-main class="mainpage">
        <div class="formContainer">
            <h2 class="title">Login</h2>
            <el-form
              ref="loginFormRef"
              :model="loginForm"
              status-icon
              class="demo-ruleForm"
            >
              <el-form-item label="Username" prop="username" label-width="80px">
                <el-input v-model="loginForm.username" 
                  prefix-icon="el-icon-user" 
                  placeholder="Enter user name " 
                  autocomplete="off" 
                />
              </el-form-item>
              <el-form-item label="Password" prop="password" label-width="80px">
                <el-input
                  v-model="loginForm.password"
                  show-password
                  type="password"
                  prefix-icon="el-icon-lock" 
                  placeholder="Enter password"
                  autocomplete="off"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="loginSubmitForm()"
                  >Login</el-button
                >
                <el-button @click="goRegister()"
                  >Go Register</el-button
                >
              </el-form-item>
              
            </el-form>
        </div>
      
    </el-main>
  </el-container>
</template>

<script>

import request from "@/utils/request";
import axios from 'axios'
import qs from 'qs'

const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    }
  },
  created() {

  },
  methods: {
    loginSubmitForm() {
    if (this.loginForm.username === '') {
      this.$message({
            message: 'The username cannot be empty',
            type: 'error'
          });
      return;
    }
    if (this.loginForm.password === '') {
      this.$message({
            message: 'Password cannot be empty',
            type: 'error'
          });
      return;
    }
    const params = qs.stringify(this.loginForm)
    // 在Vue组件中使用Axios发送POST请求
    requestInstance.post('/userrequest/login', params)
    .then(response => {
      // 请求成功时的处理逻辑
      console.log(response.data); // 打印后端返回的字符串数据
      if (response.status === 200 & response.data !== '密码错误' & response.data !== '该用户不存在') {
        this.$message({
            message: 'Login succeeded',
            type: 'success'
          });
        localStorage.setItem("user", response.data)
        this.$router.push("/")
      } else {
        this.$message({
            message: response.data,
            type: 'error'
          });
      }
    })
    .catch(error => {
      // 请求失败时的处理逻辑
      console.log(error);
    });
    },

    goRegister() {
      if(this.$route.name!='/register'){
            this.$router.push("/register");
        }
    }
  }
}
</script>

<style>
.title {
  color: black;
  margin-top: 20px;
  margin-bottom: 40px;
}

.formContainer {
  width: 500px;
  height: 350px;
  position: fixed;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid #53575c72;
  color: white;
  text-align: center;
  padding: 20px;
  background-color: rgba(127, 136, 237, 0.9);
  border-radius: 30px;
}

.mainpage {
  overflow: hidden;
  min-height: 100vh;
}

.img_box{
	width: 100%;height: 100%;
	background-image: url(@/assets/cyberpunk.jpg);
  /*https://www.wallpaperflare.com/
  digital-art-men-city-futuristic-night-neon-science-fiction-wallpaper-udroj*/
	background-size: cover; 
	position: absolute; 
	/* overflow: hidden; */
	/* overflow: auto; */
	z-index: -1;
	background-repeat: no-repeat;
}

</style>