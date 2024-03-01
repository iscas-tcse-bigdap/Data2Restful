<template>
  <div class="public">
    <div class="bo">
        <div class="left">
          <div id="left_header">接口管理模块</div>
            <el-menu :style="{ 'text-align':'left', 'padding-left':'8px', 'background-color':'#f8f8f8'}"
                background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
                default-active="1" @select="selectM">
                <template v-for="item in menus">
                    <el-menu-item :index="item.idx.toString()">
                        <i :class="item.icon"></i>
                        <span slot="title">{{item.title}}</span>
                    </el-menu-item>
                </template>
            </el-menu>
        </div>
        <div class="right">
            <div class="center">
              <component :is="actionMenuEditor" />
            </div>
        </div>
    </div>
  </div>
</template>

<script>
import emptyPage from "@/components/empty.vue"
import svcManagement from "./svcManagement.vue"

export default {
  name: 'svcManagementIndex',
  components:{
    "emptyPage": emptyPage,
    "svcManagement": svcManagement,
  },
  data () {
    return {
        actionMenuEditor: "svcManagement",
        menus: [
            {
                idx: 0,
                title: "仪表概览",
                icon: "el-icon-menu",
                editor:"emptyPage",
                action: ""
            },
            {
                idx: 1,
                title: "服务管理",
                icon: "el-icon-cloudy",
                editor:"svcManagement",
                action: ""
            },
            {
                idx: 2,
                title: "服务xx",
                icon: "el-icon-setting",
                editor:"emptyPage",
                action: ""
            },
            {
                idx: 3,
                title: "服务xxx",
                icon: "el-icon-document",
                editor:"emptyPage",
                action: ""
            },
        ],

    }
  },
  methods: {
    selectM: function(e) {
        this.actionMenuEditor = this.menus.find(item => {
            return item.idx == e;
        }).editor;
    }
  }
}
</script>

<style scoped>
.public {
    width: 100%;
    height: 100%;
}

.bo {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
}

.left {
    width: 240px;
    height: 100%;
    box-shadow: 2px 1px 2px #d3d3d3;
    padding: 21px 0px 0px 0px;
    background-color: #545c64;
}

.right {
    height: 100%;
    flex: 1;
}

#left_header {
  font-size: 28px;
  font-weight: 10;
  font-family: 'Times New Roman', Times, serif;
  color: #ffffff;
  margin-bottom: 20px;
  margin-left: -18%;
  margin-top: -5%;
}
</style>
