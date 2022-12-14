package com.fuyin.boot.service;

import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.result.ResponseResult;

/**
 * @author 何义祈安
 */
public interface  LoginService {
    ResponseResult login(UserDO userDO);

    ResponseResult logout();

    int setUserLastLoginTime(UserDO userDO);

    String test();
}
