import Vuex from 'vuex'
import Vue from 'vue'
Vue.use(Vuex)


export default new Vuex.Store({
  state: {
      islogin: false,
      token: null,
      pow: null,
  },

  // 准备actions(用于响应组件中的动作,是操作数据之前的操作)
  actions: {

  },

  mutations: {
      LOGIN: function (state, value) {
          console.log("添加token", state, value);
          state.islogin = true;
          state.token = value.token; //修改stata里面的token
          state.pow = value.pow; //修改stata里面的power
      },

      // 删除token
      LOGOUT: function (state) {
        console.log("删除token", state);
        state.islogin = false;
        state.token = null;
        state.pow = null;
      }
  },
})
