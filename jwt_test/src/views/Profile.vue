<template>
  <div class="profile-container">
    <div class="profile-card">
      <img :src="profileCheck.avatarurl" alt="Profile Avatar" class="profile-avatar">  <!-- 头像 -->
      <h1>{{ profileCheck.name }}</h1>  <!-- 昵称 -->
      <p class="tagline">{{ profileCheck.tagline }}</p>  <!-- 个性签名 -->
      <!-- 按钮 -->
      <div class="profile-buttons">
        <button @click="showEditModal = true">Edit Profile</button>
        <button @click="showPasswordModal = true">Change Password</button>
        <button @click="upgradeSubscription()">Upgrade Subscription</button>
        <button @click="goFriend()">Friend List</button>
      </div>
      <!-- 个人资料 -->
      <div class="profile-info">
        <p>ID: {{ profileCheck.id }}</p>
        <p>Membership deadline: {{ profileCheck.enddate }}</p>
        <p>Gold: {{ profileCheck.gold }}</p>
      </div>
    </div>





    <!-- 编辑资料模态框 -->
    <transition name="modal">
      <div class="modal-overlay" v-if="showEditModal" @click.self="closeEditModal">
        <div class="modal-content">
          <span class="close" @click="closeEditModal">&times;</span>
          <h2>Edit Profile</h2>
          <!-- 编辑表单 -->
          <form @submit.prevent="updateProfile">
            <div class="form-group">
              <label for="nickname">Name:</label>
              <input id="nickname" v-model="editForm.nickname" placeholder="Please enter name" required>
            </div>
            <div class="form-group">
              <label for="tagline">Tagline:</label>
              <input id="tagline" v-model="editForm.tagline" placeholder="Please enter tagline" required>
            </div>
            <!-- 头像上传 -->
            <div class="form-group">
              <label for="avatarUrl">Avatar URL:</label>
              <input type="text" id="avatarUrl" v-model="editForm.avatarUrl" placeholder="Please enter Avatar URL" required>
            </div>
            <button class="modal-button" type="submit">Save changes</button>
          </form>
        </div>
      </div>
    </transition>


    <!-- 修改密码模态框 -->
    <transition name="modal">
      <div class="modal-overlay" v-if="showPasswordModal" @click.self="closePasswordModal">
        <div class="modal-content">
          <span class="close" @click="closePasswordModal">&times;</span>
          <h2>Change Password</h2>
          <!-- 修改密码表单 -->
          <form @submit.prevent="changePassword">
            <div class="form-group">
              <label for="currentPassword">Current password:</label>
              <input type="password" id="currentPassword" v-model="passwordForm.currentPassword"
                     placeholder="Please enter current password" required>
            </div>
            <div class="form-group">
              <label for="newPassword">New password:</label>
              <input type="password" id="newPassword" v-model="passwordForm.newPassword" placeholder="Please enter new password"
                     required>
            </div>
            <div class="form-group">
              <label for="confirmPassword">Confirm password:</label>
              <input type="password" id="confirmPassword" v-model="passwordForm.confirmPassword"
                     placeholder="Please enter new password" required>
            </div>
            <button class="modal-button" type="submit">Save changes</button>
          </form>
        </div>
      </div>
    </transition>
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
  name: 'Profile',
  data() {
    return {
      loggedIn: false,
      userProfile: {
        id: '',
        nickname: '',
        tagline: '',
        avatar: '',
        expiryDate: '',
        subscriptionPlan: ''
      },
      showEditModal: false,
      showPasswordModal: false,
      editForm: {
        nickname: '',
        tagline: '',
        avatarUrl: ''
      },
      passwordForm: {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      tokenForm:{
        token: ''
      },
      profileCheck: '',
    }
  },
  mounted() {
    this.fetchname();
    this.fetchProfile();

  },
  created() {

  },
  methods: {
    //填充预表单
    fillProfile(profileCheck) {
            this.editForm.nickname = profileCheck.name,
            this.editForm.tagline = profileCheck.tagline,
            this.editForm.avatarUrl = profileCheck.avatarurl,
            this.passwordForm.currentPassword = profileCheck.password,
            console.log(this.editForm);
            console.log(this.passwordForm);
    },
    //获取用户名
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
    //获取用户资料
    fetchProfile() {
      this.tokenForm.token = localStorage.getItem("user");
      const params = qs.stringify(this.tokenForm);
      console.log(this.tokenForm.token);
      console.log(params);
      //console.log("12345");
      requestInstance.post('/userrequest/afterlogin', params).then(
          response => {
            this.profileCheck = response.data;
            console.log("USER ID IS");
            console.log(this.profileCheck);
            this.fillProfile(this.profileCheck);
          },
          response => {
            console.log("error");
            alert("User information request failed");
            this.$router.push("/login")
          }
      );
    },
    // 关闭编辑资料的模态框
    closeEditModal() {
      this.showEditModal = false;
      document.body.classList.remove('body-modal-open');
    },
    // 关闭修改密码的模态框
    closePasswordModal() {
      this.showPasswordModal = false;
      document.body.classList.remove('body-modal-open');
    },
    //检查登陆状态
/*    checkLoginStatus() {
      mockAxios('http://your-backend-url/api/check-login')
          .then(response => {
            this.loggedIn = response.data.loggedIn;
            if (this.loggedIn) {
              this.fetchUserProfile();
            }
          })
          .catch(error => {
            console.error("Login status check failed:", error);
            this.loggedIn = false;
          });
    },
    */
    //  获得用户资料
    /*
    fetchUserProfile() {
      mockAxios('http://your-backend-url/api/user-profile')
          .then(response => {
            this.userProfile = response.data;
            this.editForm.nickname = this.userProfile.nickname;
            this.editForm.tagline = this.userProfile.tagline;
            this.editForm.avatarUrl = this.userProfile.avatar; // 设置头像URL
          })
          .catch(error => {
            console.error("Failed to fetch user profile:", error);
          });
    },
    */
    // 编辑个人资料的逻辑（包括了上传头像的url）
    updateProfile() {
      const payload = {
        token: localStorage.getItem("user"),
        name: this.editForm.nickname,
        tagline: this.editForm.tagline,
        avatarurl: this.editForm.avatarUrl  // 使用URL而不是文件
      };
      const params = qs.stringify(payload);
      requestInstance.post('/userrequest/changenta', params)
          .then(response => {
            this.profileCheck.name = this.editForm.nickname;
            this.profileCheck.tagline = this.editForm.tagline;
            this.profileCheck.avatarurl = this.editForm.avatarUrl;  // 直接使用提交的URL
            if(response.data != "该用户已存在") {
              localStorage.setItem("user", response.data);
              alert('资料更新成功！');
              this.$router.go(0);
            }else {
              alert('该用户已存在！');
              this.$router.go(0);
            }
          })
          .catch(error => {
            console.error("Profile update failed:", error);
            alert('资料更新失败！');
          })
          .finally(() => {
            this.showEditModal = false;
          });
    },
    // 修改密码
    changePassword() {
      if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
        alert("New passwords do not match.");
        return;
      }
      if (this.passwordForm.newPassword === '') {
        this.$message({
              message: 'The username cannot be empty',
              type: 'error'
            });
        return;
      }
      if (this.passwordForm.confirmPassword === '') {
        this.$message({
              message: 'Password cannot be empty',
              type: 'error'
            });
        return;
      }      
      const pwdform = {
        token: localStorage.getItem("user"),
        lastpassword: this.passwordForm.currentPassword,
        newpassword: this.passwordForm.newPassword,
      }
      const params = qs.stringify(pwdform);
      requestInstance.post('/userrequest/changepassword', params)
          .then(response => {
            alert(response.data);
            this.closePasswordModal();
          })
          .catch(error => {
            console.error("Password change failed:", error);
            alert(error.message);
          });
    },
    // 升级套餐，这里我的设想是转到订阅套餐界面
    upgradeSubscription() {
      if (this.$route.path !== '/add') {
              this.$router.push("/add");
            }
    },
    // 这里是要转到好友列表
     goFriend() {
      if (this.$route.path !== '/friend') {
        this.$router.push("/friend");
      }
    }
  }
}
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow-x: hidden; /* 防止水平滚动条 */
}


