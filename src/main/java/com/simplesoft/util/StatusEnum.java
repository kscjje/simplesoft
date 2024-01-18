package com.simplesoft.util;

/**
 * 
 * @author 		kscjje
 * @description	HTTP 상태 정의
 * @since		2024.01.18
 */
public enum StatusEnum {
	
	OK(200, "OK"),
    BAD_REQUEST(400, "BAD REQUEST"),
    NOT_FOUND(404, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");

    int code;
    String message;

    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
