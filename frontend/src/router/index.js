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
import Introduce from '../components/Introduce.vue'
import Search from '../components/Search.vue'

Vue.use(VueRouter)


export default new VueRouter({
  mode: 'history'
  , routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path:'/introduce',
      name:'introduce',
      component: Introduce
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
      // beforeEnter: requireAuth
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
      component: Setting
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
    {
      path: '/search',
      name: 'search',
      component: Search
    }
  ]
})
