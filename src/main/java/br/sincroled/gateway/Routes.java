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
    public RouteLocator testeApiA(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("modulo", r ->
                        r.path("/modulo/**").uri("http://localhost:9080/")
                ).build();
    }

    @Bean
    public RouteLocator agendaApi(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("agenda", r ->
                        r.path("/agenda/**").uri("http://localhost:9081/")
                ).build();
    }

    @Bean
    public RouteLocator usuarioApi(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("usuario", r ->
                        r.path("/usuario/**").uri("http://localhost:9082/")
                ).build();
    }
}
