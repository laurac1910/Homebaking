package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.swing.plaf.PanelUI;

@Configuration
public class WebConfiguration {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // creamos un filtro de seguridad
        http
                .csrf(AbstractHttpConfigurer::disable)  //deshabilitamos todos los siguientes metodos
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest // autorizamos las peticiones
                                .requestMatchers("/api/auth/login", "/api/auth/signup").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // agregamos el filtro antes de la autenticacion
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));// deshabilitamos la creacion de sesiones

        return http.build();// construimos el filtro
    }


    @Bean
    public PasswordEncoder passwordEncoder() { // creamos un encriptador de contraseñas
        return new BCryptPasswordEncoder(); // usamos el encriptador de contraseñas BCrypt
    }

   @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception { // creamos un administrador de autenticacion
        return authenticationConfiguration.getAuthenticationManager();
       /* al manejador de la app tome en cuenta todo lo que acabamos de configurar y lo ejecuta  cuando arranca la app */
    }

}

