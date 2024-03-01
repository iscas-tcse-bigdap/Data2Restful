<template>
  <div style="padding:15px">
    <div>
        <div class="add_api_header">
          <h1>{{api.name}}接口详情</h1>
          <div style="display: flex;width: 100%;">
             <div style="width: 150px;"> api基本信息</div>
              <div class="api_header">
                <el-input placeholder="服务名称(只能包含数字,大小写字母和下划线)" v-model="api.name" @input="handleNameChange" :style="{'width':'400px', 'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend" >*名称</template>
                </el-input>
                <el-input v-model="api.url" :disabled="true" :style="{'width':'500px',  'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend">*请求路径</template>
                </el-input>
                <el-select v-model="current_group" placeholder="请选择分组">
                  <el-option v-for="item in group_list" :label="item.label" :key="item.value" :value="item"></el-option>
                </el-select>
                <el-input placeholder="接口描述" v-model="api.apiDesc" :style="{'width':'860px',  'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend">描述</template>
                </el-input>
                <el-select v-model="api.isPublic" placeholder="请选择" :style="{'margin':'2px 10px 2px 10px'}">
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
            <div style="flex:1; height: 100%;width:100%;">
                <div style="height:20px;background:#464646"></div>
                <monaco-editor types="sql" ref="child" class="codeEdit" :codes="this.api.code" @onCodeChange="excelTextChanged"> </monaco-editor>
            </div>
        </div>
        <div style="margin-top: 50px;">
          <el-button type="primary" @click="saveCode">保存代码</el-button>
        </div>
        <div class="add_api_args" v-if="args.length > 0">
            <div>请求参数</div>
            <div style="padding:20px">
              <div v-for="item in existedArgsList" style="line-height: 50px;">
                <el-input v-model="item.key" :disabled="true" :style="{'width':'200px', 'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend" >参数名</template>
                </el-input>
                <el-input v-model="item.value[0]" :style="{'width':'200px', 'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend" >类型</template>
                </el-input>
                <el-input v-model="item.value[1]"  placeholder="备注" :style="{'width':'400px', 'margin':'2px 10px 2px 10px'}">
                  <template slot="prepend" >描述</template>
                </el-input>
              </div>
            </div>
        </div>
        <el-popover placement="top-start" trigger="hover" content="已发布的数据服务无法修改" :disabled="this.api.status == 0">
          <el-button slot="reference" type="success" :disabled="this.api.status == 1" @click="updateApi">确认修改</el-button>
        </el-popover>
    </div>
  </div>
</template>

<script>
import codeEditor from '@/components/fileEditor/SqlEditor.vue';
import monacoEditor from '@/components/fileEditor/MonacoEditor.vue'
import { map } from 'lodash';
import Vue from 'vue'

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

