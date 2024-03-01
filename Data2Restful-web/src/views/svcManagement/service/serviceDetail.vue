<template>
  <div class="detail_all">
    <el-card class="box-card">
      <div style="font-size: 26px; text-align: left; margin-left: 1%;"><i class="el-icon-warning-outline" style="font-weight: 600;"></i> 服务详情</div>
    </el-card>
    <el-form status-icon ref="ruleForm" label-width="100px" class="form">
      <el-form-item label="名称" prop="pass">
        <div style="margin-left: 1%;">
          <div style="text-align: left;">{{svc.name}}</div>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left;">服务名称</div>
        </div>
      </el-form-item>
      <el-form-item label="描述" prop="pass">
        <div style="margin-left: 1%;">
          <div style="text-align: left;">{{svc.groupDesc}}</div>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left;">可选的服务描述</div>
        </div>
      </el-form-item>
      <el-form-item label="创建时间" prop="pass">
        <div style="margin-left: 1%;">
          <div style="text-align: left;">{{svc.createTime}}</div>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left;">该服务的创建时间</div>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: 'serviceDetail',
    components:{
    },
    data () {
      return {
        svcId: -1,
        svc: {
          name: "",
          groupDesc: "",
          createTime: ""
        },
      }
    },
    created() {
      this.svcId = this.$route.params.svcId;
      this.preloadGroup();
    },
    methods: {
      preloadGroup() {
        this.$get('api/svc/group/group', {
          "groupId": this.svcId
        }).then(response => {
          this.svc = response.data.data.group;
          console.log(this.svc);
        }).catch(error => {
          console.log(error);
        })
      },
    }
  }
</script>

<style>
.detail_all {
  margin-left: 5%;
}
.form {
  padding-top: 3%;
  padding-left: 10%;
  padding-right: 10%;
  font-size: 30px;
}
.divider{
  margin: 0;
}
</style>
