<template>
  <div class="all">
    <el-card class="box-card">
      <div style="display: flex; font-size: 30px;  margin-top: -0.5%; margin-bottom: 1%;">
        <div style="font-size: 34px;">服务</div>
        <div style="font-weight: 600; margin-left: 5px; line-height: 47px;">{{svcInfo.name}}</div>
      </div>
      <el-breadcrumb separator="/" style="font-size: 16px;">
        <el-breadcrumb-item :to="{ path: '/management' }">服务</el-breadcrumb-item>
        <el-breadcrumb-item>{{curPage}}</el-breadcrumb-item>
      </el-breadcrumb>
    </el-card>
    <div class="body-block">
        <div class="left">
            <el-menu class="menu" :style="{'text-align':'left', 'padding-left':'8px', 'background-color':'#f8f8f8'}"
                active-text-color="#fff" default-active="0" @select="selectM" aria-activedescendant="#ffd04b">
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
import serviceDetail from "./serviceDetail.vue"
import serviceRouter from "./serviceRouter.vue"
import servicePlugin from "./servicePlugin.vue"

export default {
  name: 'apiManagementIndex',
  components:{
    "emptyPage": emptyPage,
    "serviceDetail": serviceDetail,
    "serviceRouter": serviceRouter,
    "servicePlugin": servicePlugin,
  },
  created() {
    this.groupId = this.$route.params.svcId;
    this.getSvcOutline();
  },
  data () {
    return {
        actionMenuEditor: "serviceDetail",
        curPage: "服务详情",
        menus: [
            {
                idx: 0,
                title: "服务详情",
                icon: "el-icon-menu",
                editor:"serviceDetail",
                action: ""
            },
            {
                idx: 1,
                title: "路由",
                icon: "el-icon-cloudy",
                editor:"serviceRouter",
                action: ""
            },
            {
                idx: 2,
                title: "插件",
                icon: "el-icon-cloudy",
                editor:"servicePlugin",
                action: ""
            },
        ],
        svcInfo: {
          name: "",
          groupDesc: ""
        },
    }
  },
  methods: {
    selectM: function(e) {
        this.actionMenuEditor = this.menus.find(item => {
            return item.idx == e;
        }).editor;
        this.curPage = this.menus.find(item => {
            return item.idx == e;
        }).title;
    },
    getSvcOutline(svcId) {
      this.$get('api/svc/group/group', {
        'groupId': this.groupId,
      }).then(res => {
        if (res.data.code == 20000) {
          this.svcInfo.name = res.data.data.group.name;
          this.svcInfo.groupDesc = res.data.data.group.groupDesc;
        }
      })
    }
  }
}
</script>

<style scoped>
.all {
    width: 90%;
    height: 100%;
    padding-top: 1%;
    padding-left: 5%;
    padding-right: 5%;
}

.box-card {
  background-color: #DCDFE6;
}

.body-block {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: row;
}

.left {
    width: 300px;
    height: 100%;
    box-shadow: 2px 1px 2px #d3d3d3;
    padding: 21px 0px 0px 0px;
}

.right {
    height: 100%;
    flex: 1;
}

/* 通过menu的class标签设置选中菜单的背景色 */
.menu .el-menu-item.is-active {
  background-color: #409EFF;
}
</style>
