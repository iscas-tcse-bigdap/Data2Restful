<template>
  <div style="padding:15px">
    <div>
        <div class="add_api_header">
          <div style="height:30px">创建数据服务</div>
          <div style="display: flex;width: 100%;">
            <div style="width: 150px;"> api基本信息</div>
            <div class="api_header">
              <el-input placeholder="服务名称(只能包含数字,大小写字母和下划线)" v-model="api_name" @input="handleNameChange" :style="{'width':'400px', 'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend" >*名称</template>
              </el-input>
              <el-input v-model="path" :disabled="true" :style="{'width':'500px',  'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend">*请求路径</template>
              </el-input>
              <el-select v-model="current_group" placeholder="请选择分组">
                <el-option v-for="item in group_list" :label="item.label" :key="item.value" :value="item"></el-option>
              </el-select>
              <el-input placeholder="请输入内容" v-model="desc" :style="{'width':'860px',  'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend">描述</template>
              </el-input>
              <el-select v-model="isPublicValue" placeholder="请选择" :style="{'margin':'2px 10px 2px 10px'}">
                <el-option v-for="item in isPublicOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </div>
          </div>
        </div>
        <div class="add_api_body" style="height:200px;display: flex;margin:20px">
            <div style="width: 300px; height: 100%;padding:2px;display: flex;flex-direction: column;">
                <div style="padding: 1px;display: inline-flex;line-height: 40px;">
                    <div style="margin-right:15px;">数据源</div>
                    <el-select v-model="current_ds" placeholder="切换数据源" disabled>
                      <el-option v-for="item in dataSourceList" :label="item.label" :key="item.value" :value="item">
                        <i class="el-icon-coin"></i>{{item.label}}
                      </el-option>
                    </el-select>
                </div>
                <el-tree :data="table_list" accordion></el-tree>
            </div>
            <div style="flex:1; height: 100%">
                <div style="width:100%;height:20px;background:#464646"></div>
                <codeEditor ref="child" class="codeEdit" @changeCode="changeCode"></codeEditor>
            </div>
        </div>
        <div class="add_api_args" v-if="args.length > 0">
            <div>请求参数</div>
            <div style="padding:20px">
                <div v-for="item in args" style="line-height: 50px;">
                    <el-input v-model="item.name" :style="{'width':'200px', 'margin':'2px 10px 2px 10px'}"></el-input>
                    <el-input v-model="item.type" :style="{'width':'200px', 'margin':'2px 10px 2px 10px'}"></el-input>
                    <el-input v-model="item.title"  placeholder="备注" :style="{'width':'200px', 'margin':'2px 10px 2px 10px'}"></el-input>
                </div>
            </div>
        </div>
        <div>
            <el-button type="success" @click="saveApiAndParams">保存</el-button>
        </div>
    </div>
  </div>
</template>

<script>
import codeEditor from '@/components/fileEditor/SqlEditor.vue';
import { update } from 'lodash';
import Vue from 'vue'

