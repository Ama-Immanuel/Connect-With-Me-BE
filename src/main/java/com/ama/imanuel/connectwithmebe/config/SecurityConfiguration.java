package com.ama.imanuel.connectwithmebe.config;

import com.ama.imanuel.connectwithmebe.user.Permission;
import com.ama.imanuel.connectwithmebe.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/api/v1/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "swagger-ui.html",
                                        "/api/v1/upload/public/**"
                                )
                                .permitAll()

                                .requestMatchers("/api/v1/admin/**")
                                .hasAnyRole(Role.ADMIN.name())

                                .requestMatchers("/api/v1/user/**")
                                .hasAnyRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/user/**").hasAnyAuthority(Permission.ADMIN_READ.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/user/**").hasAnyAuthority(Permission.ADMIN_CREATE.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/user/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAnyAuthority(Permission.ADMIN_DELETE.name())

                                .anyRequest()
                                .authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout ->
                        logout
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                );


        return httpSecurity.build();
    }
}
