package com.example.taskmanagement.enums;

public enum ResultCodeEnum {
    SUCCESS(1, "success"),
    APPLICATION_ERROR(9000, "application error"),
    VALIDATE_ERROR(9001, "validate error");


    private final int code;
    private final String desc;

    ResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
