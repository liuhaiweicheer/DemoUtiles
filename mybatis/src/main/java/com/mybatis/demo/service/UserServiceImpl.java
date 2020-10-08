package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.entity.User;
import com.mybatis.demo.mapper.UserMapper;
import com.mybatis.demo.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author lhw
 * @date 2020/10/8
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private static final String SALT = "randomStringSalt";

    private static final int ALLOW_COUNT = 10;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StockService stockService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public String getVerifyHash(Integer sId, Integer userId) throws Exception {
        log.info("【验证是否在抢单时间内】");

        // 检查用户合法性
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            throw new Exception("用户不存在");
        }
        log.info("【用户信息】: {}",user.toString());

        Stock stock = stockService.checkStockForUpdate(sId);
        if(stock == null){
            throw new Exception("商品不存在");
        }
        log.info("【商品信息】: {}",stock.toString());

        // 生成 hash
        String verify = SALT + sId + user;
        String verifyHash = DigestUtils.md5DigestAsHex(verify.getBytes());

        //  把 hash 和商品信息 存到 Redis
        String hashKey = CacheKey.HASH_KEY.getKey() + "_" + sId + "_" +userId;
        redisTemplate.opsForValue().set(hashKey, verifyHash, 3600, TimeUnit.SECONDS);
        log.info("【Redis写入】: {}",hashKey, verifyHash);

        return verifyHash;
    }

    @Override
    public int addUserCount(Integer userId) {
        String limitKey = CacheKey.LIMIT_KEY.getKey() + "_" + userId;
        String limitNum = redisTemplate.opsForValue().get(limitKey);
        int limit = -1;
        if (limitNum == null) {
            redisTemplate.opsForValue().set(limitKey, "1", 3600, TimeUnit.SECONDS);
        } else {
            limit = Integer.parseInt(limitNum) + 1;
            redisTemplate.opsForValue().set(limitKey, String.valueOf(limit), 3600, TimeUnit.SECONDS);
        }
        return limit;
    }

    @Override
    public boolean getUserIsBanned(Integer userId) {
        String limitKey = CacheKey.LIMIT_KEY.getKey() + "_" + userId;
        String limitNum = redisTemplate.opsForValue().get(limitKey);
        if(limitNum == null){
            log.error("【该用户不存在】");
            return true;
        }
        return Integer.parseInt(limitNum) > ALLOW_COUNT;
    }
}
