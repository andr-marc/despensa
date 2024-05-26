package com.andremarc.pantry.config;

import com.andremarc.pantry.security.jwt.AuthEntryPointJwt;
import com.andremarc.pantry.security.jwt.AuthTokenFilter;
import com.andremarc.pantry.security.services.UserDetailsServiceImpl;
import com.andremarc.pantry.util.Permissions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.net.http.HttpResponse;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    private final String[] PUBLIC_PATHS = {
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers()
                .frameOptions()
                .disable()
                .addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "ALLOW"));

        try {
            http.csrf()
                    .disable()
                    .cors()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and().authorizeHttpRequests((authz) -> authz
                                    .requestMatchers(PUBLIC_PATHS).permitAll()
                                    .requestMatchers(HttpMethod.GET, "/user").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/user").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                                    .requestMatchers(HttpMethod.PUT, "/user").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.PUT, "/user/passwordUpdate").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.GET, "/unity/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.POST, "/unity/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.PUT, "/unity/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.DELETE, "/unity/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.GET, "/product/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.POST, "/product/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.PUT, "/product/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.DELETE, "/product/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.GET, "/category/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.POST, "/category/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.PUT, "/category/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .requestMatchers(HttpMethod.DELETE, "/category/**").hasAnyAuthority(Permissions.ADMINISTRADOR)
                                    .anyRequest().authenticated()
                    )
                    .httpBasic(withDefaults());

            http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
            return http.build();
        } catch (Exception e) {
            ResponseEntity<String> res = ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Acesso não autorizado");
            return (SecurityFilterChain) res;
        }

    }

}
