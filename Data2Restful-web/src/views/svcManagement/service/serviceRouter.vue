<template>
  <div class="servicerouter_all">
    <el-card class="box-card">
      <div style="font-size: 26px; text-align: left; margin-left: 1%; display: flex;">
        <div style="width: 50%;">服务路由</div>
        <el-button icon="el-icon-search" ></el-button>
        <el-input v-model="searchInfo" placeholder="搜索路由" style="width: 20%;"></el-input>
        <el-button type="primary" style="margin-left: 8%;"><i class="el-icon-plus"></i>添加路由</el-button>
      </div>
    </el-card>
    <el-table :data="svc_router_list" style="width: 100%">
      <el-table-column type="index"></el-table-column>
      <el-table-column prop="id" label="名称/ID"></el-table-column>
      <el-table-column prop="host" label="主机"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="protocol" label="协议"></el-table-column>
      <el-table-column prop="method" label="方法"></el-table-column>
      <el-table-column prop="create_time" label="创建时间"></el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: 'serviceRouter',
    components:{},
    data () {
      return {
        searchInfo: "",
        svc_router_list: [],

      }
    },
    methods: {

    }
  }
</script>

<style>
.servicerouter_all {
  margin-left: 5%;
}
</style>
