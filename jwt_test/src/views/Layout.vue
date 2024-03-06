<template>
  <div>
    <el-container>
      <el-header style="background-color: #606266">

        <img src="@/assets/logo.png" alt="logo.png" style="width: 40px; position: relative; top: 10px">
        <span style="font-size: 20px; margin-left: 15px; color: white">Logo</span>
        <el-dropdown style="float: right; height: 60px; line-height: 60px">
          <el-button type="warning">
            <i class="el-icon-user el-icon--left"></i>{{ nameData }}<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button> 
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item>
              <div @click="logout()">退出登录</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
    </el-container>
    
    <el-container>
      <el-aside style="overflow: hidden; min-height: 100vh; background-color: #545c64; width: 250px">
        <el-menu
        :default-active="$route.path" 
        router
        class="el-menu-vertical-demo"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">

        <el-menu-item index="/">
          <i class="el-icon-menu"></i>
          <span slot="title">Home/主页</span>
        </el-menu-item>

        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>Map/地图</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/gps">GPS</el-menu-item>
            <el-menu-item index="/gps1">GPS1</el-menu-item>
          </el-menu-item-group>

        </el-submenu>

        <el-menu-item index="/add">
          <i class="el-icon-setting"></i>
          <span slot="title">Payment/支付</span>
        </el-menu-item>

        <el-menu-item index="/myorder">
          <i class="el-icon-setting"></i>
          <span slot="title">MyOrder/我的订单</span>
        </el-menu-item>
      </el-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from 'axios'
import qs from 'qs'

const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

export default {
  name: 'Layout',

  data () {
    return {
      tokenForm:{
        token: ''
      },
      nameData: ''
    }
  },

  mounted() {
    this.fetchname()
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
            this.nameData = response.data.name;
            console.log(this.nameData);
            //console.log("12345");
          },
          response => {
            console.log("error");
            alert("请求失败");
            this.$router.push("/login")
          }
      );
    },

    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("userid");
      this.$router.push("/login");
    }

  }
}

</script>

<style>
  .el-menu{
    border-right: none !important;
  }
</style>

