package com.jobtrackingapp.api_gateway.exception;

import com.jobtrackingapp.api_gateway.model.ExceptionMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestControllerAdvice
@Log4j2
public class GlobalException {





    @ExceptionHandler({SessionExpired.class})
    public ResponseEntity<ExceptionMessage> sessionExpired(SessionExpired e, HttpServletRequest request) {

      //  log.info("SessionExpired handler");
        String path = request.getRequestURI();

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "JWT expired",
                HttpStatus.UNAUTHORIZED.value(),
                new Date().toString(),
                e.getMessage(),
                path
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionMessage);

    }





}
