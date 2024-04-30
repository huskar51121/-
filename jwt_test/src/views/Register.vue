<template>
  <el-container>
    <div class="img_box"></div>
    <el-main class="mainpage">
    <div class="formContainer">
        <h2 class="title">Register</h2>
        <el-form
          ref="registerForm"
          :model="registerForm"
          status-icon
          class="demo-ruleForm"
        >
          <el-form-item label="Username" prop="username" label-width="80px">
            <el-input v-model="registerForm.username"
              prefix-icon="el-icon-user" 
              placeholder="Entry user name" 
              autocomplete="off" 
            />
          </el-form-item>
          <el-form-item label="Password" prop="password" label-width="80px">
            <el-input
              v-model="registerForm.password"
              type="password"
              show-password
              prefix-icon="el-icon-lock" 
              placeholder="Entry password"
              autocomplete="off"
            />
          </el-form-item>
          <el-form-item label="Mobile" prop="mobile" label-width="80px" :rules="rules.mobile">
            <el-input v-model="registerForm.mobile"
              prefix-icon="el-icon-phone" 
              placeholder="Entry mobile number" 
              autocomplete="off" 
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="registerSubmitForm()"
              >Register</el-button
            >
            <el-button @click="goLogin()"
              >Go login</el-button
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
  name: 'Register',
  data() {
    var checkPhone = (rule, value, callback) => {
    const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
    if (!value) {
      return
    }

          if (phoneReg.test(value)) {
            callback()
          } else {
            this.$message({
              message: 'Please enter the phone number in Chinese Mainland',
              type: 'error'
            });
          }
        }

    return {
      registerForm: {
        username: '',
        password: '',
        mobile: ''
      },
      rules: {
        mobile: [
          {validator: checkPhone, trigger: 'blur'}
        ]
      }
    }
  },
  created() {

  },

  

  methods: {
    registerSubmitForm() {
    if (this.registerForm.username === '') {
      this.$message({
            message: 'The username cannot be empty',
            type: 'error'
          });
      return;
    }
    if (this.registerForm.password === '') {
      this.$message({
            message: 'Password cannot be empty',
            type: 'error'
          });
      return;
    }
    if (this.registerForm.mobile === '') {
      this.$message({
            message: 'Phone number cannot be empty',
            type: 'error'
          });
      return;
    }

    this.$refs.registerForm.validate((valid) => {
        if (valid) {
      const params = qs.stringify(this.registerForm)
      // 在Vue组件中使用Axios发送POST请求
      console.log(this.registerForm.username);
      requestInstance.post('/userrequest/register', params)
      .then(response => {
        // 请求成功时的处理逻辑
        console.log(response); // 打印后端返回的字符串数据
        if (response.data == "创建成功") {
          this.$message({
            message: 'Created successfully',
            type: 'success'
          });
          //跳转登录路由
          this.$router.push("/login");
        } else {
           this.$message({
            message: response.data,
            type: 'error'
          });
        }
      });
        }else{
          return;
        }
    });
    },

    goLogin() {
        if(this.$route.name!='/login'){
            this.$router.push("/login");
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
  background-color: rgba(228, 231, 255, 0.9);
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