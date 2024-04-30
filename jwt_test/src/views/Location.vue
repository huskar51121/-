<template>
  <div>
    <h1>Movement Real-time position</h1>
    <!-- 获取用户位置并发送的按钮 -->
    <button class="location-button" @click="getUserLocation">Start Moving</button>
    <button class="location-button" @click="stopTracking">Pause Moving</button>
    <button class="location-button" @click="endTracking">End Moving</button>
    <button class="location-button" @click="refreshCoinPosition">Refresh Coin Position</button>
    <!-- 地图展示 -->
    <div id="map" style="height: 400px; margin-top: 20px;"></div>
    <!-- 用户位置信息显示 -->
    <div v-if="location" class="location-info">
      <div>
        <label>Latitude: </label>
        <span>{{ location.latitude.toFixed(6) }}</span>
      </div>
      <div>
        <label>Longtitude: </label>
        <span>{{ location.longitude.toFixed(6) }}</span>
      </div>
      <div>
        <label>Decimal degree (Dd): </label>
        <span>{{ location.latitude.toFixed(6) }}, {{ location.longitude.toFixed(6) }}</span>
      </div>
      <div>
        <label>Degrees minute second (Dms): </label>
        <!-- 显示转换后的度分秒格式 -->
        <span>{{ convertToDMS(location.latitude, true) }}, {{ convertToDMS(location.longitude, false) }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import qs from 'qs'

const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
})