.profile-container {
  display: flex; /* 使用弹性盒模型 */
  flex-direction: column; /* 子元素垂直排列 */
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
  min-height: 100vh; /* 至少占满整个视口的高度 */
  width: 100%; /* 占满整个视口的宽度 */
  background-image: url('@/assets/landscape-78058_1920.jpg');
  background-size: cover;
  background-position: center;
}

.profile-card {
  width: 50%; /* 根据需要设置卡片宽度 */
  min-height: 50vh; /* 根据需要设置卡片最小高度 */
  color: #fff;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.3);
  text-align: center;
}

.profile-avatar {
  width: 150px; /* Fixed width for the avatar */
  height: 150px; /* Fixed height for the avatar */
  object-fit: cover; /* Ensure the image covers the area */
  border-radius: 50%; /* Circle shape */
  border: 3px solid #fff; /* White border */
  margin-bottom: 1rem;
}

.profile-info p, .profile-buttons button {
  margin: 0.5rem 0;
}

.profile-buttons button {
  background: #ffc107; /* 主操作按钮颜色 */
  color: #333; /* 按钮文本颜色，确保对比度足够 */
  border: none;
  padding: 0.8rem 1.2rem; /* 增加内边距以适应点击 */
  margin: 0.5rem; /* 增加按钮之间的间距 */
  border-radius: 0.3rem; /* 根据品牌形象选择合适的圆角大小 */
  cursor: pointer;
  transition: background-color 0.3s ease; /* 平滑过渡动画效果 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 轻微的阴影效果提升层次感 */
}

