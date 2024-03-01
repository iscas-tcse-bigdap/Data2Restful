<template>
  <div style="padding: 15px;">
    <el-page-header @back="goBack" content="api组管理中心"></el-page-header>
    <el-row style="display:flex; width:100%; margin-left: 1%; margin-bottom:1%; margin-top: 15px;">
      <el-button type="primary" style="margin-right:1%;" @click="dialogFormVisible = true">+ 新建分组</el-button>
      <el-dialog title="新建分组" :visible.sync="dialogFormVisible" :modal-append-to-body="false">
        <el-form :model="new_group">
          <el-form-item label="分组名称" :label-width="formLabelWidth">
            <el-input v-model="new_group.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <el-form :model="new_group">
          <el-form-item label="描述" :label-width="formLabelWidth">
            <el-input v-model="new_group.groupDesc" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="createGroup(new_group.name, new_group.groupDesc)">确 定</el-button>
        </div>
      </el-dialog>
    </el-row>
    <el-table ref="singleTable":data="group_list" highlight-current-row @current-change="handleCurrentChange" style="width: 98%; margin-left: 1%; margin-top: 20px;">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column property="groupId" label="分组id" width="120"></el-table-column>
      <el-table-column property="name" label="分组名称" width="120"></el-table-column>
      <el-table-column property="groupDesc" label="描述"></el-table-column>
      <el-table-column property="time" label="创建时间"></el-table-column>
      <el-table-column prop="action" label="操作">
        <template slot-scope="scope">
          <el-popconfirm title="确定删除该分组吗？" @confirm="deleteGroup(scope.$index, scope.row)">
            <el-button type="danger" plain slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

  </div>

</template>

<script>
  export default {
    name: "groupManage",
    data() {
      return {
        group_list: [],
        dialogFormVisible: false,
        new_group: {
          name: '',
          groupDesc: '',
        },
        formLabelWidth: '120px',
      }
    },
    created() {
      this.preloadGroupList();
    },
    methods: {
      goBack() {
        console.log('go back');
      },
      setCurrent(row) {
        this.$refs.singleTable.setCurrentRow(row);
      },
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      deleteGroup(index, row) {
        console.log(index, row);
        console.log("删除成功！");
      },
      createGroup(g_name, g_describe) {
        this.$post('api/svc/group/group',{
          "name": g_name,
          "groupDesc": g_describe
        }).then(response => {
          if (response.data.code == 20000) {
            this.dialogFormVisible = false;
            console.log("成功创建分组" + g_name);
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
      },
      preloadGroupList() {
        this.$get('api/svc/group/getGroupList').then(response => {
          this.group_list = response.data.data.groupList;
          console.log(this.group_list);
        }).catch(error => {
          console.log(error);
        })
      }
    }
  }
</script>

<style>
</style>
