package com.jobtrackingapp.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class ValidatorRoute {


List<String> openApiEndpoints=List.of(
        "api/auth/login",
        "api/auth/register",
        "api/auth/validate"
);

public Predicate <ServerHttpRequest>isSecureEndpoint = serverHttpRequest ->

    openApiEndpoints.stream().noneMatch(uri->serverHttpRequest.getURI().getPath().contains(uri));


}
