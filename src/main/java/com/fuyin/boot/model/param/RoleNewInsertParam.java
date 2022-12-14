package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName: RoleNewInsertParam
 * @Author: 何义祈安
 * @Description: 角色管理新增按钮接口传入的参数
 * @Date: 2022/11/19 16:35
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleNewInsertParam {
    /**
    * 角色名称，对应数据库name
    **/
    private String roleName;
    /**
     * 角色描述，对应数据库descprition
     **/
    private String roleDesc;
    /**
     * 权限名称（中文），对应数据库descprition，是个集合
     **/
    private List<String> resource;
}
