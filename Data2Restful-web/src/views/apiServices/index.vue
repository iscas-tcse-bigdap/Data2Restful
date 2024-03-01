<template>
  <div class="public">
    <div class="bo">
        <div class="left">
            <el-menu
                :style="{ 'text-align':'left', 'padding-left':'8px', 'background-color':'#f8f8f8'}"
                default-active="2"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                @select="selectM">
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

import apiDetail from "./apiDetail.vue"
import emptyPage from "@/components/empty.vue"
import apiCenter from "./apiCenter.vue"

export default {
  name: 'apiServiceIndex',
  components:{
    'apiDetail': apiDetail,
    "emptyPage": emptyPage,
    "apiCenter": apiCenter,
  },
  data () {
    return {
        actionMenuEditor: "",
        menus: [
            {
                idx: 0,
                title: "数据源",
                icon: "el-icon-menu",
                editor:"emptyPage",
                action: ""
            },
            {
                idx: 1,
                title: "服务分组",
                icon: "el-icon-document",
                editor:"emptyPage",
                action: ""
            },
            {
                idx: 2,
                title: "服务xxx",
                icon: "el-icon-setting",
                editor:"emptyPage",
                action: ""
            },
            {
                idx: 3,
                title: "发布数据服务",
                icon: "el-icon-setting",
                editor:"apiDetail",
                action: ""
            },
            {
              idx: 4,
              title: "API管理中心",
              icon: "el-icon-setting",
              editor:"apiCenter",
              action: ""
            },
        ],
        btns: [
            {
                title: "python",
                name: "python"
            },
            {
                title: "sql",
                name: "sql"
            }
        ],
        serviceCode: {
            type: "python",
            code: "select * from appinfo"
        },
        user: null,
        url: "http://192.168.1.100:31000/get",
        result: ""

    }
  },
  methods: {
        changeAlgo: function(item) {
            this.serviceCode.code = "";
            this.serviceCode.type = item.name
        },
        publicService: function() {
            if(this.serviceCode.code == "") {
                this.$message('请输入代码');
                return
            }

            this.$get("http://localhost:9480/api/algo/public", this.serviceCode).then(res => {
                console.log(res)
                this.url="http://192.168.1.100:31000/get";
            }).catch(err => {
                console.log(err)
            })
        },
        resApi: function() {
            if(this.url == "") {
                this.$message('请输入url');
                return
            }
            this.result = "";
            this.$get(this.url).then(res => {
                this.result = res
            }).catch(err => {
                console.log(err)
            })
        },
        handleOpen: function() {

        },
        handleClose: function(){

        },
        selectM: function(e) {
            this.actionMenuEditor = this.menus.find(item => {
                return item.idx == e;
            }).editor;
        }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
}

.right {
    height: 100%;
    flex: 1;
}

.left .btn {
    height: 50px;
    line-height: 50px;
    border: 1px solid #ffffff;
    background: #c7c7c7;
    color: black;
}
.left .btn:hover {
    cursor: pointer;
}
.left .cur_btn {
    background: #20b3b3;
}
.right .center {
    width: 100%;
    height: 100%;
}
</style>
