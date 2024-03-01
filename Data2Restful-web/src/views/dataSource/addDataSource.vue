<template>
  <div style="padding: 15px;">
    <el-page-header @back="goBack" content="添加数据源"></el-page-header>
    <h1>添加数据源</h1>
    <div style="margin: 20px;"></div>
    <el-form :label-position='labelPosition' label-width="80px" :model="dataSource">
      <el-form-item label="源类型" style="display: flex;">
        <el-select v-model="dataSource.ds_type" placeholder="请选择" style="margin-left: -80px;">
            <el-option v-for="item in ds_type_list" :key="item.value" :value="item.value">
              <i class="el-icon-coin"></i>{{item.label}}
            </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="dataSource.name" placeholder="数据源名称(必填)"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="dataSource.describe"></el-input>
      </el-form-item>
      <el-form-item label="jdbc驱动类" v-if="dataSource.ds_type != 'mongodb'">
        <el-input v-model="dataSource.jdbcDriver"></el-input>
      </el-form-item>
      <el-form-item label="host">
        <el-input v-model="dataSource.host"></el-input>
      </el-form-item>
      <el-form-item label="port">
        <el-input v-model="dataSource.port"></el-input>
      </el-form-item>
      <el-form-item label="数据库">
        <el-input v-model="dataSource.db"></el-input>
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
        <el-button type="primary" plain @click="addDataSource">确认添加</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "addDataSource",
    data() {
      return {
        labelPosition: 'right',
        ds_type_list: [{
          value: 'mysql',
          label: 'mysql',
        },{
          value: 'mongodb',
          label: 'mongodb',
        }],
        dataSource: {
          ds_type: '',
          name: '',
          describe: '',
          jdbcDriver: "",
          host: "",
          port: "",
          db: "",
          username: "",
          password: "",
          status: "0",
        },
        connection: 0,
      }
    },
    methods: {
      goBack() {
        console.log('go back');
      },
      toDsCenter() {
        this.$router.push("/ds");
      },
      addDataSource() {
         this.$post('api/svc/db/create', {
            "name": this.dataSource.name,
            "srcType": this.dataSource.ds_type,
            "driverClassName": this.dataSource.jdbcDriver,
            "host": this.dataSource.host,
            "port": this.dataSource.port,
            "db": this.dataSource.db,
            "username": this.dataSource.username,
            "password": this.dataSource.password,
            "rmark": this.dataSource.describe,
            "status": this.dataSource.status
         }).then(response => {
           if (response.data.code == 20000) {
             console.log(response);
             this.createConfigMap();
           } else {
             console.log(response.data);
             this.$message({
               message: "错误：" + response.data.message,
               type: 'error'
             });
           }
         }).catch(error => {
           console.log(error);
         })
      },
      createConfigMap() {
        this.$post('k8scli/api/k8s/createConfigMap', {
          "srcType": this.dataSource.ds_type,
          "dsName": this.dataSource.name,
          "host": this.dataSource.host,
          "port": this.dataSource.port,
          "db": this.dataSource.db,
          "user": this.dataSource.username,
          "pwd": this.dataSource.password
        }).then(response => {
          if(response.data.code == 200) {
            this.toDsCenter();
          } else {
            console.log(response.data);
            this.$message({
              message: "创建configmap发生错误" + response.data.message,
              type: 'error'
            });
          }
        })
      },
      testConnection() {
        if (this.dataSource.ds_type == "mysql") {
          var url = 'jdbc:mysql://' + this.dataSource.host + ':' + this.dataSource.port + '/' + this.dataSource.db;
          this.$get('api/svc/db/testConnectionMysql', {
            "url": url,
            "driverClass": this.dataSource.jdbcDriver,
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
        } else if (this.dataSource.ds_type == "mongodb") {
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
    },
  }
</script>

<style scope>
</style>
