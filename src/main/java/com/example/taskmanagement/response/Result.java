package com.example.taskmanagement.response;


import com.example.taskmanagement.enums.ResultCodeEnum;

public class Result<T> {


    protected T data;


    protected Integer code;

    protected String message;



    public Result(T data) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.message = ResultCodeEnum.SUCCESS.getDesc();
        this.data = data;
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(T data, String message) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.data = data;
        this.message = message;
    }




}
