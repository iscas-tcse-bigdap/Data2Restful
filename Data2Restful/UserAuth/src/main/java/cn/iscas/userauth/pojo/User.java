package cn.iscas.userauth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@title User
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/2 13:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("User")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private int userId;

    private String username;
    private String password;
    private String userType;

}
