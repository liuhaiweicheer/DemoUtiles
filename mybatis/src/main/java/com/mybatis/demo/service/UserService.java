package com.mybatis.demo.service;

/**
 * @author lhw
 * @date 2020/10/8
 */
public interface UserService {

    public String getVerifyHash(Integer sId, Integer userId) throws Exception;

    public int addUserCount(Integer userId);

    public boolean getUserIsBanned(Integer userId);

}
