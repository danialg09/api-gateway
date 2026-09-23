package com.skyfall.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class GatewayRoutesConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayRoutesConfig.class);

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctions() {
        return route("auth-route")
                .POST("/api/auth/**", http())
                .before(uri("http://bank-system:8080"))
                .filter((request, next) -> {
                    log.info("Bank request: {} {}", request.method(), request.uri());
                    return next.handle(request);
                })
                .build();
    }
}
