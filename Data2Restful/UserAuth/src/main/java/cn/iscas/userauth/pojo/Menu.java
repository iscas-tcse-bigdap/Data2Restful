package cn.iscas.userauth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单表(Menu)实体类
 *
 * @author wbq
 * @since 2023-10-24 15:30:08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "menu")
public class Menu {
    @TableId(value = "menu_id", type = IdType.AUTO)
    private int menuId;

    private String menuName;
    private String perms;
    private int status;
}
