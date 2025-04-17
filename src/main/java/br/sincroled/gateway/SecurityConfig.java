package br.sincroled.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/testea/**").hasRole("user")
                        .pathMatchers("/testeb/**").hasRole("admin")
                        .anyExchange().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
            converter.setAuthoritiesClaimName("roles");
            converter.setAuthorityPrefix("ROLE_");

            Map<String, Object> resourceAccess = jwt.getClaimAsMap("realm_access");
            List<String> roles = (List<String>) resourceAccess.get("roles");



            Collection<GrantedAuthority> authorities =  roles.stream().map(role -> new GrantRole(role)).collect(Collectors.toList());

            // Log para debug
            System.out.println("=== Roles extraídas do token ===");
            authorities.forEach(auth -> System.out.println(auth.getAuthority()));

            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }

    private class GrantRole implements GrantedAuthority{

        private String role;

        public GrantRole(String role) {
            this.role = role;
        }

        @Override
        public String getAuthority() {
            return "ROLE_" + role;
        }
    }
}