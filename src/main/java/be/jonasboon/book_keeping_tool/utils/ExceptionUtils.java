package be.jonasboon.book_keeping_tool.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ExceptionUtils {

    public static String toJson(Exception exception) {
        return String.format("{ error: %s }", exception.getMessage());
    }

    public static ResponseEntity<String> sendExceptionResponse(Exception exception, int statusCode) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(statusCode).body(toJson(exception));
    }
}
