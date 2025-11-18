package com.tpi.microservicio_cliente.config; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Esto habilita la seguridad por anotaciones (ej. @PreAuthorize)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // [CORRECCIÓN] Permitir acceso público a la documentación Swagger
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Por defecto, TODAS las demás peticiones deben estar autenticadas
                .anyRequest().authenticated() 
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    // Usamos nuestro "conversor" personalizado para leer los roles
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()) 
                )
            );
        return http.build();
    }

    /**
     * Este Bean se usa para configurar cómo Spring Security lee los roles
     * desde el token JWT de Keycloak.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        // Le decimos que use nuestra clase interna para extraer los roles
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }

    /**
     * Clase interna que sabe cómo navegar dentro del token JWT de Keycloak
     * para encontrar la lista de roles.
     * Keycloak pone los roles en: "realm_access" -> "roles"
     */
    class KeycloakRealmRoleConverter implements org.springframework.core.convert.converter.Converter<org.springframework.security.oauth2.jwt.Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(org.springframework.security.oauth2.jwt.Jwt jwt) {
            
            // Revisa si el claim "realm_access" existe
            if (jwt.getClaims().get("realm_access") == null) {
                return List.of(); // Devuelve lista vacía si no hay roles
            }

            // 1. Navega hasta "realm_access"
            final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            
            // 2. Extrae la lista de "roles"
            return ((List<String>) realmAccess.get("roles")).stream()
                    // 3. Añade el prefijo "ROLE_" (estándar de Spring Security)
                    .map(roleName -> "ROLE_" + roleName.toUpperCase()) 
                    // 4. Convierte el String a un objeto GrantedAuthority
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}