package br.sincroled.gateway;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DynamicJwtDecoderWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String realm = exchange.getRequest().getHeaders().getFirst("X-Tenant-ID");
        var host = exchange.getRequest().getPath().pathWithinApplication().value();
        var request = exchange.getRequest();

        if(true || host.equals("/cliente") || host.contains("/processo") || host.contains("/validate"))
            return chain.filter(exchange);

        if(false && realm == null){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        if (authHeader != null && authHeader != null && authHeader.startsWith("Bearer ")) {
            addCors(exchange);
            String token = authHeader.substring(7);

            String jwkSetUri = "http://localhost:8080/realms/" + realm + "/protocol/openid-connect/certs";
            String issuer = "http://localhost:8080/realms/" + realm;

            NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri)
                    .build();

            OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
            decoder.setJwtValidator(withIssuer);

            JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);

            try {
                Jwt jwt = jwtDecoder.decode(token);
                List<String> tenants = jwt.getClaimAsStringList("tenants");
                boolean authorized = realm != null && tenants != null && tenants.contains(realm);

                if(!authorized){
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                AbstractAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
                SecurityContext context = new SecurityContextImpl(authentication);
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
            } catch (JwtException ex) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange);
    }

    private void addCors(ServerWebExchange exchange){
        HttpHeaders headers = exchange.getResponse().getHeaders();
        if(!headers.containsKey("Access-Control-Allow-Origin")) {
            headers.add("Access-Control-Allow-Origin", "*"); // ou origem dinâmica
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "*");
            headers.add("Access-Control-Allow-Credentials", "false");
        }
    }
    private ReactiveAuthorizationManager<AuthorizationContext> clientIdMatchesTenantClaim() {
        return (authenticationMono, context) -> authenticationMono.map(auth -> {
            if (auth.getPrincipal() instanceof Jwt jwt) {
                List<String> tenants = jwt.getClaimAsStringList("tenants");

                List<String> roles = jwt.getClaimAsStringList("roles");
                if(!roles.contains("client") && !roles.contains("admin"))
                    return new AuthorizationDecision(false);

                String clientId = context.getExchange().getRequest().getHeaders().getFirst("X-Tenant-ID");
                String issuerUri = "http://localhost:8080/realms/" + clientId;
                ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
                boolean authorized = clientId != null && tenants != null && tenants.contains(clientId);
                return new AuthorizationDecision(authorized);
            }
            return new AuthorizationDecision(false);
        });
    }
}