.profile-buttons button:hover {
  background: #e0a800; /* 鼠标悬停时的背景颜色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 悬停时增加阴影 */
}

.profile-buttons button:not(:last-child) {
  margin-right: 1rem; /* 除了最后一个按钮外，其他按钮右侧添加间距 */
}

.login-prompt {
  text-align: center;
  color: #fff;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(50, 50, 50, 0.8); /* 更深的覆盖，更专注于模态框 */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #f8f9fa; /* 浅灰色背景 */
  border: 1px solid #dee2e6; /* 边框以匹配输入字段 */
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  max-width: 480px;
  width: calc(100% - 40px);
  transition: transform 0.3s ease-out;
}

.modal-content:target {
  transform: scale(1.05); /* 当模态打开时，有轻微的变焦效果 */
}

.close {
  position: absolute;
  top: 35px;
  right: 35px;
  font-size: 3.5rem;
  font-weight: bold;
  color: #6c757d;
  background: transparent;
  border: none;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: #000;
  transform: rotate(90deg); /* 悬停时的交互旋转效果 */
}

.form-group label {
  margin-bottom: .75rem;
  font-size: 1rem;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: .5rem .75rem;
  font-size: 1rem;
  line-height: 1.5;
  color: #495057;
  background-color: #fff;
  border: 1px solid #ced4da;
  border-radius: .25rem;
  transition: all 0.2s;
}

.form-group input:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.modal-button {
  font-weight: 400;
  text-align: center;
  vertical-align: middle;
  user-select: none;
  background-color: #007bff; /* Use Bootstrap primary color for consistency */
  border: none; /* Remove border */
  padding: .5rem 1rem; /* Optimal padding for buttons */
  font-size: 1rem;
  line-height: 1.5;
  border-radius: .25rem;
  transition: all 0.2s; /* Smoother transitions for all properties */
  display: block; /* Full-width button */
  width: 100%; /* Full-width button */
  color: white; /* Text color for buttons */
  margin-top: 1rem; /* Space above the button */
}

.modal-button:hover {
  background-color: #0056b3; /* Darker shade on hover */
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1); /* Subtle shadow on hover */
}

@media (max-width: 767px) {
  .modal-content {
    padding: 20px; /* Less padding on smaller screens */
    width: calc(100% - 20px); /* Full width minus padding on smaller screens */
  }
}

@media (min-width: 768px) {
  .profile-container {
    flex-direction: row;
    justify-content: space-evenly;
  }

  .profile-card {
    padding: 40px; /* More padding on larger screens */
  }

  .profile-avatar {
    width: 200px; /* Larger avatar on larger screens */
    height: 200px; /* Larger avatar on larger screens */
  }

  p {
    margin-bottom: 1.5rem; /* More space between text elements on larger screens */
  }
}
</style>


<style scoped>
body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow-x: hidden; /* Prevent horizontal scrollbar */
}


.profile-container {
  display: flex; /* 使用弹性盒模型 */
  flex-direction: column; /* 子元素垂直排列 */
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
  min-height: 100vh; /* 至少占满整个视口的高度 */
  width: 100%; /* 占满整个视口的宽度 */
  background-image: url('@/assets/landscape-78058_1920.jpg');
  background-size: cover;
  background-position: center;
}

