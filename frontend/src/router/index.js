import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../components/Home.vue'
import Login from '../components/Login.vue';
import MyPage from '../components/MyPage.vue';
import Register from '../components/Register.vue'
import Post from '../components/Post.vue'
import TextEditor from '../components/TextEditor.vue'
import Setting from '../components/Setting.vue'
import PostDetail from '../components/PostDetail.vue'
import FindID from '../components/user/FindID.vue'

Vue.use(VueRouter)
import store from '../router/index.js'

const requireAuth = () => (from, to, next) => {
  if (store.app.$store.getters.isAuthenticated) {
    return next() // isAuth === true면 페이지 이동
  }
  // 홈 화면으로 제대로 보내지지 않는 문제
  return next('/') // isAuth === false면 다시 로그인 화면으로 이동
}

export default new VueRouter({
  mode: 'history'
  , routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: MyPage,
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/texteditor',
      name : 'texteditor',
      component: TextEditor
    },
    {
      path: '/setting',
      name : 'setting',
      component: Setting,
      beforeEnter: requireAuth()
    },
    {
      path: '/create',
      name: 'create',
      component: Post
    },
    {
      path: '/:username/:title',
      name: 'postdetail',
      component: PostDetail
    },
    {
      path: '/findid',
      name: 'findid',
      component: FindID,
    },
  ]
})
