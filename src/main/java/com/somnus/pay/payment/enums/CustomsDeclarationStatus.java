package com.somnus.pay.payment.enums;

/**
 * @description: ${TODO}
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/28
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/28       bai          1.0             Why & What is modified
 */
public enum CustomsDeclarationStatus {
    FAILURE(0,"报关失败"),
    SUCCEED(1,"报关成功"),
    NOTYET(2,"暂未报关");

    private Integer value;
    private String message;
    CustomsDeclarationStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static CustomsDeclarationStatus getCustomsDeclarationStatusByValue(String value){
        CustomsDeclarationStatus customsDeclarationStatus = null;
        if (value != null){
            for (CustomsDeclarationStatus type : CustomsDeclarationStatus.values()) {
                if (type.value == Integer.parseInt(value))
                    customsDeclarationStatus = type;
            }
        }
        return customsDeclarationStatus;
    }

    public static CustomsDeclarationStatus messageOf(String message){
        CustomsDeclarationStatus customsDeclarationStatus = null;
        if (message != null){
            for (CustomsDeclarationStatus type : CustomsDeclarationStatus.values()) {
                if (type.message.equalsIgnoreCase(message))
                    customsDeclarationStatus = type;
            }
        }
        return customsDeclarationStatus;
    }
}
