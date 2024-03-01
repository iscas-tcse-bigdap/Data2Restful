<template>
  <div style="padding: 15px;">
    <el-page-header @back="goBack" :content="dataSource.name"></el-page-header>
    <h1>数据源 {{dataSource.name}} 详情</h1>
    <div style="margin: 20px;"></div>
    <el-form :label-position='labelPosition' label-width="80px" :model="dataSource">
      <el-form-item label="源类型" style="display: flex;">
        <el-select v-model="dataSource.srcType" placeholder="请选择" style="margin-left: -80px;">
            <el-option v-for="item in ds_type_list" :key="item.value" :value="item.value">
              <i class="el-icon-coin"></i>{{item.label}}
            </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="dataSource.rmark"></el-input>
      </el-form-item>
      <el-form-item label="jdbc驱动类" v-if="dataSource.srcType != 'mongodb'">
        <el-input v-model="dataSource.driverClassName" placeholder="com.mysql.cj.jdbc.Driver"></el-input>
      </el-form-item>
      <el-form-item label="host">
        <el-input v-model="dataSource.host" placeholder="60.245.209.102"></el-input>
      </el-form-item>
      <el-form-item label="port">
        <el-input v-model="dataSource.port" placeholder="13306"></el-input>
      </el-form-item>
      <el-form-item label="数据库">
        <el-input v-model="dataSource.db" placeholder="datacenter"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="dataSource.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="dataSource.password" show-password></el-input>
      </el-form-item>
    </el-form>
    <el-alert style= "width: 90%; margin-left: 5%;" title="数据源连接测试成功" type="success" effect="dark" v-if="connection == 1"></el-alert>
    <el-alert style= "width: 90%; margin-left: 5%;" title="数据源连接失败" type="warning" effect="dark" v-if="connection == -1"></el-alert>
    <div style="margin-top: 20px;">
        <el-button plain @click="testConnection">连接测试</el-button>
        <el-button type="primary" plain @click="updateDataSource">确认修改</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "updateDataSource",
    data() {
      return {
        srcId: -1,
        labelPosition: 'right',
        ds_type_list: [{
          value: 'mysql',
          label: 'mysql',
        }],
        dataSource: {},
        connection: 0,
      }
    },
    created() {
      this.srcId = this.$route.params.srcId;
      this.preloadDsInfo(this.srcId);
    },
    methods: {
      goBack() {
        console.log('go back');
      },
      toDsCenter() {
        this.$router.push("/ds");
      },
      preloadDsInfo(srcId) {
        this.$get("api/svc/db/datasource", {
          "srcId": this.srcId,
        }).then(response => {
          this.dataSource = response.data.data.dataSource;
          this.$forceUpdate();
        })
      },
      testConnection() {
        if (this.dataSource.srcType == "mysql") {
          var url = 'jdbc:mysql://' + this.dataSource.host + ':' + this.dataSource.port + '/' + this.dataSource.db;
          this.$get('api/svc/db/testConnectionMysql', {
            "url": url,
            "driverClass": this.dataSource.driverClassName,
            "user": this.dataSource.username,
            "pwd": this.dataSource.password
          }).then(response => {
            console.log(response);
            if (response.data.code == 20000) {
              this.connection = 1;
              this.dataSource.status = 1;
            } else {
              this.connection = -1;
              this.dataSource.status = 0;
            }
          }).catch(error => {
            console.log(error);
          })
        } else if (this.dataSource.srcType == "mongodb") {
          this.$get('api/svc/db/testConnectionMongoDB', {
            "host": this.dataSource.host,
            "port": this.dataSource.port,
            "database": this.dataSource.db,
            "user": this.dataSource.username,
            "pwd": this.dataSource.password
          }).then(response => {
            console.log(response);
            if (response.data.code == 20000) {
              this.connection = 1;
              this.dataSource.status = 1;
            } else {
              this.connection = -1;
              this.dataSource.status = 0;
            }
          }).catch(error => {
            console.log(error);
          })
        }

      },
      updateDataSource() {
        this.$post('k8scli/api/k8s/updateConfigMap', {
          "dsName": this.dataSource.name,
          "host": this.dataSource.host,
          "port": this.dataSource.port,
          "db": this.dataSource.db,
          "user": this.dataSource.username,
          "pwd": this.dataSource.password
        }).then(response => {
          if (response.data.code == 200) {
            console.log("configmap更新成功，准备更新数据库表项");
            this.updateSrcTableRow();
          }
        }).catch(error => {
          console.log("更新configmap时发生错误，报错信息如下" + error);
        })
      },
      updateSrcTableRow() {
        this.$put('api/svc/db/update', {
          "srcId": this.dataSource.srcId,
          "name": this.dataSource.name,
          "srcType": this.dataSource.srcType,
          "driverClassName": this.dataSource.driverClassName,
          "host": this.dataSource.host,
          "port": this.dataSource.port,
          "db": this.dataSource.db,
          "username": this.dataSource.username,
          "password": this.dataSource.password,
          "rmark": this.dataSource.rmark,
          "status": this.dataSource.status
        }).then(res => {
          if (res.data.code == 20000) {
            this.$message({
              message: "数据源" + this.dataSource.name + "修改成功",
              type: 'success'
            });
            this.toDsCenter();
          }
        }).catch(err => {
          console.log("更新数据库表项时发生错误，报错信息如下" + err);
        })
      },
    }
  }
</script>

<style>
</style>
