package com.websocket.websocket.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.rmi.Naming;

@Data
public class MsgEntity {

    @JSONField(name = "toUserId")
    private String toUserId;

    @JSONField(name = "contentText")
    private String message;

}
