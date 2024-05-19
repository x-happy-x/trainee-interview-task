package ru.interview.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    public static ResponseEntity<?> build(HttpStatus httpStatus, Object responseObj, String path, String error) {
        Map<String, Object> map = new HashMap<>();

        map.put("status", httpStatus.value());
        if (error != null) {
            map.put("error", error);
        } else if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError()) {
            map.put("error", httpStatus.getReasonPhrase());
        }

        if (map.containsKey("error")) {
            map.put("timestamp", ZonedDateTime.now().toLocalDateTime().toString());
            if (path != null) map.put("path", path);
        }

        if (responseObj != null) {
            map.put("response", responseObj);
            if (responseObj instanceof Collection) {
                map.put("count", ((Collection<?>) responseObj).size());
            }
        }

        return new ResponseEntity<>(map, httpStatus);
    }

    public static ResponseEntity<?> build(HttpStatus httpStatus, Object responseObj, String path) {
        return build(httpStatus, responseObj, path, null);
    }

    public static ResponseEntity<?> ok(Object responseObj) {
        return build(HttpStatus.OK, responseObj, null);
    }

    public static ResponseEntity<?> error(HttpStatus httpStatus, Object responseObj, Throwable throwable, String path) {
        return build(httpStatus, responseObj, path, throwable.getMessage());
    }

    public static ResponseEntity<?> error(HttpStatus httpStatus, Object responseObj, Throwable throwable) {
        return error(httpStatus, responseObj, throwable, null);
    }

    public static ResponseEntity<?> error(HttpStatus httpStatus, Throwable throwable, String path) {
        return error(httpStatus, null, throwable, path);
    }

}