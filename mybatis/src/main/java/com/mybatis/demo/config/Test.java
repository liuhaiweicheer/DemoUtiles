package com.mybatis.demo.config;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhw
 * @date 2020/10/17
 */
public class Test {
    public static void main(String[] args) {
        List list = new ArrayList<>();
        list.add("aaaaaaaaaaaa");
        list.add("bbbbbbbbbbbbbb");
        list.add("cccccccccccccc");
        Map map = (Map) list.get(1);
        System.out.println(map);
    }
}