export default {
  name: "addApi",
  components:{ codeEditor, monacoEditor },
  data() {
    return {
      apiId: -1,
      api: [],
      api_base_path: "",
      group_list: [],
      current_group: {},
      dataSourceList: [],
      current_ds: {},
      table_list: [],
      /**
       * args = [name1, name2]
       * existedArgs = {
            name1: [String, v1],
            name2: [int, v2]
          }
       * existedArgsList 是将上述map转换成array， 便于 v-for 的遍历
       */
      args: [],
      existedArgs: new map(),
      existedArgsList: [],
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
    }
  },
  async created() {
    this.preloadBasePath();
    this.preloadGroupList();
    this.preloadDataSourceList();
    this.apiId = this.$route.params.apiId;
    await this.preloadApiInfo(this.apiId);
  },
  watch: {
    current_ds: {
      handler(newVal, oldVal) {
        this.loadTableList();
      }
    },
    current_group: {
      handler(newVal, oldVal) {
        this.api.url = this.api_base_path + `${this.current_group.name}` + `/` + `${this.api.name}`;
        this.updateCurrentDataSource(this.current_group.srcId);
      }
    }
  },
  methods: {
    goBack() {
      this.$router.push("list");
    },
    excelTextChanged(code) {
      this.api.code = code;
      this.$emit("changeCode", code);
      let args = this.getBracketStr(code);
      this.args = args;
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
    preloadBasePath(){
      this.api_base_path = Vue.prototype.api.base_path;
      this.api_path_tail = Vue.prototype.api.path_tail;
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
    preloadApiInfo() {
      return new Promise((resolve, reject) => {
          this.$get("api/svc/getApiInfoByApiId", {
            "apiId": this.apiId,
          }).then(res => {
            if (res.data.code == 20000) {
              this.api = res.data.data.api;
              console.log(this.api);
              var map = new Map();
              for (var key in this.api.params) {
                map.set(key, this.api.params[key]);
              }
              this.existedArgs = map;
              this.existedArgsList = this.mapToArray(this.existedArgs);
              // 加载分组
              this.current_group = res.data.data.api.group;
              this.current_group["label"] = this.current_group.name;
              this.current_group["value"] = this.current_group.name;
              // 加载数据源
              this.current_ds = res.data.data.api.dataSource;
              this.current_ds["label"] = this.current_ds.name;
              this.current_ds["value"] = this.current_ds.name;

              console.log(this.current_group);
              console.log(this.current_ds);
              this.loadTableList();
              this.$forceUpdate();

            } else {
              reject(new Error("请求失败")); // 某些错误情况下拒绝 Promise
            }
          }).catch(error => {
            reject(error); // 异步请求出错，拒绝 Promise
          });
        });
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
        console.log(this.dataSourceList);
      }).catch(error => {
        reject(error); // 异步请求出错，拒绝 Promise
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
        }).catch(error => {
          console.log(error);
        })
      }
    },
    saveCode() {
      // if it exists now, but does not exist at the beginning, add it
      for (let i in this.args) {
        // delete { }
        this.args[i] = this.args[i].substring(2, this.args[i].length-1)
        if (!this.existedArgs.has(this.args[i])) {
          var info = ['String', ''];
          this.existedArgs.set(this.args[i], info);
        }
      }
      // It started to exist, but now it does not exist, delete it
      for (const [key, value] of this.existedArgs) {
        if (!this.args.includes(key)) {
          this.existedArgs.delete(key);
        }
      }
      this.existedArgsList = this.mapToArray(this.existedArgs);
      this.$forceUpdate();
    },
    // since map cannot be traversed directly with v-for, so converted to array here
    mapToArray(map) {
      return Array.from(map, ([key, value]) => ({ key, value }));
    },
    updateApi() {
      if (this.api.name == '') {
        this.$message({
          message: "警告：服务名不能为空",
          type: 'warning'
        });
        return;
      }
      var params = [];
      for (const [key, value] of this.existedArgs) {
        const param = {
          apiId: this.apiId,
          name: key,
          type: value[0],
          title: value[1]
        };
        params.push(param);
      }
      this.$put("api/svc/update", {
        "apiId": this.apiId,
        "groupId": this.current_group.groupId,
        "srcId": this.current_ds.srcId,
        "name": this.api.name,
        "apiDesc": this.api.apiDesc,
        "code": this.api.code,
        "url": this.api.url,
        "params": params,
        "isPublic": this.api.isPublic,
      }).then(response => {
        if (response.data.code == 20000) {
          console.log(response);
          this.toApiCenter();
        } else {
          console.log(response.data);
          this.$message({
            message: "警告：" + response.data.message,
            type: 'warning'
          });
        }
      }).catch(error => {
        console.log(error);
      })
    },
    toApiCenter() {
    	this.$router.push("/api");
    },
    handleNameChange(value) {
      this.api.url = this.api_base_path + `${this.current_group.name}` + `/` + `${this.api.name}`;
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
.el-input-group__append, .el-input-group__prepend {
    background-color: #00bd7acc;
    color: rgb(245, 245, 245);
}

.add_api_args {
  margin: 10px;
  margin-left: 18%;
  text-align: left;
}
</style>


<!--

time1:

args = [name1, name2]
existedArgs = {
  name1: [String, v1],
  name2: [int, v2],
}


 /////////////////

time2:

args = [name1, name3]

oldKeysShorcut = existedArgs.keys()

for x in args:
  if x not in oldKeysShorcut:
    existedArgs.add(x, defaultValues)

for x in oldKeysShortcut:
  if x not in args:
    existedArgs.removeKey(x)

 -->
