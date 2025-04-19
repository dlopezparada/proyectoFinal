package com.futuro.proyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                	http.requestMatchers(HttpMethod.POST, "/api/v1/sensor_data").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/companies/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.PUT, "/companies/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.GET, "/companies/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.GET, "/locations/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.POST, "/locations/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.PUT, "/locations/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.DELETE, "/locations/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.GET, "/sensors/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.POST, "/sensors/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.DELETE, "/sensors/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.PUT, "/sensors/**").hasRole("ADMINISTRADOR");
                    http.requestMatchers(HttpMethod.GET, "/api/**").hasRole("ADMINISTRADOR");
                    http.anyRequest().denyAll();
                })
                .build();
    }

	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
