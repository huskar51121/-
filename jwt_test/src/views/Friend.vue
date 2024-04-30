<template>
  <div>
    <!-- 添加好友图标按钮 -->
    <img src="@/assets/logo.png" alt="Add Follow" class="add-friend-icon" @click="showModal = true">←Add Follow

    <!-- 添加好友弹框 -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p>ID：<input type="text" v-model="friendId" placeholder="Enter User ID"></p>
        <button @click="addFriend">Add Follow</button>
      </div>
    </div>

    <!-- 添加成功或失败提示 -->
    <div v-if="showMessage" class="message" :class="{'success-message': isSuccess, 'error-message': !isSuccess}">
      {{ message }}
    </div>
    
    <div>
        <el-table  
        :data="tableData" 
        style="width: 100%">
            <el-table-column prop="upid" label="Follower id"></el-table-column>
            <el-table-column prop="upname" label="Follower name"></el-table-column>
            <el-table-column label="Operate">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="info"
                @click="deleteFriend(scope.$index, scope.row)"
                >Delete Follow</el-button>
            </template>
            </el-table-column>
        </el-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';


// 创建axios的实例
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
});
const requestInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
});
const requestInstance2 = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
});
const requestInstance3 = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 100000
});


// 模拟任何POST到`/addFriend`的请求
// 直接返回字符串 '11111' 作为响应数据


export default {
    name: 'Friend',
  data() {
    return {
        tokenForm:{
            token: ''
        },
        idForm:{
            idData: ''
        },
        upIdForm:{
          userid: '',
          upid: ''
        },
        showModal: false,
        friendId: '',
        showMessage: false,
        isSuccess: false,
        message: '',
        idCheck: '',
        friendData: '',
        tableData: []
    };
  },
  mounted() {
    this.fetchname();
    this.fetchfriend();
  },
  methods: {
    addFriend() {
      if (!this.friendId) {
        this.displayMessage(false, "ID cannot be empty");
        return;
      }
      const params = qs.stringify({userid: this.idCheck, upid: this.friendId});
      // 使用axiosInstance发送请求
      axiosInstance.post('/friend/add', params)
          .then(          
            response => {
            if (response.status === 200 & response.data == '创建成功') {
                this.$message({
                message: 'Created successful',
                type: 'success'
                });
                this.$router.go(0)
            }else {
                this.$message({
                    message: response.data,
                    type: 'error'
                });
            }
          })
          .catch(error => {
            console.log(error);
            this.displayMessage(false, "Adding follow failed");
          });
    },
    closeModal() {
      this.showModal = false;
      this.friendId = ''; // 清空输入框
    },
    displayMessage(isSuccess, message) {
      this.showMessage = true;
      this.isSuccess = isSuccess;
      this.message = message;
      setTimeout(() => {
        this.showMessage = false;
      }, 3000); // 3秒后隐藏提示
    },
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
    fetchfriend() {
        this.idForm.idData = localStorage.getItem("userid");
        const params = qs.stringify(this.idForm);
        console.log(params);
        console.log('this is friend');
        console.log(this.idForm.idData);
        requestInstance2.post('friend/user/myFriend', params).then(
          response => {
            this.friendData = response.data;
            this.tableData = response.data;
            console.log("friendData");
            console.log(this.friendData);
          },
          response => {
            console.log("error");
            alert("Follow acquisition failed");
          }
      );
    },
    deleteFriend(index, row) {
      console.log(row);
      console.log(row.upid);
      this.upIdForm.upid = row.upid;
      this.upIdForm.userid = localStorage.getItem("userid");
      const params = qs.stringify(this.upIdForm);
      console.log(params);
          requestInstance3.post('friend/delete', params).then(
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


<style scoped>
.add-friend-icon {
  cursor: pointer;
  width: 50px; /* 图标大小保持不变 */
  transition: transform 0.3s ease;
}

.add-friend-icon:hover {
  transform: scale(1.1); /* 悬停放大效果保持不变 */
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5); /* 模态背景遮罩保持不变 */
  z-index: 999;
}

.modal-content {
  background: #fff; /* 模态内容背景设置为白色 */
  padding: 20px 40px;
  border-radius: 10px; /* 边角圆润度保持不变 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 盒子阴影效果保持不变 */
  width: auto; /* 宽度自适应 */
  max-width: 400px; /* 最大宽度设置 */
  transition: all 0.3s ease;
  animation: modalFadeIn 0.5s forwards;
}

/* 关闭按钮 */
.close {
  cursor: pointer;
  float: right;
  font-size: 24px;
  color: #000000;
}

input[type="text"], button {
  width: 100%;
  padding: 12px 20px;
  margin-top: 15px;
  border-radius: 20px; /* 边框圆角保持不变 */
  box-sizing: border-box;
  border: 2px solid #ccc;
}


input[type="text"] {
  background-color: rgba(255, 255, 255, 0.7); /* 输入框背景色透明度调整 */
  color: #333; /* 输入框文字颜色保持不变 */
}

button {
  border: none;
  background-color: #ff4081; /* 按钮背景色与好友列表边框颜色相匹配 */
  color: #fff; /* 按钮文字颜色为白色 */
  margin-top: 25px; /* 与输入框的间距保持不变 */
  cursor: pointer; /* 鼠标样式保持不变 */
  transition: background-color 0.3s, color 0.3s; /* 过渡效果保持不变 */
}

button:hover {
  background-color: #6a11cb; /* 按钮悬停时颜色调整，与好友列表渐变起始颜色相匹配 */
  color: #ffffff; /* 按钮悬停时文字颜色保持白色 */
}

.message {
  position: fixed;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
  background-color: #ffffff; /* 消息背景色保持白色 */
  color: #ff4081; /* 消息文字颜色与好友列表边框颜色相匹配 */
  padding: 15px 30px;
  border-radius: 20px; /* 圆角效果保持不变 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 阴影效果保持不变 */
  transition: opacity 0.3s, visibility 0.3s; /* 过渡效果保持不变 */
  animation: messageFadeIn 1s forwards;
}

.success-message {
  border: 1px solid #4CAF50; /* 成功消息边框颜色保持不变 */
}

.error-message {
  border: 1px solid #F44336; /* 错误消息边框颜色保持不变 */
}
</style>
