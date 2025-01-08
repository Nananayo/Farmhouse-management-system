package com.farmhouseSystem.common;

import lombok.Data;

import java.io.Serializable;
@Data
public class R<T> implements Serializable {
    private Integer code;
    private String msg;
    private Object data;
    private Integer power;
    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.data= object;
        r.code = 1;
        r.power = 0;
        return r;
    }
    public static <T> R<T> error(String msg){
        R r = new R<T>();
        r.data= msg;
        r.code = 0;
        return r;
    }
}
