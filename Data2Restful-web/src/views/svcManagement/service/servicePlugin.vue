<template>
  <div class="serviceplugin_all">
    <el-card class="box-card">
      <div style="font-size: 26px; text-align: left; margin-left: 1%; display: flex;">
        <div style="width: 50%;">已分配插件</div>
        <el-button icon="el-icon-search" ></el-button>
        <el-input v-model="searchInfo" placeholder="搜索插件" style="width: 20%;"></el-input>
        <el-button type="primary" style="margin-left: 8%;" @click="addPluginButton()"><i class="el-icon-plus"></i>添加插件</el-button>
      </div>
    </el-card>
    <el-table :data="svc_plugin_list" style="width: 100%">
      <el-table-column type="index"></el-table-column>
      <el-table-column width="60">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" @change="updatePluginStatus(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column width="50">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-view" id="eye-button" @click="editPlugin(scope.row)"></el-button>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="consumer" label="消费者"></el-table-column>
      <el-table-column prop="updateTime" label="修改时间"></el-table-column>
      <el-table-column prop="action" label="操作">
        <template slot-scope="scope">
          <el-popconfirm title="确定删除该服务吗？" @confirm="deletePlugin(scope.row)">
            <el-button type="danger" plain slot="reference">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="编辑key-auth插件" :visible.sync="keyAuthDialogVisible" width="50%" :before-close="handleEditClose" :modal-append-to-body="false">
      <el-form :model="key_auth_plugin" status-icon ref="ruleForm" label-width="100px" class="form">
        <el-form-item label="key名称" prop="pass">
           <div style="margin-left: 1%;">
             <el-input v-model="key_auth_plugin.keyName" autocomplete="off"></el-input>
             <el-divider class="divider"></el-divider>
             <div style="text-align: left; color: #909399;">插件将根据此关键字，尝试在请求中读取访问凭据</div>
           </div>
         </el-form-item>
         <el-form-item label="凭据" prop="pass">
           <div style="margin-left: 1%;">
             <el-input v-model="key_auth_plugin.keyPwd" autocomplete="off" show-password></el-input>
             <el-divider class="divider"></el-divider>
             <div style="text-align: left; color: #909399;">需要通过该凭据及对应的key访问该接口</div>
           </div>
         </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="keyAuthDialogVisible = false;">取 消</el-button>
        <el-button type="primary" @click="updateKeyAuth(key_auth_plugin)">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="编辑访问控制列表ACL" :visible.sync="aclDialogVisible" width="50%" :before-close="handleEditClose" :modal-append-to-body="false">
      <el-form :model="acl_plugin" status-icon ref="ruleForm" label-width="100px" class="form">
        <el-form-item label="白名单" >
          <el-tag :key="tag.userId" type="success" v-for="tag in acl_plugin.whitelist" closable @close="deleteWhiteListTag(tag)">{{tag.value}}</el-tag>
          <el-autocomplete class="input-new-tag" v-if="addWhiteListVisible" v-model="inputWhiteListUsername" :fetch-suggestions="querySearch" :trigger-on-focus="false"
           @select="handleWhiteListInputConfirm" @keyup.enter.native="handleWhiteListEnterConfirm" ref="autoCompleteWhitelistInput"></el-autocomplete>
          <!-- @keyup.enter.native="handleWhiteListInputConfirm"  处理回车
               @blur="handleWhiteListInputConfirm"                处理点击界外 -->
          <el-button v-else size="small" @click="showWhiteListInput">+ New</el-button>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left; color: #909399;">提示：需要选择一个具体用户，选择用户或按enter结束输入</div>
        </el-form-item>
        <el-form-item label="黑名单">
          <el-tag :key="tag.userId" type="danger" v-for="tag in acl_plugin.blacklist" closable @close="deleteBlackListTag(tag)">{{tag.value}}</el-tag>
          <el-autocomplete class="input-new-tag" v-if="addBlackListVisible" v-model="inputBlackListUsername" :fetch-suggestions="querySearch" :trigger-on-focus="false"
           @select="handleBlackListInputConfirm" @keyup.enter.native="handleBlackListEnterConfirm" ref="autoCompleteBlacklistInput"></el-autocomplete>
          <el-button v-else size="small" @click="showBlackListInput">+ New</el-button>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left; color: #909399;">提示：需要选择一个具体用户，选择用户或按enter结束输入</div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="aclDialogVisible = false; preloadPluginList(groupId);">取 消</el-button>
        <el-button type="primary" @click="updateAcl">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="编辑rate-limit插件" :visible.sync="rateLimitDialogVisible" width="50%" :before-close="handleEditClose" :modal-append-to-body="false" :style="{'padding-top': 0}">
      <div class="plugin-describe-text">启用访问速率控制插件时，数据服务将在网关内限制对当前数据服务的访问频率，以提高数据服务的安全性</div>
      <el-form :model="rate_limit_plugin" status-icon ref="ruleForm" label-width="100px" class="form">
        <el-form-item label="访问速率控制" style="margin-top: 20px;">
          <el-input v-model="rate_limit_plugin.rateLimit" autocomplete="off"></el-input>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left; color: #909399;">输入一个数字，表示限制该服务内接口一段时间内被访问的次数</div>
        </el-form-item>
        <el-form-item label="时间窗口">
          <el-input v-model="rate_limit_plugin.timeWindow" autocomplete="off"></el-input>
          <el-divider class="divider"></el-divider>
          <div style="text-align: left; color: #909399;">输入一个数字，表示访问速率限制的时间间隔</div>
        </el-form-item>
      </el-form>
      <el-button @click="rateLimitDialogVisible = false;">取 消</el-button>
      <el-button type="primary" @click="updateRateLimit">确 定</el-button>
    </el-dialog>

    <el-dialog title="编辑dynamic-expansion插件" :visible.sync="dynamicExpansionDialogVisible" width="50%" :before-close="handleEditClose" :modal-append-to-body="false">
      <div class="plugin-describe-text">启用动态扩展插件时，数据服务会根据现有节点的资源占用情况动态调整节点数量，以保证当前数据服务的高可用性。</div>
      <el-form :model="dynamic_expansion_plugin" status-icon ref="ruleForm" label-width="100px" class="form">
        <el-form-item label="动态扩展范围" style="margin-top: 20px;">
          <el-slider v-model="podNumRange" range show-stops :max="10" :min="1"></el-slider>
        </el-form-item>
      </el-form>
      <div style="margin-top: 20px;">
        <el-button @click="dynamicExpansionDialogVisible = false;">取 消</el-button>
        <el-button type="primary" @click="updateDynamicExpansion">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="可添加插件" :visible.sync="addPluginDialogVisible" width="50%" :before-close="handleAddClose" :modal-append-to-body="false">
      <el-table :data="notAllocatedPlugin" style="width: 100%" ref="multipleTable" tooltip-effect="dark" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="插件名" width="180"></el-table-column>
        <el-table-column prop="desc" label="描述" width="420"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addPluginDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addPlugin()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'servicePlugin',
    components: {

    },
    data () {
      return {
        searchInfo: "",
        svc_plugin_list: [],
        dialogVisible: false,
        keyAuthDialogVisible: false,
        aclDialogVisible: false,
        rateLimitDialogVisible: false,
        dynamicExpansionDialogVisible: false,
        addPluginDialogVisible: false,
        addWhiteListVisible: false,
        addBlackListVisible: false,
        cur_plugin: {},
        key_auth_plugin: {},
        acl_plugin: {},
        rate_limit_plugin: {},
        dynamic_expansion_plugin: {},
        podNumRange: [],
        acl_id: 0,
        user: [],
        suggestions: [],
        inputWhiteListUsername: "",
        inputBlackListUsername: "",

        notAllocatedPlugin: [],
        multipleSelection: [],
      }
    },
    created() {
      this.groupId = this.$route.params.svcId;
      this.preloadUserInputAdvice();
      this.preloadPluginList(this.groupId);
    },
    methods: {
      editPlugin(plugin) {
        console.log(plugin)
        if (plugin.name == "key-auth") {
          this.keyAuthDialogVisible = true;
        } else if (plugin.name == "acl") {
          this.aclDialogVisible = true;
        } else if (plugin.name == "rate-limit") {
          this.rateLimitDialogVisible = true;
        } else if (plugin.name == "dynamic-expansion") {
          this.dynamicExpansionDialogVisible = true;
        }
      },
      handleEditClose(done) {
        this.$confirm('确认放弃编辑插件？')
          .then(_ => {
            done();
          }).catch(_ => {});
      },
      handleAddClose(done) {
        this.$confirm('确认放弃添加插件？')
          .then(_ => {
            done();
          }).catch(_ => {});
      },
      deleteWhiteListTag(tag) {
        this.acl_plugin.whitelist.splice(this.acl_plugin.whitelist.indexOf(tag), 1);
      },
      deleteBlackListTag(tag) {
        this.acl_plugin.blacklist.splice(this.acl_plugin.blacklist.indexOf(tag), 1);
      },
      showWhiteListInput() {
        this.addWhiteListVisible = true;
        this.$nextTick(_ => {
          this.$refs.autoCompleteWhitelistInput.focus();
        });
      },
      showBlackListInput() {
        this.addBlackListVisible = true;
        this.$nextTick(_ => {
          this.$refs.autoCompleteBlacklistInput.focus();
        });
      },
      handleWhiteListInputConfirm(item) {
        let inputValue = item;
        if (!inputValue || this.acl_plugin.whitelist.some(function(item) {
          return JSON.stringify(item) == JSON.stringify(inputValue)
        })) {
          this.$message({
            message: "错误：不可重复添加相同用户",
            type: 'error'
          });
        } else {
          this.acl_plugin.whitelist.push(inputValue);
        }
        this.addWhiteListVisible = false;
        this.inputWhiteListUsername = '';
      },
      handleWhiteListEnterConfirm() {
        if (this.inputWhiteListUsername != "" && this.suggestions && this.suggestions.length > 0) {
          const firstSuggestion = this.suggestions[0];
          this.handleWhiteListInputConfirm(firstSuggestion);
        }
        this.addWhiteListVisible = false;
        this.inputWhiteListUsername = '';
        this.suggestions = [];
      },
      handleBlackListInputConfirm(item) {
        let inputValue = item;
        if (!inputValue || this.acl_plugin.blacklist.some(function(item) {
          return JSON.stringify(item) == JSON.stringify(inputValue)
        })) {
          this.$message({
            message: "错误：不可重复添加相同用户",
            type: 'error'
          });
        } else {
          this.acl_plugin.blacklist.push(inputValue);
        }
        this.addBlackListVisible = false;
        this.inputBlackListUsername = '';
      },
      handleBlackListEnterConfirm(){
        if (this.inputBlackListUsername != "" && this.suggestions && this.suggestions.length > 0) {
          const firstSuggestion = this.suggestions[0];
          this.handleBlackListInputConfirm(firstSuggestion);
        }
        this.addBlackListVisible = false;
        this.inputBlackListUsername = '';
        this.suggestions = [];
      },
      querySearch(queryString, cb) {
        var user = this.user;
        var results = queryString ? user.filter(this.createFilter(queryString)) : user;
        this.suggestions = results;
        cb(results);
      },
      createFilter(queryString) {
        return (user) => {
          return (user.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      preloadUserInputAdvice() {
        this.$get('api/svc/user/inputAdviseUserList').then(response => {
          if (response.data.code == 20000) {
            this.user = response.data.data.userList;
          }
        }).catch(error => {
          console.log(error);
        })
      },
      preloadPluginList(groupId) {
        this.$get('api/svc/group/pluginList', {
          "groupId": groupId,
        }).then(response => {
          if (response.data.code == 20000) {
            this.svc_plugin_list = response.data.data.pluginList;
            for (let i = 0; i < this.svc_plugin_list.length; i++) {
              if(this.svc_plugin_list[i].name == "key-auth") {
                this.key_auth_plugin = this.svc_plugin_list[i].pluginConfig;
              } else if (this.svc_plugin_list[i].name == 'acl') {
                this.acl_plugin = this.svc_plugin_list[i].pluginConfig;
                this.acl_id = this.svc_plugin_list[i].pluginId;
              } else if (this.svc_plugin_list[i].name == 'rate-limit') {
                this.rate_limit_plugin = this.svc_plugin_list[i].pluginConfig;
              } else if (this.svc_plugin_list[i].name == "dynamic-expansion") {
                this.dynamic_expansion_plugin = this.svc_plugin_list[i].pluginConfig;
                this.podNumRange.push(this.dynamic_expansion_plugin.minValue);
                this.podNumRange.push(this.dynamic_expansion_plugin.maxValue);
              }
            }
          }
        }).catch(error => {
          console.log(error);
        })
      },
      updateKeyAuth(key_auth_plugin) {
        this.$put('api/svc/group/updateKeyAuthPlugin', {
          "groupId": this.groupId,
          "keyName": key_auth_plugin.keyName,
          "keyPwd":  key_auth_plugin.keyPwd
        }).then(response => {
          if (response.data.code == 20000) {
            this.$message({
              message: "成功：" + response.data.message,
              type: 'success'
            });
            this.keyAuthDialogVisible = false;
            this.preloadPluginList(this.groupId);
            this.$forceUpdate();
          }
        }).catch(error => {
          console.log(error);
        })
      },
      updateAcl() {
        this.$put('api/svc/group/updateAclPlugin', {
          'aclId': this.acl_id,
          'whiteList': this.acl_plugin.whitelist,
          'blackList': this.acl_plugin.blacklist
        }).then(response => {
          if (response.data.code == 20000) {
            this.$message({
              message: "成功：" + response.data.message,
              type: 'success'
            });
            this.aclDialogVisible = false;
            this.preloadPluginList(this.groupId);
            this.$forceUpdate();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      updateRateLimit() {
        this.$put('api/svc/group/updateRateLimitPlugin', {
          'groupId': this.groupId,
          'rateLimit': this.rate_limit_plugin.rateLimit,
          'timeWindow': this.rate_limit_plugin.timeWindow
        }).then(response => {
          if (response.data.code == 20000) {
            this.$message({
              message: "成功：" + response.data.message,
              type: 'success'
            });
            this.rateLimitDialogVisible = false;
            this.preloadPluginList(this.groupId);
            this.$forceUpdate();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      updateDynamicExpansion() {
        this.$put('api/svc/group/updateDynamicExpansionPlugin', {
          'groupId': this.groupId,
          'minValue': this.podNumRange[0],
          'maxValue': this.podNumRange[1]
        }).then(response => {
          if (response.data.code == 20000) {
            this.$message({
              message: "成功：" + response.data.message,
              type: 'success'
            });
            this.dynamicExpansionDialogVisible = false;
            this.preloadPluginList(this.groupId);
            this.$forceUpdate();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      updatePluginStatus(row) {
        if (row.name == "key-auth") {
          this.$put('api/svc/group/updateKeyAuthStatus', {
            "keyAuthId": row.pluginId,
            "status": row.status ? 1 : 0,
          }).then(response => {
            if (response.data.code == 20000) {
              this.$message({
                message: "成功：" + response.data.message,
                type: 'success'
              });
              this.preloadPluginList(this.groupId);
              this.$forceUpdate();
            }
          }).catch(error => {
            console.log(error);
          });
        } else if (row.name == "acl") {
          this.$put('api/svc/group/updateAclStatus', {
            "aclId": row.pluginId,
            "status": row.status ? 1 : 0,
          }).then(res => {
            if (res.data.code == 20000) {
              this.$message({
                message: "成功：" + res.data.message,
                type: 'success'
              });
              this.preloadPluginList(this.groupId);
              this.$forceUpdate();
            }
          }).catch(error => {
            console.log(error);
          });
        } else if (row.name == "rate-limit") {
          this.$put('api/svc/group/updateRateLimitStatus', {
            'rateLimitId': row.pluginId,
            'status': row.status ? 1 : 0,
          }).then(res => {
            if (res.data.code == 20000) {
              this.$message({
                message: "成功：" + res.data.message,
                type: 'success'
              });
              this.preloadPluginList(this.groupId);
              this.$forceUpdate();
            }
          }).catch(error => {
            console.log(error);
          });
        } else if (row.name == "dynamic-expansion") {
          this.$put('api/svc/group/updateDynamicExpansionStatus', {
            "groupId": this.groupId,
            'dynamicExpansionId': row.pluginId,
            'status': row.status ? 1 : 0,
          }).then(res => {
            if (res.data.code == 20000) {
              this.$message({
                message: "成功：" + res.data.message,
                type: 'success'
              });
              this.preloadPluginList(this.groupId);
              this.$forceUpdate();
            } else {
              this.$message({
                message: "警告：" + res.data.message,
                type: 'warning'
              });
              this.preloadPluginList(this.groupId);
              this.$forceUpdate();
            }
          }).catch(error => {
            console.log(error);
          });
        }
      },
      deletePlugin(row){
        if (row.name == "key-auth") {
          this.$delete('api/svc/group/deleteKeyAuthPlugin?groupId=' + this.groupId).then(res => {
            if (res.data.code == 20000) {
              this.preloadPluginList(this.groupId);
              this.$message({
                showClose: true,
                message: res.data.message,
                type: 'success'
              });
            }
          }).catch(err => {
            this.$message.error(err.response.data.message);
          });
        } else if (row.name == "acl") {
          this.$delete('api/svc/group/deleteAclPlugin?groupId=' + this.groupId).then(res => {
            if (res.data.code = 20000) {
              this.preloadPluginList(this.groupId);
              this.$message({
                showClose: true,
                message: res.data.message,
                type: 'success'
              });
            }
          }).catch(err => {
            this.$message.error(err.response.data.message);
          });
        } else if (row.name == "rate-limit") {
          this.$delete('api/svc/group/deleteRateLimitPlugin?groupId=' + this.groupId).then(res => {
            if (res.data.code == 20000) {
              this.preloadPluginList(this.groupId);
              this.$message({
                showClose: true,
                message: res.data.message,
                type: 'success'
              });
            }
          }).catch(err => {
            this.$message.error(err.response.data.message);
          });
        } else if (row.name == "dynamic-expansion") {
          this.$delete('api/svc/group/deleteDynamicExpansionPlugin?groupId=' + this.groupId).then(res => {
            console.log(res);
            if (res.data.code == 20000) {
              this.preloadPluginList(this.groupId);
              this.$message({
                showClose: true,
                message: res.data.message,
                type: 'success'
              });
            }
          }).catch(err => {
            this.$message.error(err.response.data.message);
          });
        } else {
          this.$message.error("该插件的删除操作待实现");
        }
      },
      addPluginButton() {
        this.addPluginDialogVisible = true;
        this.$get('api/svc/group/unallocatedPluginList', {
          "groupId": this.groupId,
        }).then(res => {
          if (res.data.code = 20000) {
            this.notAllocatedPlugin = res.data.data.pluginAllocateOutlineList;
          }
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      addPlugin() {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          switch (this.multipleSelection[i].name) {
            case 'key-auth':
              this.$post('api/svc/group/addKeyAuthPlugin', {
                'groupId': this.groupId,
              }).then(res => {
                if (res.data.code == 20000) {
                  this.preloadPluginList(this.groupId);
                  this.addPluginDialogVisible = false;
                  this.$message({
                    showClose: true,
                    message: this.data.message,
                    type: 'success'
                  });
                }
              }).catch(err => {
                this.$message.error(err.response.data.message);
              });
              break;
            case 'acl':
              this.$post('api/svc/group/createAclPlugin', {
                'groupId': this.groupId
              }).then(res => {
                if (res.data.code == 20000) {
                  this.preloadPluginList(this.groupId);
                  this.addPluginDialogVisible = false;
                  this.$message({
                    showClose: true,
                    message: this.data.message,
                    type: 'success'
                  });
                }
              }).catch(err => {
                this.$message.error(err.response.data.message);
              });
              break;
            case 'rate-limit':
              this.$post('api/svc/group/createRateLimitPlugin', {
                'groupId': this.groupId
              }).then(res => {
                if (res.data.code == 20000) {
                  this.preloadPluginList(this.groupId);
                  this.addPluginDialogVisible = false;
                  this.$message({
                    showClose: true,
                    message: this.data.message,
                    type: 'success'
                  });
                }
              }).catch(err => {
                this.$message.error(err.response.data.message);
              });
              break;
            case 'dynamic-expansion':
              this.$post('api/svc/group/createDynamicExpansionPlugin', {
                'groupId': this.groupId
              }).then(res => {
                if (res.data.code == 20000) {
                  this.preloadPluginList(this.groupId);
                  this.addPluginDialogVisible = false;
                  this.$message({
                    showClose: true,
                    message: this.data.message,
                    type: 'success'
                  });
                }
              }).catch(err => {
                this.$message.error(err.response.data.message);
              });
              break;
            default:
              this.$message.error('this is an unkonwn plugin.');
          }
        }
      },

    }
  }
</script>

<style scoped>
.serviceplugin_all {
  margin-left: 5%;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.plugin-describe-text {
  text-align: left;
  color: #909399;
  line-height: 1.2;
  width: 70%;
  margin-left: 15%;
  font-size: 16px;
}
</style>
