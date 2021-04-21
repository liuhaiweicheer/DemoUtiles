package com.mybatis.demo.service;

/**
 * @author lhw
 * @date 2020/10/8
 */
public interface UserService {

    String getVerifyHash(Integer sId, Integer userId) throws Exception;

    int addUserCount(Integer userId);

    boolean getUserIsBanned(Integer userId);

}
