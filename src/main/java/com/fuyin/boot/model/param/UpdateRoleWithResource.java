package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName: UpdateRoleWithResource
 * @Author: 何义祈安
 * @Description: 角色管理中提交角色编辑参入参数
 * @Date: 2022/11/19 16:41
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateRoleWithResource {
    /**
     * 角色id
     **/
    private Long roleId;
    /**
     * 角色名
     **/
    private String roleName;
    /**
     * 权限名称（中文），对应数据库descprition，是个集合
     **/
    private List<String> resource;

}
