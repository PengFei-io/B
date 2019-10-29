package com.baizhi.entities;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private Integer code;
    private String message;

    public ErrorMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
