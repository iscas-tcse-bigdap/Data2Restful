<template>
  <div class="login">
    <el-card class="login__card">
      <div slot="header">Login</div>
      <el-form>
        <el-form-item label="User name">
          <el-input v-model="userName"></el-input>
        </el-form-item>
        <el-form-item label="Password">
          <el-input type="password" v-model="password"></el-input>
          <el-button type="text" class="register" @click="registerVisible = true">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="login">Login</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-dialog title="DataCenter Register" :visible.sync="registerVisible" width="50%" :modal-append-to-body="false">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="User name">
          <el-input v-model="registerUserName"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
		      <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
        </el-form-item>
		</el-form>
	</el-dialog>
  </div>
</template>

<script>
import jwt_decode from "jwt-decode";

export default{
  name: 'Login',
  data () {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
    return {
      registerVisible: false,
      userName: 'wbq',
      registerUserName: 'test-user',
      password: '',
      ruleForm: {
          pass: '',
          checkPass: ''
        },
        rules: {
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ]
        }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.register();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    login () {
      this.$post('user/login', {
        "username": this.userName,
        "password": this.password
      }).then(response => {
        if (response.data.code == 20000) {
          let res = {
            token: response.data.data.token,
            pow: jwt_decode(response.data.data.token),
          };
          sessionStorage.setItem("token", res.token);
          sessionStorage.setItem("pow", res.pow);
          this.$store.commit("LOGIN", res);
          console.log(this.$store.state);
          this.$message({
            showClose: true,
            message: '登录成功',
            type: 'success'
          });
          this.toApiCenter();
        }
      })
    },
    register(){
      // this.axios.post('/api/user/register',{
      //   name: this.registerUserName,
      //   password: this.ruleForm.pass,
      // },{ emulateJSON:true })
      // .then((response) =>{
      //   if(response.data.message == '成功'){
      //     this.registerVisible=false;
      //     this.$alert('欢迎使用数据中台!');
      //   }else{
      //     console.log(response)
      //     this.$alert(response.data.message)
      //   }
      // })
    },
    toApiCenter() {
      this.$router.push("/api");
    },
  }
}
</script>

<style>
  .login{
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 902px;
    margin-top: -60px;
    background-image: url('../assets/OIP.jpg');
    background-size: cover;
  }
  .login__card{
    width: 300px;
  }
  .register{
	  margin-left: 210px;
  }
</style>
