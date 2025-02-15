package br.sincroled.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {

    private final RouteFilter filter;

    public Routes(RouteFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routesApi(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("totem", r ->
                        r.path("/totem/**").uri("http://totem:8081/")
                ).build();
    }

    @Bean
    public RouteLocator routesComando(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("comando", r ->
                        r.path("/comando/**").uri("http://comando:8082/")
                ).build();
    }

    @Bean
    public RouteLocator routesFirmware(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("firmware", r ->
                        r.path("/firmware/**")
                                .uri("http://comando:8082/")
                ).build();
    }

    @Bean
    public RouteLocator routesIntegracao(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("integracao", r ->
                        r.path("/integracao/**")
                                .uri("http://comando:8082/")
                ).build();
    }
}
