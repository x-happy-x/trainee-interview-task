package ru.interview.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    public static ResponseEntity<?> build(int status, HttpStatus httpStatus, Object responseObj, String message) {
        Map<String, Object> map = new HashMap<>();

        map.put("status", status);
        map.put("message", message);
        if (responseObj != null) {
            map.put("response", responseObj);
            if (responseObj instanceof Collection) {
                map.put("count", ((Collection<?>) responseObj).size());
            }
        }

        return new ResponseEntity<>(map, httpStatus);
    }

    public static ResponseEntity<?> build(HttpStatus httpStatus, Object responseObj) {
        int status;
        if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
            status = -1;
        } else {
            status = 1;
        }
        return build(status, httpStatus, responseObj, status == 1 ? "success" : httpStatus.getReasonPhrase());
    }

    public static ResponseEntity<?> build(Object responseObj) {
        return build(HttpStatus.OK, responseObj);
    }

    public static ResponseEntity<?> error(int status, HttpStatus httpStatus, Object responseObj, Throwable throwable) {
        return build(status, httpStatus, responseObj, throwable.getMessage());
    }

    public static ResponseEntity<?> error(int status, HttpStatus httpStatus, Throwable throwable) {
        return error(status, httpStatus, null, throwable);
    }

    public static ResponseEntity<?> error(HttpStatus httpStatus, Throwable throwable) {
        return error(-1, httpStatus, throwable);
    }

}