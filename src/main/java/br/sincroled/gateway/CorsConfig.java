package br.sincroled.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Collections;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/comando/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public CorsWebFilter  corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedHeader("*"); // Permitir todos os cabeçalhos
        corsConfig.addAllowedMethod("*"); // Permitir todos os métodos
        corsConfig.setAllowCredentials(false); // Não permitir credenciais (cookies, headers de autorização, etc.)

        // Defina uma única origem permitida (exemplo: http://sincroled.com.br)
        corsConfig.setAllowedOrigins(Collections.singletonList("http://sincroled.com.br")); // Permitir apenas uma origem específica

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Aplicar a configuração para todos os endpoints

        return new CorsWebFilter(source);
    }
}
