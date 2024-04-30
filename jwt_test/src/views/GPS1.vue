<template>
    <div>
        <div id="map"></div>
        <div id="path-length">Path Length: 0 km</div>
        <div>
        <el-table  
        :data="tableData" 
        style="width: 100%">
            <el-table-column prop="tripid" label="Tripid"></el-table-column>
            <el-table-column prop="id" label="Id"></el-table-column>
            <el-table-column prop="starttime" label="Starttime">
                  <template slot-scope="scope">
                      {{ formatTime(scope.row.starttime) }}
                  </template>
            </el-table-column>
            <el-table-column prop="endtime" label="Endtime">
                  <template slot-scope="scope">
                      {{ formatTime(scope.row.endtime) }}
                  </template>
            </el-table-column>
            <el-table-column label="Operate">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="success"
                @click="handleTrip(scope.$index, scope.row)"
                >View</el-button>
            </template>
            </el-table-column>
        </el-table>
    </div>
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

export default {
  name: 'GPS1',

  data() {
    return {
        tokenForm:{
            token: ''
        },
        idForm:{
            idData: ''
        },
        tripIdForm:{
          tripId: ''
        },
        idCheck: '',
        tripData: '',
        gpsData: '',
        tableData: [],
        coordinates : [],
        latlngs: [
            [45.51, -122.68],
            [37.77, -122.43],
            [34.04, -118.2]
        ],
        map: null
    }
  },

  mounted() {
    this.fetchname();
    this.fetchtrip();
    this.map = L.map('map').setView([51.505, -0.09], 13);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '© OpenStreetMap contributors'
        }).addTo(this.map);
        console.log('latlngs');
        console.log(this.latlngs);

        var polyline = L.polyline(this.latlngs, {color: 'red'}).addTo(this.map);
        this.map.fitBounds(polyline.getBounds());

        // 计算路径长度的函数
        function calculatePathLength(latlngs) {
            var totalLength = 0;
            for (var i = 0; i < latlngs.length - 1; i++) {
                var pointA = L.latLng(latlngs[i]);
                var pointB = L.latLng(latlngs[i + 1]);
                var distance = pointA.distanceTo(pointB) / 1000; // 转换为公里
                totalLength += distance;
            }
            return totalLength.toFixed(2); // 保留两位小数
        }

        // 计算并显示路径长度
        var pathLength = calculatePathLength(this.latlngs);
        document.getElementById('path-length').innerHTML = 'Path Length: ' + pathLength + ' km';
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
    fetchtrip() {
        this.idForm.idData = localStorage.getItem("userid");
        const params = qs.stringify(this.idForm);
        console.log(params);
        console.log('this is trip');
        console.log(this.idForm.idData);
      requestInstance2.post('trip/user/myTrip', params).then(
          response => {
            this.tripData = response.data;
            this.tableData = response.data;
            console.log("tripData");
            console.log(this.tripData);
          },
          response => {
            console.log("error");
            alert("GPS request failed");
          }
      );
    },
    handleTrip(index, row){
      console.log(row);
      console.log(row.tripid);
      this.tripIdForm.tripId= row.tripid;
      const params = qs.stringify(this.tripIdForm);
      console.log(params);
          requestInstance3.post('trip/gpsdata', params).then(
          response => {
          this.$message({
          message: 'GPS request succeeded',
          type: 'success'
          });
            this.gpsData = response.data;
            console.log("gpsData");
            console.log(this.gpsData);
            // 创建一个空数组来存放经纬度信息
          this.coordinates = [];
            // 遍历数据数组，提取经纬度信息并存入新数组
            this.gpsData.forEach(obj => {
                let latitude = obj.latitude;
                let longitude = obj.longitude;
                this.coordinates.push([latitude, longitude]);
            });

            // 现在coordinates数组中包含了每个点的经纬度信息
            console.log(this.coordinates);

            this.latlngs = this.coordinates;

            // 清除现有地图上的所有图层
            this.map.eachLayer(function (layer)  {
                this.map.removeLayer(layer);
            }.bind(this));

            // 重新添加底图图层
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap contributors'
            }).addTo(this.map);

            // 重新绘制轨迹
            var polyline = L.polyline(this.latlngs, {color: 'red'}).addTo(this.map);
            this.map.fitBounds(polyline.getBounds());
          },
          response => {
            console.log("error");
            alert("GPS request failed")
          }
      );
    },
        formatTime(time) {
        // 将时间格式从 "2024-04-17T16:19:58" 转换成 "2024-04-17 16:19:58"
        const formattedTime = time.replace('T', ' ');
        return formattedTime;
    },
 }
};
</script>

<style>
    #map { height: 400px; }
    #path-length { padding: 10px; }
</style>