<template>
  <div class="sqlRes">
    <div class="header">
        <ul class="navCon">
          <template v-for="item in navList">
            <li @click="changeIndex(item)" :class="{cur: currentIndex == item.index }">{{item.name}}</li>
          </template>
        </ul>
        <div class="user" >
          <div :class="mouseon==true? 'link-item-hover' : 'link-item-hover-remove'"
            @mouseover="mouseon=true" @mouseout="mouseon=false" @click="toLogin" v-if="$store.state.token==null">未登录
          </div>
          <div :class="mouseon==true? 'link-item-hover' : 'link-item-hover-remove'"
            @mouseover="mouseon=true" @mouseout="mouseon=false" v-if="$store.state.token!=null">
            <div>wbq</div>
            <div class="logout" @click="logout">注销</div>
          </div>
        </div>
    </div>
    <div class="resBody">
      <router-view/>
    </div>
    <div></div>
  </div>
</template>

<script>

import throttle from "lodash/throttle"
import axios from 'axios';

export default {
  name: 'index',
  data () {
    return {
     currentIndex: 0,
     navList: [
      {
        index: 0,
        name: "首页",
        route: 'home',
        route: '/home'
      },
      {
        index: 1,
        name: "接口生成模块",
        route: '/api'
      },
      {
        index: 2,
        name: "算法发布",
        route: '/algopublic'
      },
      {
        index: 3,
        name: "算法应用",
        route: '/algos'
      },
      {
        index: 4,
        name: "数据源模块",
        route: '/ds'
      },
      {
        index: 5,
        name: "接口管理模块",
        route: '/management'
      },
     ],
     mouseon: false,
    }
  },
  mounted() {
    this.checkLoginState();
  },
  created() {
    console.log(this.$route.path)
    console.log(this.navList)
    let path = this.$route.path
    this.navList.forEach(item => {
      if(path.indexOf(item.route) >= 0) {
        this.currentIndex = item.index;
      }
    })
  },
  methods: {
    changeIndex: throttle(function(item) {
      this.currentIndex = item.index;
      this.$router.push(item.route)
    }, 50),
    checkLoginState() {
      if (sessionStorage.getItem("token") != null && sessionStorage.getItem("pow") != null) {
        let res = {
          token: sessionStorage.getItem("token"),
          pow: sessionStorage.getItem("pow"),
        };
        this.$store.commit("LOGIN", res);
      }
      return this.$store.state.islogin;
    },
    toLogin() {
    	this.$router.push("/login");
    },
    logout() {
      const instance = axios.create({
        headers: {
          'Authorization': this.$store.state.token,
          'token': this.$store.state.token
        }
      });
      instance.get('http://60.245.208.50:9001/user/logout').then(response => {
        if (response.data.code == 20000) {
          this.$store.commit("LOGOUT");
          sessionStorage.clear();
          this.$message({
            showClose: true,
            message: '登出成功',
            type: 'success'
          });
          this.$forceUpdate();
        }
      });
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.sqlRes {
    position: absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    bottom: 0px;
}

.header {
  height: 40px;
  width: 100%;
  display: flex;
  background-color: white;
  box-shadow: 1px 1px 1px #d5d5d5;
}

.navCon {
  list-style: none;
  display: flex;
  height: 100%;
  line-height: 40px;
  justify-content: center;
  margin: 0px;
  width: 90%;
}

.navCon li {
  margin-left: 20px;
}

.navCon li:hover {
  cursor: pointer;
}

.user{
  width: 8%;
  list-style: none;
  display: flex;
  height: 100%;
  line-height: 40px;
  justify-content: center;
}

.link-item-hover {
  display: flex;
  width: 100%;
  color: #92c5f3;
  cursor: pointer;
}
.link-item-hover-remove {
  display: flex;
  width: 100%;
  color: #b8b8b8;
}

.logout{
  margin-left: 20%;
}

.cur {
  border-bottom: 2px solid #f78115;
}

.resBody {
  position: fixed;
  top: 41px;
  left: 0px;
  right: 0px;
  bottom: 0px;
  background: rgb(248, 248, 248);
}
</style>
