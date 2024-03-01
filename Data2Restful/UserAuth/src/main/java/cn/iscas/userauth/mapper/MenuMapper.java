package cn.iscas.userauth.mapper;

import cn.iscas.userauth.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *@title MunuMapper
 *@description
 *@author wbq
 *@version 1.0
 *@create 2023/10/24 15:42
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(int userId);
}
