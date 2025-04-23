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
    public RouteLocator testeApiB(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("refresh", r ->
                        r.path("/refresh/**").uri("http://localhost:8080/realms/master/protocol/openid-connect/token")
                ).build();
    }

    @Bean
    public RouteLocator scopeApiB(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("scope", r ->
                        r.path("/scope/**").uri("http://localhost:8083/")
                ).build();
    }

    @Bean
    public RouteLocator loginGoogle(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("google", r ->
                        r.path("/google/**").uri("http://localhost:8080/realms/master/.well-known/openid-configuration")
                ).build();
    }

    @Bean
    public RouteLocator loginGoogled(RouteLocatorBuilder builder) {
        return builder.routes()
                               .route("resources", r ->
                        r.path("/resources/**").uri("http://localhost:8080/resources/")
                ).build();
    }

    @Bean
    public RouteLocator routesApi(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("totem", r ->
                        r.path("/totem/**").uri("http://localhost:8081/")
                ).build();
    }

    @Bean
    public RouteLocator routesComando(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("comando", r ->
                        r.path("/comando/**").uri("http://localhost:8082/")
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