export default {
  name: "addApi",
  components:{ codeEditor },
  data() {
    return {
        api_name: "",
        api_base_path: "",
        path: `basePath/apiName/pathTail`,
        desc: "",
        dataSourceList: [],
        current_ds: {},
        group_list: [],
        current_group: new Object(),
        table_list: [],
        code:"",
        args: [],
        isPublicOptions:[
          {
            label: '公开访问',
            value: 1,
          },
          {
            label: '需认证',
            value: 0,
          }
        ],
        isPublicValue: 0,
    }
  },
  created() {
    this.preloadBasePath();
    this.preloadDataSourceList();
    this.preloadGroupList();
  },
  watch: {
    current_ds: {
      handler(newVal, oldVal) {
        this.loadTableList();
      }
    },
    current_group: {
      handler(newVal, oldVal) {
        this.path = this.api_base_path + `${this.current_group.name}` + `/` + `${this.api_name}`;
        this.updateCurrentDataSource(this.current_group.srcId);
      }
    }
  },
  methods: {
    goBack() {
      this.$router.push("list");
    },
    changeCode(code) {
      this.code = code;
      console.log(this.code);
      let args = this.getBracketStr(code);
      if(args != null) {
          this.args = [];
          for(let item in args) {
              let obj = {
                  name: args[item].substring(2,args[item].length-1),
                  type: "String",
                  title: "remark",
                  defaultValue: "defaultValue"
              }
              this.args.push(obj);
          }
      }
    },
    getBracketStr(text) {
        let result = '';
        if (text == "")
            return result;
        let regex = /#\{(.+?)\}/g;
        let options = text.match(regex);
        if (options != null) {
            return options;
        }
        return result;
    },
    preloadGroupList(){
      this.$get('api/svc/group/getGroupList').then(response => {
        this.group_list = response.data.data.groupList;
        this.group_list = this.group_list.map(item => {
          return {
            ...item,
            value: item.name,
            label: item.name,
          };
        });
      }).catch(error => {
        console.log(error);
      })
    },
    preloadBasePath(){
      this.api_base_path = Vue.prototype.api.base_path;
    },
    // 虽然交由分组决定数据源， 但是为了显示数据源，这里还是需要进行预加载
    preloadDataSourceList(){
      this.$get('api/svc/db/sourceList').then(response => {
        this.dataSourceList = response.data.data.dataSourceList;
        this.dataSourceList = this.dataSourceList.map(item => {
          return {
            ...item,
            value: item.name,
            label: item.name,
          };
        });
      }).catch(error => {
        console.log(error);
      })
    },
    updateCurrentDataSource(srcid) {
      this.$get("api/svc/db/dsForElementSelect", {
        "srcId": srcid
      }).then(res => {
        this.current_ds = res.data.data.dataSource;
      }).catch(e => {
        console.log(e);
      })
    },
    loadTableList(){
      if (this.current_ds.srcType == "mysql") {
        var url = 'jdbc:mysql://' + this.current_ds.host + ':' + this.current_ds.port + '/' + this.current_ds.db;
        this.$get("api/svc/db/tableList", {
          "dburl": url,
          "username": this.current_ds.username,
          "password": this.current_ds.password
        }).then(response => {
          this.table_list = response.data.data.tableList;
          console.log(this.table_list);
        }).catch(error => {
          console.log(error);
        })
      } else {
        this.$get('api/svc/db/mongoDictList', {
          "host": this.current_ds.host,
          "port": this.current_ds.port,
          "database": this.current_ds.db,
          "user": this.current_ds.username,
          "pwd": this.current_ds.password,
        }).then(response => {
          this.table_list = response.data.data.mongoDictList;
          console.log(this.table_list);
        }).catch(error => {
          console.log(error);
        })
      }
    },
    toApiCenter() {
    	this.$router.push("/api");
    },
    saveApiAndParams() {
      if (this.current_group.groupId == null) {
        this.$message({
          message: "警告：分组不能为空",
          type: 'warning'
        });
        return;
      } else if (this.current_ds.srcId == null) {
        this.$message({
          message: "警告：数据源不能为空",
          type: 'warning'
        });
        return;
      } else if (this.api_name == "") {
        this.$message({
          message: "警告：服务名不能为空",
          type: 'warning'
        });
        return;
      }
      this.$post('api/svc/save',{
        "groupId": this.current_group.groupId,
        "srcId": this.current_ds.srcId,
        "name": this.api_name,
        "apiDesc": this.desc,
        "code": this.code,
        "url": this.path,
        "params": this.args,
        "isPublic": this.isPublicValue,
      }).then(response => {
        if (response.data.code == 20000) {
          this.toApiCenter();
        } else {
          console.log(response.data);
          this.$message({
            message: "警告：" + response.data.message,
            type: 'warning'
          });
        }
      }).catch(error => {
        console.log(error.data);
      });

    },
    handleNameChange(value) {
      this.path = this.api_base_path + `${this.current_group.name}` + `/` + `${this.api_name}`;
    },
  }
}
</script>
<style scoped>
.add_api_header {
    display: flex;
    flex-direction: column;
    align-items: center;
}
.codeEdit{
    width: 100%;
    height: 100%;
    text-align: left;
}
.api_header {
    display: flex;
    flex: 1;
    justify-content: flex-start;
    flex-wrap: wrap;
    flex-direction: row;
}

</style>

<style>
.add_api_header .el-input-group__append, .el-input-group__prepend {
    background-color: #00bd7acc;
    color: rgb(245, 245, 245);
}

.add_api_args {
  margin-top: 2%;
  margin-bottom: 2%;
  margin-left: 18%;
  text-align: left;
}
</style>