export default {
  data() {
    return {
      map: null, // 地图实例
      marker: null, // 地图标记
      location: null, // 用户的位置信息
      currentRouteList: [],
      currentRoute: [],
      coins: [],  // 存放硬币的数组
      coinRange: 1000,  // 硬币放置的最大范围（米）
      maxCoinDistance: 1500,  // 硬币的最大距离（米）
       intervalId: null,
       startTime: null,
       endTime: null,
    };
  },
  mounted() {
    // 组件挂载完成后初始化地图
    this.map = L.map('map').setView([0, 0], 1);
    // 添加地图瓦片层
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '版权 &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(this.map);
  },
  methods: {
    getUserLocation() {
        // 清除之前的定时器
        clearInterval(this.intervalId);
        // 存储开始时间，仅当localStorage中没有startTime时才存储
        if (!localStorage.getItem('startTime')) {
            this.startTime = new Date().toLocaleString();
            localStorage.setItem('startTime', this.startTime);
        }
            // 获取用户地理位置的函数
                if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    position => {
                    // 成功获取位置
                    const lat = position.coords.latitude;
                    const lng = position.coords.longitude;
                    // 设置位置信息
                    this.location = {latitude: lat, longitude: lng};

                    // 更新地图标记
                    if (this.marker) {
                        this.marker.setLatLng([lat, lng]);
                    } else {
                        this.marker = L.marker([lat, lng], {icon: this.getRedIcon()}).addTo(this.map);
                    }
                    // 设置地图视角
                    this.map.setView([lat, lng], 13);

                    this.checkCoinDistance();  // 检查硬币的距离，确保没有超出设定的最大距离

                    if (!this.coins.length) {
                      this.placeRandomCoin({latitude: lat, longitude: lng});  // 放置一个随机硬币
                    };
                    
                    this.checkForCoins();  // 检查附近是否有硬币

                    // 更新路径列表
                        this.currentRouteList.push([JSON.stringify({latitude: lat, longitude: lng})]);
                        console.log(this.currentRouteList);
                        this.currentRoute.push({latitude: lat, longitude: lng});
                    // 存储路径列表到前端本地存储
                    this.sendLocationToLocalStorage(this.currentRouteList);
                    },
                    error => {
                    // 获取位置失败
                    console.error("错误代码 = " + error.code + " - " + error.message);
                    }
                );
            }
            // 开始定时器，每五秒获取一次位置
            this.intervalId = setInterval(() =>{
                // 获取用户地理位置的函数
                if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    position => {
                    // 成功获取位置
                    const lat = position.coords.latitude;
                    const lng = position.coords.longitude;
                    // 设置位置信息
                    this.location = {latitude: lat, longitude: lng};

                    // 更新地图标记
                    if (this.marker) {
                        this.marker.setLatLng([lat, lng]);
                    } else {
                        this.marker = L.marker([lat, lng], {icon: this.getRedIcon()}).addTo(this.map);
                    }
                    // 设置地图视角
                    this.map.setView([lat, lng], 13);

                    this.checkForCoins();  // 检查附近是否有硬币

                    // 更新路径列表
                        this.currentRouteList.push([JSON.stringify({latitude: lat, longitude: lng})]);
                        this.currentRoute.push({latitude: lat, longitude: lng});
                        console.log(this.currentRouteList);

                    // 绘制路径
                    this.drawRoute(this.currentRoute);

                    // 存储路径列表到前端本地存储
                    this.sendLocationToLocalStorage(this.currentRouteList);
                    },
                    error => {
                    // 获取位置失败
                    console.error("错误代码 = " + error.code + " - " + error.message);
                    }
                );
            }
        }, 5000);
    },
    // 绘制路径方法
    drawRoute(currentRoute) {
        // 清除之前的路径
        if (this.routeLayer) {
            this.map.removeLayer(this.routeLayer);
        }

        // 创建路径图层
        this.routeLayer = L.polyline(currentRoute.map(point => [point.latitude, point.longitude]), {color: 'blue'}).addTo(this.map);
    },
    sendLocationToLocalStorage(currentRouteList) {
// 将更新的位置列表转换为JSON对象后存储到本地存储
    const jsonRouteList = currentRouteList.map(location => {
        return JSON.parse(location);;
    });
    localStorage.setItem('currentRoute', JSON.stringify(jsonRouteList));

    },
    stopTracking() {
        // 停止定时器
        clearInterval(this.intervalId);
    },
    endTracking() {
        // 停止定时器
        clearInterval(this.intervalId);
        // 存储结束时间
        this.endTime = new Date().toLocaleString();
        localStorage.setItem('endTime', this.endTime);

        // 向后台提交数据
        const payload = {
            id: localStorage.getItem('userid'),
            starttime: localStorage.getItem('startTime'),
            endtime: localStorage.getItem('endTime'),
            gpslist: localStorage.getItem('currentRoute')
        };
        const params = qs.stringify(payload);
            requestInstance.post('/trip/add', params)
            .then(response => {
              // 请求成功时的处理逻辑
              console.log(response.data); // 打印后端返回的字符串数据
                  this.$message({
                      message: 'Submit succeeded',
                      type: 'success'
                  });
            },response => {
                this.$message({
                    message: response.data,
                    type: 'error'
                });
              }
            )
        // 清除本地存储
        localStorage.removeItem('currentRoute');
        localStorage.removeItem('startTime');
        localStorage.removeItem('endTime');
    },
    placeRandomCoin(center) {
      console.log("Received center:", center);  // 打印接收到的中心坐标

      if (!center.latitude || !center.longitude) {
        console.error("Invalid center coordinates:", center);
        return;  // 如果坐标无效，则直接返回
      }

      const edge = this.coinRange / 111000;  // 换算经纬度
      let lat = center.latitude + (Math.random() - 0.5) * edge;  // 计算新的纬度
      let lng = center.longitude + (Math.random() - 0.5) * edge;  // 计算新的经度

      // 确保纬度和经度在有效范围内
      lat = Math.max(Math.min(lat, 90), -90);
      lng = Math.max(Math.min(lng, 180), -180);

      if (!isNaN(lat) && !isNaN(lng)) {
        const coin = {lat, lng, id: Date.now()};  // 创建硬币对象
        this.coins.push(coin);  // 将硬币添加到数组中
        L.marker([lat, lng], {icon: this.getCoinIcon()}).addTo(this.map).bindPopup("硬币!");  // 在地图上标记硬币位置并添加弹出窗口
        console.log("Coins placed in:", lat, lng);  // 打印硬币的位置
      } else {
        console.error("The calculated position of the coin is not valid:", lat, lng);
      }
    },
    checkForCoins() {
      const userLatLng = L.latLng(this.location.latitude, this.location.longitude);  // 创建用户当前位置的LatLng对象
      this.coins.forEach(coin => {
        const coinLatLng = L.latLng(coin.lat, coin.lng);  // 创建硬币位置的LatLng对象
        if (userLatLng.distanceTo(coinLatLng) < 10) {  // 如果用户与硬币的距离小于10米，则收集硬币
          this.collectCoin(coin.id);
        }
      });
    },
    checkCoinDistance() {
      const userLatLng = L.latLng(this.location.latitude, this.location.longitude);  // 创建用户当前位置的LatLng对象
      this.coins = this.coins.filter(coin => {
        const coinLatLng = L.latLng(coin.lat, coin.lng);  // 创建硬币位置的LatLng对象
        return userLatLng.distanceTo(coinLatLng) <= this.maxCoinDistance;  // 确保所有硬币都在最大距离范围内
      });
      if (this.coins.length === 0) {
        this.placeRandomCoin(this.location);  // 如果所有硬币都超出范围，则在当前位置附近重新放置一个硬币
      }
    },
    collectCoin(coinId) {
      this.coins = this.coins.filter(coin => coin.id !== coinId);  // 从数组中移除已收集的硬币
      axios.post('/api/collect-coin', {coinId});  // 向服务器发送收集硬币的请求
    },
    convertToDMS(deg, isLat) {
      // 将十进制度转换为度分秒的函数
      const absolute = Math.abs(deg);
      const degrees = Math.floor(absolute);
      const minutesNotTruncated = (absolute - degrees) * 60;
      const minutes = Math.floor(minutesNotTruncated);
      const seconds = ((minutesNotTruncated - minutes) * 60).toFixed(2);

      // 根据经纬度方向确定标识
      const direction = deg < 0
          ? isLat ? 'S' : 'W'
          : isLat ? 'N' : 'E';

      return `${degrees}°${minutes}'${seconds}" ${direction}`;
    },
    getRedIcon() {
      // return红色的坐标图标
      return L.icon({
        iconUrl: 'https://cdn-icons-png.freepik.com/256/684/684908.png?ga=GA1.1.2024108217.1697683229&semt=ais_hybrid',
        iconSize: [24, 24],
        iconAnchor: [12, 12],
        popupAnchor: [-3, -6]
      });
    },
    getCoinIcon() {
      return L.icon({
        iconUrl: 'https://cdn-icons-png.freepik.com/256/10275/10275028.png?ga=GA1.1.2024108217.1697683229&semt=ais_hybrid',
        iconSize: [25, 25],
        iconAnchor: [12.5, 12.5]
      });
    },
    refreshCoinPosition() {
      if (!this.location) {
        console.error("The user location has not been obtained.");
        return;
      }

      // 移除当前所有硬币
      this.coins.forEach(coin => {
        this.map.eachLayer(layer => {
          if (layer instanceof L.Marker && layer.getLatLng().equals(L.latLng(coin.lat, coin.lng))) {
            this.map.removeLayer(layer);
          }
        });
      });
      this.coins = [];

      // 根据原硬币位置重新放置硬币，确保新硬币与用户位置保持合适的距离
      this.placeRandomCoin(this.location);
    },
  }
};
</script>

<style>
.location-info div {
  margin: 10px 0;
}

.location-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.location-info label {
  font-weight: bold;
}

.location-info span {
  margin-left: 8px;
}

.location-button {
  background: linear-gradient(135deg, #ff758c 0%, #ff7eb3 100%);
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  transition: all 0.4s ease;
  cursor: pointer;
  border-radius: 50px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.location-button:hover {
  background: linear-gradient(135deg, #ff758c 0%, #ff5189 100%);
  box-shadow: 0 5px 15px rgba(255, 117, 140, 0.4);
  transform: translateY(-2px);
}


</style>
