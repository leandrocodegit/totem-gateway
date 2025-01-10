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
                .route("api", r ->
                        r.path("api/**")
                                .filters(f -> f.filter(filter)
                                        .rewritePath("api", "/totem"))
                                .uri("http://totem:8081/")
                ).build();
    }

    @Bean
    public RouteLocator routesComando(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("comando", r ->
                        r.path("comando/**")
                                .filters(f -> f.filter(filter))
                                .uri("http://comando:8082/")
                ).build();
    }
}
