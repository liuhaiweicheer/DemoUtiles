package com.mybatis.demo.controller;

import com.mybatis.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhw
 * @date 2020/10/8
 */
@Slf4j
@RestController
public class UseController {

    @Autowired
    UserService userService;

    @GetMapping("/getVerifyHash")
    public String getVerifyHash(@RequestParam("sId")Integer sId, @RequestParam("userId")Integer userId){
        String hash = null;
        try {
            hash = userService.getVerifyHash(sId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("请求抢购验证hash值为：%s",hash);
    }

}
