package com.aop.aop.exception;

/**
 * @author lhw
 * @date 2020/10/3
 */
public class AopException extends RuntimeException{
    private String msg;
    public AopException(){
    }
    public AopException(String msg){
        super(msg);    //  把异常信息传递过去，才可以使用 getMessage获取到异常信息
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
