package com.jobtrackingapp.api_gateway.exception;

import com.jobtrackingapp.api_gateway.model.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class SessionExpired extends RuntimeException{

    private ExceptionMessage exceptionMessage;



    public SessionExpired(String   message)
    {
        super(message);
    }
    public SessionExpired(ExceptionMessage  message)
    {
        this.exceptionMessage=message;
    }
    public SessionExpired(String message,ExceptionMessage exceptionMessage)
    {
        super(message);
        this.exceptionMessage=exceptionMessage;
    }


    public ExceptionMessage getExceptionMessage()
    {
        return  exceptionMessage;
    }

}

