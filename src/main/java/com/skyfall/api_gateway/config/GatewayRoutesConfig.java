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

    private static final String BANK_URI = "http://bank-system:8080";
    private static final String HOTEL_URI = "http://hotel-system:8080";

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctions() {
        return route("bank_service")
                .route(path(
                        "/api/auth/**",
                        "/api/admin/users/**",
                        "/api/cards/**",
                        "/api/transfer/**"
                ), http())
                .before(uri(BANK_URI))
                .filter((request, next) -> {
                    log.info("[GATEWAY -> BANK] {} {}", request.method(), request.uri().getPath());
                    return next.handle(request);
                })
                .build().and(route("hotel_service")
                        .route(path(
                                "/api/hotels/**",
                                "/api/rooms/**",
                                "/api/bookings/**",
                                "/api/statistics/**",
                                "/api/users/**"
                        ), http())
                        .before(uri(HOTEL_URI))
                        .filter((request, next) -> {
                            log.info("[GATEWAY -> HOTEL] {} {}", request.method(), request.uri().getPath());
                            return next.handle(request);
                        })
                        .build());
    }
}
