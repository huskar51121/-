<template>
    <div>
        <el-table  
        :data="tableData" 
        style="width: 100%">
            <el-table-column prop="days" label="Days"></el-table-column>
            <el-table-column prop="id" label="Id"></el-table-column>
            <el-table-column prop="orderid" label="Orderid"></el-table-column>
            <el-table-column prop="payornot" label="Payornot"></el-table-column>
            <el-table-column prop="price" label="Price"></el-table-column>
            <el-table-column prop="purchaseDate" label="PurchaseDate"></el-table-column>
            <el-table-column label="operate">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="success"
                @click="handlePayment(scope.$index, scope.row)"
                v-if='scope.row.payornot==0'
                >Pay</el-button>
              <el-button
                size="mini"
                type="info"
                @click="handleDelete(scope.$index, scope.row)"
                >Delete</el-button>
            </template>
            </el-table-column>
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
const requestInstance3 = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})
const requestInstance4 = axios.create({
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
        orderIdForm:{
          orderId: ''
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
            alert("User information request failed");
            this.$router.push("/login")
          }
      );
    },
    fetchorder() {
        this.idForm.idData = localStorage.getItem("userid");
        const params = qs.stringify(this.idForm);
        console.log(params);
        console.log('this is order');
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
            alert("Order request failed");
          }
      );
    },
    handlePayment(index,row) {
      if(row.payornot === 1){
        this.$message({
          message: 'The order has already been paid',
          type: 'error'
          });
          return;
      }
      console.log(row);
      console.log(row.orderid);
      this.orderIdForm.orderId= row.orderid;
      const params = qs.stringify(this.orderIdForm);
      console.log(params);
          requestInstance3.post('order/pay', params).then(
          response => {
          this.$message({
          message: 'Payment successful ',
          type: 'success'
          });
          this.$router.go(0)
          },
          response => {
            console.log("error");
            alert("Payment request failed")
          }
      );
    },
    handleDelete(index,row) {
      console.log(row);
      console.log(row.orderid);
      this.orderIdForm.orderId= row.orderid;
      const params = qs.stringify(this.orderIdForm);
      console.log(params);
          requestInstance4.post('order/delete', params).then(
          response => {
          this.$message({
          message: 'Delete successful',
          type: 'success'
          });
          this.$router.go(0)
          },
          response => {
            console.log("error");
            alert("Delete request failed")
          }
        )
    }
  }

}
</script>