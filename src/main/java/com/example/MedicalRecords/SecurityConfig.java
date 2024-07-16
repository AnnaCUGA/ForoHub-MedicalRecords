package com.example.medicalrecords.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //seguridad web de Spring Security
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    //Constructor para cargar detalles del usuario
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //Configurar el AuthenticationManager para autenticación en Spring Security
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class) //Obtiene el objeto AuthenticationManagerBuilder desde HttpSecurity
                .userDetailsService(userDetailsService) //Configura el servicio de detalles de usuario
                .passwordEncoder(passwordEncoder) //Configura el encoder de contraseñas
                .and()
                .build(); //Devuelve el AuthenticationManager construido
    }

    //Bean para configurar la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()//Deshabilita la protección CSRF
            .authorizeRequests()
            .antMatchers("/login").permitAll()//Permite el acceso a /login sin autenticación
            .anyRequest().authenticated() //Cualquier otra solicitud que requiera hacer el paciente requiere autenticación
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); 

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);//Agrega un filtro JWT antes del filtro estándar de autenticación por usuario y contraseña

        return http.build(); //Devuelve la cadena de filtros de seguridad configurada
    }

    //Bean para configurar BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
