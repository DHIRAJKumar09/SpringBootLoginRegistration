package com.tcs.registration_login.userauthentication.config;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class RoutesController {
    private final RequestMappingHandlerMapping mapping;
    public RoutesController(RequestMappingHandlerMapping mapping) { this.mapping = mapping; }
    @GetMapping("/routes")
    public List<String> routes() {
        return mapping.getHandlerMethods().entrySet().stream()
                .map(e -> {
                    RequestMappingInfo info = e.getKey();
                    return info.getMethodsCondition().getMethods() + " " + info.getPatternsCondition().getPatterns();
                }).collect(Collectors.toList());
    }

}