.profile-card {
  width: 50%; /* 根据需要设置卡片宽度 */
  min-height: 50vh; /* 根据需要设置卡片最小高度 */
  color: #fff;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.3);
  text-align: center;
}

.profile-avatar {
  width: 150px; /* Fixed width for the avatar */
  height: 150px; /* Fixed height for the avatar */
  object-fit: cover; /* Ensure the image covers the area */
  border-radius: 50%; /* Circle shape */
  border: 3px solid #fff; /* White border */
  margin-bottom: 1rem;
}

.profile-info p, .profile-buttons button {
  margin: 0.5rem 0;
}

.profile-buttons button {
  background: #ffc107; /* 主操作按钮颜色 */
  color: #333; /* 按钮文本颜色，确保对比度足够 */
  border: none;
  padding: 0.8rem 1.2rem; /* 增加内边距以适应点击 */
  margin: 0.5rem; /* 增加按钮之间的间距 */
  border-radius: 0.3rem; /* 根据品牌形象选择合适的圆角大小 */
  cursor: pointer;
  transition: background-color 0.3s ease; /* 平滑过渡动画效果 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 轻微的阴影效果提升层次感 */
}

.profile-buttons button:hover {
  background: #e0a800; /* 鼠标悬停时的背景颜色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 悬停时增加阴影 */
}

.profile-buttons button:not(:last-child) {
  margin-right: 1rem; /* 除了最后一个按钮外，其他按钮右侧添加间距 */
}

.login-prompt {
  text-align: center;
  color: #fff;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(50, 50, 50, 0.8); /* Darker overlay for more focus on modal */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #f8f9fa; /* Light grey background */
  border: 1px solid #dee2e6; /* Border to match input fields */
  padding: 40px; /* More padding for aesthetic spacing */
  border-radius: 8px; /* Softer rounded corners */
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2); /* Smoother box-shadow */
  max-width: 480px; /* Optimal width for form modal */
  width: calc(100% - 40px); /* Responsive with padding on mobile */
  transition: transform 0.3s ease-out; /* Smooth open/close transitions */
}

.modal-content:target {
  transform: scale(1.05); /* Slight zoom effect when modal opens */
}

.close {
  position: absolute;
  top: 35px; /* Position closer to the corner */
  right: 35px; /* Position closer to the corner */
  font-size: 3.5rem;
  font-weight: bold;
  color: #6c757d; /* Softer color for close button */
  background: transparent;
  border: none;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: #000;
  transform: rotate(90deg); /* Interactive rotate effect on hover */
}

.form-group label {
  margin-bottom: .75rem; /* Increase spacing for better readability */
  font-size: 1rem;
  font-weight: 500; /* Slightly bolder labels for better focus */
}

.form-group input {
  width: 100%;
  padding: .5rem .75rem; /* Adjust padding for inputs */
  font-size: 1rem;
  line-height: 1.5;
  color: #495057;
  background-color: #fff;
  border: 1px solid #ced4da;
  border-radius: .25rem;
  transition: all 0.2s; /* Smoother transitions for all properties */
}

.form-group input:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.modal-button {
  font-weight: 400;
  text-align: center;
  vertical-align: middle;
  user-select: none;
  background-color: #007bff; /* Use Bootstrap primary color for consistency */
  border: none; /* Remove border */
  padding: .5rem 1rem; /* Optimal padding for buttons */
  font-size: 1rem;
  line-height: 1.5;
  border-radius: .25rem;
  transition: all 0.2s; /* Smoother transitions for all properties */
  display: block; /* Full-width button */
  width: 100%; /* Full-width button */
  color: white; /* Text color for buttons */
  margin-top: 1rem; /* Space above the button */
}

.modal-button:hover {
  background-color: #0056b3; /* Darker shade on hover */
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1); /* Subtle shadow on hover */
}

@media (max-width: 767px) {
  .modal-content {
    padding: 20px; /* Less padding on smaller screens */
    width: calc(100% - 20px); /* Full width minus padding on smaller screens */
  }
}

@media (min-width: 768px) {
  .profile-container {
    flex-direction: row;
    justify-content: space-evenly;
  }

  .profile-card {
    padding: 40px; /* More padding on larger screens */
  }

  .profile-avatar {
    width: 200px; /* Larger avatar on larger screens */
    height: 200px; /* Larger avatar on larger screens */
  }

  p {
    margin-bottom: 1.5rem; /* More space between text elements on larger screens */
  }
}
</style>


