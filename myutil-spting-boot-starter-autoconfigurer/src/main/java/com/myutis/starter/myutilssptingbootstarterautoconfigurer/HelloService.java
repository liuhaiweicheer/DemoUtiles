package com.myutis.starter.myutilssptingbootstarterautoconfigurer;

/**
 * @author lhw
 * @date 2020/10/8
 */
public class HelloService {

    HelloProperties helloProperties;

    public HelloProperties getHelloProperties() {
        return helloProperties;
    }

    public void setHelloProperties(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }

    public String sayHello(String name){
        return helloProperties.getPrefix() + "My DIY stater" + helloProperties.getSuffix();
    }

}
