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
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutesConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayRoutesConfig.class);

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctions() {
        return route("bank-routes")
                .route(path(
                        "/api/auth/**",
                        "/api/admin/users/**",
                        "/api/cards/**",
                        "/api/transfer/**"
                ), http())
                .before(uri("http://bank-system:8080"))
                .filter((request, next) -> {
                    log.info("[GATEWAY -> BANK] {} {}", request.method(), request.uri().getPath());
                    return next.handle(request);
                })
                .route(path(
                        "/api/hotels/**",
                        "/api/rooms/**",
                        "/api/bookings/**",
                        "/api/statistics/**",
                        "/api/users/**"
                ), http())
                .before(uri("http://hotel-system:8080"))
                .filter((request, next) -> {
                    log.info("[GATEWAY -> HOTEL] {} {}", request.method(), request.uri().getPath());
                    return next.handle(request);
                })
                .build();
    }
}
