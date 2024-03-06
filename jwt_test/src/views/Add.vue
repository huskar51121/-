<template>
  <div>
    <h1>选择套餐</h1>
    <button @click="selectPackage('一周', 10)">一周 - $10</button>
    <button @click="selectPackage('一月', 36)">一月 - $36</button>
    <button @click="selectPackage('三月', 100)">三月 - $100</button>
    <button @click="selectPackage('一年', 400)">一年 - $400</button>
  </div>
</template>

<script>


import axios from 'axios';
import qs from 'qs'

const ConfirmPaymentRequest = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

export default {
  name: 'Add',
  data() {
    return {
      orderForm: {
        time: '',
        price: '',
        userid: ''
      },
      tokenForm:{
        token: ''
      },
      idData: ''
    }
  },
  created() {

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
            this.idData = response.data.id;
            console.log(this.idData);
            console.log("12345");
          },
          response => {
            console.log("error");
            alert("请求失败");
            this.$router.push("/login")
          }
      );
    },
    selectPackage(name, price) {
      this.orderForm.time = name;
      this.orderForm.price = price;
      this.orderForm.userid = this.idData;
      console.log("this.orderForm.userid");
      console.log(this.orderForm.userid);
      const params = qs.stringify(this.orderForm);
    console.log("this.orderForm:");
      console.log(this.orderForm);
      // 在Vue组件中使用Axios发送POST请求
    console.log("params:");
    console.log(params);
    ConfirmPaymentRequest.post('/order/add', params)
    .then(response => {
      // 请求成功时的处理逻辑
      console.log(response.data); // 打印后端返回的字符串数据
      if (response.status === 200 & response.data === '创建成功') {
        this.$message({
            message: '创建成功',
            type: 'success'
          });
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
