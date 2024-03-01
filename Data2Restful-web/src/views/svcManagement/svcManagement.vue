<template>
  <div style="padding:15px; padding-left: 3%; padding-right: 3%;">
    <el-card class="box-card">
      <div style="font-size: 30px; margin-top: -0.5%; margin-bottom: 1%;">服务节点管理</div>
      <div style="color: #909399;">服务实体，是对一个上游服务的抽象，是包含一组数据服务接口的集合</div>
    </el-card>
    <div style="display: flex; margin-bottom: 2%;">
      <el-button type="primary" @click="dialogVisible = true"><i class="el-icon-plus"></i>新增服务</el-button>
      <el-button icon="el-icon-search" style="margin-left: 65%;"></el-button>
      <el-input v-model="searchInfo" placeholder="search..." style="width: 15%;"></el-input>
      <div style="height: 40px; line-height: 40px; margin-left: 1%;">Result:</div>
      <el-select v-model="svcNum" placeholder="请选择" style="margin-left: 1%;">
        <el-option v-for="item in svcNumList" :key="item.value":label="item.label" :value="item.value"></el-option>
      </el-select>
    </div>
    <el-table :data="svcList" style="width: 100%; font-size: 16px;" size="huge">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column width="50">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-view" id="eye-button" @click="toSvcDeatial(scope.row.groupId)"></el-button>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" width="500">
        <template slot-scope="scope">
          <div :style="{ color: '#409EFF' }">{{ scope.row.name }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="groupDesc" label="描述" width="300"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" sortable></el-table-column>
      <el-table-column prop="action" label="操作">
        <template slot-scope="scope">
          <el-popconfirm title="确定删除该服务吗？" @confirm="deleteService(scope.row.groupId, scope.row.name)">
            <el-button type="danger" plain slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="创建服务" :visible.sync="dialogVisible" width="50%" :before-close="handleClose" :modal-append-to-body="false">
      <el-form :model="createSvcInfo" status-icon ref="ruleForm" label-width="100px" class="form">
        <el-form-item label="名称" prop="pass">
          <div style="margin-left: 1%;">
            <el-input v-model="createSvcInfo.name" autocomplete="off"></el-input>
            <el-divider class="divider"></el-divider>
            <div style="text-align: left; color: #909399;">服务名称</div>
          </div>
        </el-form-item>
        <el-form-item label="描述" prop="pass">
          <div style="margin-left: 1%;">
            <el-input v-model="createSvcInfo.desc" autocomplete="off"></el-input>
            <el-divider class="divider"></el-divider>
            <div style="text-align: left; color: #909399;">可选的服务描述</div>
          </div>
        </el-form-item>
        <el-form-item label="数据源" prop="pass">
          <div style="margin-left: 1%;">
            <el-select v-model="createSvcInfo.current_ds" placeholder="切换数据源">
              <el-option v-for="item in dataSourceList" :label="item.label" :key="item.value" :value="item">
                <i class="el-icon-coin"></i>{{item.label}}
              </el-option>
            </el-select>
            <el-divider class="divider"></el-divider>
            <div style="text-align: left; color: #909399;">指定该数据服务的数据源</div>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="createService()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'apiManagement',
    components:{

    },
    data () {
      return {
        searchInfo:"",
        svcNumList: [{
          value: '10',
          label: '10'
        }, {
          value: '25',
          label: '25'
        }],
        svcNum: '10',
        svcList: [],
        dialogVisible: false,

        dataSourceList: [],
        createSvcInfo: {
          name: "",
          desc: "",
          current_ds: {},
        },
      }
    },
    created() {
      this.preloadGroupList();
      this.preloadDataSourceList();
    },
    methods: {
      toSvcDeatial(svcId) {
        this.$router.push("/management/service/" + svcId);
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      preloadGroupList() {
        this.$get('api/svc/group/getGroupList').then(response => {
          this.svcList = response.data.data.groupList;
          console.log(this.svcList);
        }).catch(error => {
          console.log(error);
        })
      },
      preloadDataSourceList(){
        this.$get('api/svc/db/sourceList').then(response => {
          this.dataSourceList = response.data.data.dataSourceList;
          this.dataSourceList = this.dataSourceList.map(item => {
            return {
              ...item,
              value: item.name,
              label: item.name + "  (" + item.srcType + ")",
            };
          });
        }).catch(error => {
          console.log(error);
        })
      },
      createService() {
        this.$post('api/svc/group/group', {
          "name": this.createSvcInfo.name,
          "groupDesc": this.createSvcInfo.desc,
          "srcId": this.createSvcInfo.current_ds.srcId,
        }).then(response => {
          if (response.data.code == 20000) {
            this.$alert('成功创建服务', '提示', {
              confirmButtonText: '确定',
              callback: action => {
                this.preloadGroupList();
              }
            });
          } else {
            console.log(response.data);
            this.$message({
              message: "警告：" + response.data.message,
              type: 'warning'
            });
          }
        }).catch(error => {
          console.log(error.data);
        })
        this.dialogVisible = false;
      },
      deleteService(groupId, groupName) {
        this.$delete('api/svc/group/group?groupId=' + groupId + "&groupName=" + groupName).then(response => {
          if (response.data.code == 20000) {
            this.$message({
              message: "成功删除服务" + groupName,
              type: 'success'
            });
            this.preloadGroupList();
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
    }
  }
</script>

<style>
.box-card {
  text-align: left;
  padding-left: 1%;
  margin-top: 1%;
  margin-bottom: 3%;
  background-color: #DCDFE6;
}
#eye-button {
  font-size: 22px;
}

.form {
  padding-top: 3%;
  padding-left: 5%;
  padding-right: 15%;
  font-size: 30px;
}
.divider{
  margin: 0;
}
</style>
