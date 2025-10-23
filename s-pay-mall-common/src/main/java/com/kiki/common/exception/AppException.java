package com.kiki.common.exception;

public class AppException extends RuntimeException{
    //rialVersionUID 是 Java 中用于 序列化（Serialization） 机制的一个重要字段，它的主要作用是：
    //在对象序列化和反序列化过程中，确保发送方和接收方的类版本兼容，防止因类结构变化导致 
    // 如果没有 serialVersionUID，Java 会根据类的结构 自动生成一个（通过哈希算法），但这个值对类的任何改动都非常敏感。
    private static final long serialVersionUID = 8653090271840061986L;

    /**
     * 异常码
     */
    private String code;

    /**
     * 异常信息
     */
    private String info;

    public AppException(String code) {
        this.code = code;
    }

    public AppException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public AppException(String code, String message) {
        this.code = code;
        this.info = message;
    }

    public AppException(String code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

    @Override
    public String toString() {
        return "cn.bugstack.common.exception.AppException{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

}
