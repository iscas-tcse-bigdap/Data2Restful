<template>
  <div style="padding: 15px;">
    <h2 style="text-align: left; margin-left: 1%;">数据源管理中心</h2>
    <el-row style="display:flex; width:100%; left:1%; margin-bottom:1%; margin-top: 15px;">
      <el-button type="primary" style="margin-right:1%;" @click="toAddDS">+ 添加数据源</el-button>
      <el-button type="warning" style="margin-right:1%;">- 导出数据源列表</el-button>
    </el-row>
    <el-table ref="singleTable" :data="ds_list" highlight-current-row @current-change="handleCurrentChange" style="width: 98%; margin-left: 1%; margin-top: 20px;">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column property="srcType" label="数据源类型" width="120"></el-table-column>
      <el-table-column property="name" label="名称" width="120"></el-table-column>
      <el-table-column property="driverClassName" label="驱动类" width="200"></el-table-column>
      <el-table-column property="host" label="host"></el-table-column>
      <el-table-column property="port" label="端口"></el-table-column>
      <el-table-column property="db" label="数据库"></el-table-column>
      <el-table-column property="rmark" label="备注"></el-table-column>
      <el-table-column prop="action" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" plain @click="toDsDetail(scope.row.srcId)">详情</el-button>
          <el-popconfirm title="确定删除该数据源吗？" @confirm="deleteDataSource(scope.row.srcId, scope.row.name)">
            <el-button type="danger" plain slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>


  </div>
</template>

<script>
  export default {
    name: "dsCenter",
    data() {
      return {
        ds_list: []
      }
    },
    created() {
      this.preloadDataSourceList();
    },
    methods: {
      setCurrent(row) {
        this.$refs.singleTable.setCurrentRow(row);
      },
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      toAddDS() {
      	this.$router.push("/ds/add");
      },
      toDsDetail(srcId){
        this.$router.push("/ds/detail/" + srcId);
      },
      preloadDataSourceList() {
        this.$get('api/svc/db/sourceList').then(response => {
          this.ds_list = response.data.data.dataSourceList;
        }).catch(error => {
          console.log(error);
        })
      },
      deleteDataSource(srcId, srcName) {
        this.$delete("k8scli/api/k8s/deleteConfigMap?srcName=" + srcName).then(response => {
          if (response.data.code == 200) {
            console.log("configmap删除成功，准备删除数据库表项");
            this.deleteDsTableRow(srcId);
          }
        }).catch(error => {
          console.log("删除configmap时发生错误，报错信息如下" + error);
        });
      },
      deleteDsTableRow(srcId) {
        this.$delete("api/svc/db/delete?srcId=" + srcId).then(res => {
          if (res.data.data.code = 20000) {
            console.log("成功删除数据源");
            this.$forceUpdate();
          }
        }).catch(err => {
          console.log("删除数据库表项时发生错误，报错信息如下" + err);
        })
      },
    }
  }
</script>

<style>
</style>
