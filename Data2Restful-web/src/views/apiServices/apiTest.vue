<template>
  <div style="padding: 15px;">
    <el-page-header @back="goBack" content="接口请求测试"></el-page-header>
    <h1>接口{{api.name}}请求测试</h1>
    <div style="margin-top: 15px;">
      <span style="display: flex; font-size: 20px; font-weight: bold; margin-bottom: 10px;;">接口地址:</span>
      <el-input placeholder="接口地址" v-model="api.url"  :disabled="true" clearable></el-input>
    </div>
    <div style="margin-top: 15px; display: flex;">
      <div style="width: 40%;">
        <span style="display: flex; font-size: 20px; font-weight: bold; margin-bottom: 10px;;">接口名称:</span>
        <el-input placeholder="接口名称" v-model="api.name"  :disabled="true" clearable></el-input>
      </div>
      <div style="width: 40%; margin-left: 5%;">
        <span style="display: flex; font-size: 20px; font-weight: bold; margin-bottom: 10px;;">接口描述:</span>
        <el-input placeholder="接口描述" v-model="api.apiDesc"  :disabled="true" clearable></el-input>
      </div>
    </div>
    <div style="margin-top: 15px;">
      <span style="display: flex; font-size: 20px; font-weight: bold; margin-bottom: 10px;;" v-if= "paramsList.length > 0">接口参数:</span>
      <div style="width: 100%;" v-for="item in paramsList">
        <div style="display: flex">
          <el-tag style="margin-left: 2%; margin-bottom: 10px; font-size: 16px;">{{item.key}}</el-tag>
          <el-tag type="success" style="font-size: 16px;">{{item.value[0]}}</el-tag>
          <el-input v-model="item.value[2]" style="margin-left: 1%; width: 20%; border-color: aquamarine;"></el-input>
        </div>
      </div>
    </div>
    <div style="display: flex; margin-top: 20px;">
      <el-select v-model="requestMethod" placeholder="请选择" style="margin-left: 10px;">
        <el-option v-for="item in requestMethodList" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-button style="margin-left: 20px;" @click="test">发送请求</el-button>
    </div>
    <div style="margin-top: 15px;" v-if="openJson==true">
      <span style="display: flex; font-size: 20px; font-weight: bold; margin-bottom: 10px;;">返回结果:</span>
      <vue-json-editor v-model="testInfo" :showBtns="false" :mode="'text'"/>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import vueJsonEditor from 'vue-json-editor'
  export default {
    name: "apiTest",
    components: { vueJsonEditor },
    data() {
      return {
        apiId: -1,
        api: [],
        paramsList: [],
        requestMethodList: [{
          value: 'GET',
          lable: 'GET',
        },{
          value: 'PUT',
          lable: 'PUT',
        },{
          value: 'DELETE',
          lable: 'DELETE',
        },{
          value: 'POST',
          lable: 'POST',
        }],
        requestMethod: "GET",
        testInfo: {},
        openJson: false,
      }
    },
    created(){
      this.apiId = this.$route.params.apiId;
      this.preloadApiInfo(this.apiId);
    },
    methods: {
      goBack() {
        console.log('go back');
      },
      preloadApiInfo() {
        this.$get("api/svc/getApiInfoByApiId", {
          "apiId": this.apiId
        }).then(res => {
          this.api = res.data.data.api;
          var map = new Map();
          for (var key in this.api.params) {
            this.api.params[key].value = "";
            map.set(key, this.api.params[key]);
          }
          this.paramsList = this.mapToArray(map);
          console.log(this.paramsList);
        })
      },
      test() {
        console.log(this.api);
        if (this.api.isPublic==0&&this.$store.state.token==null) {
          this.$message({
            showClose: true,
            message: '无权限访问该数据服务',
            type: 'error'
          });
        } else {
          const instance = axios.create({
            headers: {
              'Authorization': this.$store.state.token
            }
          });
          if (this.requestMethod == "GET") {
            const finalUrl = this.api.url + (this.api.params ? `?${this.filterParamsToString(this.api.params)}` : '');
            instance.get(finalUrl).then(res => {
              this.testInfo = res.data;
              this.openJson = true;
            }).catch(err => {
              this.$message({
                showClose: true,
                message: "error:" + err.response.data.message,
                type: 'error'
              });
              this.testInfo = err.message;
            })
          } else if (this.requestMethod == "POST") {
            instance.post(this.api.url, this.filterParamsToJson(this.api.params)).then(res => {
              this.testInfo = res.data;
              this.openJson = true;
            }).catch(err => {
              this.$message({
                showClose: true,
                message: "error:" + err.response.data.message,
                type: 'error'
              });
              this.testInfo = err.message;
            })
          } else if (this.requestMethod == "PUT") {
            instance.put(this.api.url, this.filterParamsToJson(this.api.params)).then(res =>{
              this.testInfo = res.data;
              this.openJson = true;
            }).catch(err => {
              this.$message({
                showClose: true,
                message: "error:" + err.response.data.message,
                type: 'error'
              });
              this.testInfo = err.message;
            })
          } else if (this.requestMethod == "DELETE") {
            const finalUrl = this.api.url + (this.api.params ? `?${this.filterParamsToString(this.api.params)}` : '');
            instance.delete(finalUrl).then(res => {
              this.testInfo = res.data;
              this.openJson = true;
            }).catch(err => {
              this.$message({
                showClose: true,
                message: "error:" + err.response.data.message,
                type: 'error'
              });
              this.testInfo = err.message;
            })
          }
          else {
            this.$message({
              showClose: true,
              message: '暂不支持该方法',
              type: 'warning'
            });
          }
        }

      },
      mapToArray(map) {
        return Array.from(map, ([key, value]) => ({ key, value }));
      },
      filterParamsUnusedInfo(paramObj) {
        const filteredParams = {};
        /** 过滤掉无用数据
         * paramObj = {
           * username:['string', remark, @param {String}  = [value] ]
         }
        */
        for (const key in paramObj) {
          const values = paramObj[key];
          if (values.length >= 3 && values[2] != "") {
            filteredParams[key] = [values[2]];
          }
        }
        return filteredParams
      },
      filterParamsToString(paramObj) {
        const filteredParams = this.filterParamsUnusedInfo(paramObj);
        // 将过滤后的参数对象转换为查询字符串
        const params = new URLSearchParams(filteredParams).toString();
        return params;
      },
      filterParamsToJson(paramObj) {
        const filteredParams = this.filterParamsUnusedInfo(paramObj);
        const paramsJson = Object.fromEntries(
          Object.entries(filteredParams).map(([key, value]) => [key, value[0]])
        );
        return paramsJson;
      }
    }
  }
</script>

<style>
</style>
