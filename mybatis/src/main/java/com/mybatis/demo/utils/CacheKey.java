package com.mybatis.demo.utils;

/**
 * @author lhw
 * @date 2020/10/8
 */
public enum CacheKey {

    HASH_KEY("user_hash"),
    LIMIT_KEY("user_limit");

    private String key;

    private CacheKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

}
