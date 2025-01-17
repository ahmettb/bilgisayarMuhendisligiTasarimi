package com.jobtrackingapp.authentacition.exception;


import com.jobtrackingapp.authentacition.model.response.ExceptionMessage;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
@Log4j2
public class GlobalException {




    @ExceptionHandler({RoleNotFound.class})
    public ResponseEntity<ExceptionMessage> roleNotFound(RoleNotFound e, HttpServletRequest request) {

        String path = request.getRequestURI();

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "NOT FOUND",
                HttpStatus.NOT_FOUND.value(),
                new Date().toString(),
                e.getMessage(),
                path
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionMessage);

    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<ExceptionMessage> loginException(LoginException e, HttpServletRequest request) {

        String path = request.getRequestURI();

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "Incorrect",
                HttpStatus.UNAUTHORIZED.value(),
                new Date().toString(),
                e.getMessage(),
                path
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionMessage);

    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ExceptionMessage> expiredJwtToken(ExpiredJwtException e, HttpServletRequest request) {

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





    @ExceptionHandler({EmailAlreadyExist.class})
    public ResponseEntity<ExceptionMessage> emailAlreadyExist(EmailAlreadyExist e, HttpServletRequest request) {


    String path = request.getRequestURI();

    ExceptionMessage exceptionMessage = new ExceptionMessage(
            "ALREADY EXIST",
            HttpStatus.CONFLICT.value(),
            new Date().toString(),
            e.getMessage(),
            path
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionMessage);

}
    @ExceptionHandler({UserNotFound.class})
    public ResponseEntity<ExceptionMessage> userNotFound(UserNotFound e, HttpServletRequest request) {


        String path = request.getRequestURI();

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "NOT FOUND",
                HttpStatus.NOT_FOUND.value(),
                new Date().toString(),
                e.getMessage(),
                path
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionMessage);

    }

    @ExceptionHandler({UsernameAlreadyExist.class})
    public ResponseEntity<ExceptionMessage> usernameAlreadyExist(UsernameAlreadyExist e, HttpServletRequest request) {


        String path = request.getRequestURI();

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                "ALREADY EXIST",
                HttpStatus.NOT_FOUND.value(),
                new Date().toString(),
                e.getMessage(),
                path
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionMessage);

    }



}
