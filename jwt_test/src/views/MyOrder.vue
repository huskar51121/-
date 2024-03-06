<template>
    <div>
        <el-table  
        :data="tableData" 
        style="width: 100%">
            <el-table-column prop="days" label="days/日期"></el-table-column>
            <el-table-column prop="id" label="id/用户号"></el-table-column>
            <el-table-column prop="orderid" label="orderid/订单号"></el-table-column>
            <el-table-column prop="payornot" label="payornot/是否支付"></el-table-column>
            <el-table-column prop="price" label="price/金额"></el-table-column>
            <el-table-column prop="purchaseDate" label="purchaseDate/交易日期"></el-table-column>
        </el-table>
    </div>
</template>

<script>

import axios from 'axios';
import qs from 'qs'

const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})
const requestInstance2 = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

export default {
  name: 'Myorder',

  data() {
    return {
        tokenForm:{
            token: ''
        },
        idForm:{
            idData: ''
        },
        idCheck: '',
        orderData: '',
        tableData: []
    }
  },
  mounted() {
    this.fetchname();
    this.fetchorder();
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
            alert("请求失败");
            this.$router.push("/login")
          }
      );
    },
    fetchorder() {
        this.idForm.idData = localStorage.getItem("userid");
        const params = qs.stringify(this.idForm);
        console.log(params);
        console.log(this.idForm.idData);
      requestInstance2.post('order/user/myOrder', params).then(
          response => {
            this.orderData = response.data;
            this.tableData = response.data;
            console.log("orderData");
            console.log(this.orderData);
          },
          response => {
            console.log("error");
            alert("请求失败");
          }
      );
    }
  }

}
</script>