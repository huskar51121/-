import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'
import About from '@/views/AboutView.vue'
import Register from '@/views/Register.vue'
import GPS from '@/views/GPS.vue'
import GPS1 from '@/views/GPS1.vue'
import Add from '@/views/Add.vue'
import MyOrder from '@/views/MyOrder.vue'
import Friend from '@/views/Friend.vue'
import Profile from '@/views/Profile.vue'
import Location from '@/views/Location.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },  
  {
    path: '/',
    name: 'layout',
    component: Layout,
    children: [
      {
        path: '',
        name: 'home',
        component: Home
      },
      {
        path: 'about',
        name: 'about',
        component: About
      },
      {
        path: 'gps',
        name: 'gps',
        component: GPS
      },
      {
        path: 'gps1',
        name: 'gps1',
        component: GPS1
      },
      {
        path: 'add',
        name: 'add',
        component: Add
      },
      {
        path: 'myorder',
        name: 'myorder',
        component: MyOrder
      },
      {
        path: 'friend',
        name: 'friend',
        component: Friend
      },
      {
        path: 'profile',
        name: 'profile',
        component: Profile
      },
      {
        path: 'location',
        name: 'location',
        component: Location
      },
    ]
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//路由守卫

router.beforeEach( (to, from, next) => {
    if (to.path === '/login') {
      next();
    }
    if(to.path === '/register') {
      next();
    }
    const user = localStorage.getItem('user');
    if (!user && to.path !== '/login' && to.path !== '/register') {
      return next('/login');
    }
    next();
  })


export default router
