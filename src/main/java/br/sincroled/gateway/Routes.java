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
                .route("/api/totem", r ->
                        r.path("totem/**").uri("http://totem:8081/")
                ).build();
    }

    @Bean
    public RouteLocator routesComando(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("/api/comando", r ->
                        r.path("comando/**").uri("http://comando:8082/")
                ).build();
    }
}
