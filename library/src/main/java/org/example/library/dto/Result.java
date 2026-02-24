package org.example.library.dto;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.code = 200;
        result.message = "成功";
        result.data = data;
        return result;
    }

    public static Result success(String message, Object data) {
        Result result = new Result();
        result.code = 200;
        result.message = message;
        result.data = data;
        return result;
    }

    public static Result success(String message) {
        Result result = new Result();
        result.code = 200;
        result.message = message;
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.code = 400;
        result.message = message;
        return result;
    }
}
