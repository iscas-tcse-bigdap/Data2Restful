<template>
  <div style="padding:15px; height: 100vh; overflow-y: auto">
    <h2 style="text-align: left; margin-left: 1%;">API管理中心</h2>
    <el-row style="display:flex; width:100%; left:1%; margin-bottom:2%;">
      <el-button type="success" style="margin-right:1%;" @click="toAddApi">+ 创建API</el-button>
      <el-button type="success" style="margin-right:1%;" @click="toGroupManage">API分组管理</el-button>
      <el-button type="warning" round style="margin-right:1%;" @click="exportService">导出API配置</el-button>
      <el-button type="warning" round style="margin-right:1%;">导出API分组配置</el-button>
      <el-button type="warning" round style="margin-right:1%;">导入API配置<i class="el-icon-upload el-icon--right"></i></el-button>
      <div style="margin-left: 15px;">
        <el-input placeholder="关键字查询" v-model="keyword">
          <el-button slot="append" icon="el-icon-search" @click="filterApiByKeyword(keyword)"></el-button>
        </el-input>
      </div>
    </el-row>
    <el-table :data="tableData" style="width: 98%; margin-top: 1%; margin-left: 1%; margin-bottom: 5%;" :default-sort = "{prop: 'updateTime', order: 'descending'}">
      <el-table-column prop="apiId" label="id" width="140"></el-table-column>
      <el-table-column prop="apiName" label="名称" width="140"></el-table-column>
      <el-table-column prop="url" label="路径"></el-table-column>
      <el-table-column prop="status" label="类型" width="140">
        <template slot-scope="scope">
          <el-tag type="danger" v-if="scope.row.isPublic == 0">需授权</el-tag>
          <el-tag type="success" v-if="scope.row.isPublic == 1">公开访问</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="140" :filters="status" :filter-method="filterStatus" filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.status == 0">未发布</el-tag>
          <el-tag type="success" v-if="scope.row.status == 1">运行中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="groupName" label="API分组" width="160" :filters="groupsWithApi" :filter-method="filterGroup" filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag disable-transitions>{{scope.row.groupName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="parameter" label="参数" width="160">
        <template slot-scope="scope">
          <div style="width: 100%;" v-for="item in scope.row.params">
            <div style="display: flex">
              <el-tag style="margin-bottom: 3px;">{{item.paramType}}</el-tag>
              <el-tag type="success">{{item.paramName}}</el-tag>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="创建时间" sortable></el-table-column>
      <el-table-column prop="action" label="操作"  width="360">
        <template slot-scope="scope">
          <el-button type="primary" plain @click="toApiTest(scope.row.apiId)">测试</el-button>
          <el-popconfirm title="确定要发布该数据服务吗？" @confirm="operateApi(scope.row.apiId, scope.row.srcId, scope.row.groupId, scope.row.status)">
            <el-button type="success" plain v-if="scope.row.status == 0" slot="reference">发布</el-button>
            <el-button type="warning" plain v-if="scope.row.status == 1" slot="reference">终止</el-button>
          </el-popconfirm>
          <el-button type="primary" plain @click="toApiDetail(scope.row.apiId)">详情</el-button>
          <el-popconfirm title="确定删除该API吗？" @confirm="deleteAPI(scope.row.apiId)">
            <el-button type="danger" :disabled="scope.row.status === 1" plain slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import addApi from './addApi.vue'
import axios from 'axios';
import saveAs from 'file-saver';


export default {
  name: "apiCenter",
  components:{},
    data() {
      return {
        keyword: '',
        options: [{
          value: '分组1',
          label: '分组1',
        },],
        status:[
          {
            text: '未发布',
            value: 0,
          },
          {
            text: '运行中',
            value: 1,
          },
        ],
        groupsWithApi: [],
        current_group: '',
        value: '',
        tableData: [],
      }
    },
    created() {
      this.preloadApiList();
    },
    methods: {
      editAPI(index, row) {
        console.log(row);
      },
      filterGroup(value, row) {
        return row.groupName === value;
      },
      filterStatus(value, row) {
        return row.status === value;
      },
      toApiTest(apiId) {
      	this.$router.push("/api/test/" + apiId);
      },
      toAddApi() {
      	this.$router.push("/api/add");
      },
      toGroupManage() {
      	this.$router.push("/api/group_manage");
      },
      toApiDetail(apiId) {
      	this.$router.push("/api/detail/" + apiId);
      },
      preloadApiList(){
        this.$get('api/svc/getApiList').then(response => {
          this.tableData = response.data.data.apiOutlineAndGroup.apiOutlines;
          console.log(this.tableData);
          this.groupsWithApi = [];
          for (let i = 0; i < response.data.data.apiOutlineAndGroup.groups.length; i++) {
            const obj = {
              text: "",
              value: "",
            };
            obj.text = response.data.data.apiOutlineAndGroup.groups[i];
            obj.value = response.data.data.apiOutlineAndGroup.groups[i];
            this.groupsWithApi.push(obj);
          }
        }).catch(error => {
          console.log(error);
        })
      },
      operateApi(apiId, srcId, groupId, status) {
        if (status == 1) {
          this.terminateAPI(apiId, srcId, groupId);
        } else if (status == 0) {
          this.publishApi(apiId, srcId, groupId);
        }
      },
      publishApi(apiId, srcId, groupId){
        this.$put('api/svc/publish', {
          'apiId': apiId,
          'srcId': srcId,
          'groupId': groupId
        }).then(response => {
          if (response.data.code == 20000) {
            this.$message({
              showClose: true,
              message: '服务发布成功',
              type: 'success'
            })
            this.preloadApiList();
            this.$forceUpdate();
          }
        }).catch(error => {
          this.$message.error(error.response.data.message);
        });
      },
      terminateAPI(apiId, srcId, groupId) {
        this.$put('api/svc/terminate', {
          'apiId': apiId,
          'srcId': srcId,
          'groupId': groupId,
        }).then(response => {
          if (response.data.code == 20000) {
            this.preloadApiList();
            this.$message({
              showClose: true,
              message: '终止数据服务',
              type: 'success'
            });
            this.$forceUpdate();
          }
        }).catch(error => {
          this.$message.error(error.response.data.message);
        });
      },
      deleteAPI(apiId) {
        this.$delete("api/svc/delete?apiId=" + apiId).then(res => {
          if (res.data.code == 20000) {
            this.preloadApiList();
            this.$message({
              showClose: true,
              message: "服务删除成功",
              type: 'success'
            });
          }
        }).catch(err => {
          this.$message.error(err.response.data.message);
        });
      },
      filterApiByKeyword(keyword){
        this.$get('api/svc/filterByKeyword', {
          "keyword": keyword
        }).then(response => {
          this.tableData = response.data.data.apiListWithKeyword.apiOutlines;
          this.groupsWithApi = [];
          for (let i = 0; i < response.data.data.apiListWithKeyword.groups.length; i++) {
            const obj = {
              text: "",
              value: ""
            };
            obj.text = response.data.data.apiListWithKeyword.groups[i];
            obj.value = response.data.data.apiListWithKeyword.groups[i];
            this.groupsWithApi.push(obj);
          }
          this.$forceUpdate();
        }).catch(error => {
          console.log(error);
        })
      },
      exportService() {
        var serviceList = this.tableData;
        const exportServiceList = serviceList.map(obj => {
          const { apiId, status, updateTime, ...rest } = obj;
          return rest;
        });
        const json = JSON.stringify(exportServiceList, null, 2);
        const blob = new Blob([json], { type: 'application/json' });
        saveAs(blob, 'exported_data.json');
      },
    }
  }
</script>

<style>
</style>
