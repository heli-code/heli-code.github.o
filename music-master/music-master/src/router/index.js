import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
const routes = [
  {
    path: '/',
    name: 'menu',
    component: () => import('../components/menu.vue'),
    children: [
      {
        path: '/',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: '/login',
        name: 'login',
        component: () => import('../views/system/login.vue')
      },
      {
        path: '/register',
        name: 'register',
        component: () => import('../views/system/register.vue')
      },
      {
        path: '/listInfo',
        name: 'listInfo',
        component: () => import('../views/musicList/listInfo.vue')
      },
      {
        path: '/singerList',
        name: 'singerList',
        component: () => import('../views/singer/singerList.vue')
      },
      {
        path: '/singerInfo',
        name: 'singerInfo',
        component: () => import('../views/singer/singerInfo.vue')
      },
      {
        path: '/albumInfo',
        name: 'albumInfo',
        component: () => import('../views/album/albumInfo.vue')
      },
      {
        path: '/musicList',
        name: 'musicList',
        component: () => import('../views/musicList/musicList.vue')
      },
      {
        path: '/putList',
        name: 'putList',
        component: () => import('../views/musicList/putList.vue')
      },
      {
        path: '/userInfo',
        name: 'userInfo',
        component: () => import('../views/userInfo/userInfo.vue')
      },
      {
        path: '/putUser',
        name: 'putUser',
        component: () => import('../views/userInfo/putUser.vue')
      },
      {
        path: '/fans',
        name: 'fans',
        component: () => import('../views/userInfo/fans.vue')
      },
      {
        path: '/songResult',
        name: 'songResult',
        component: () => import('../views/song/songResult.vue')
      },
      {
        path: '/playSong',
        name: 'playSong',
        component: () => import('../views/song/playSong.vue')
      },
      {
        path: '/about',
        name: 'About',
        component: () => import('../views/About.vue')
      }
    ]
  }
]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
