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
                        http.requestMatchers(HttpMethod.POST, "/companies/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.GET, "/locations/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.POST, "/locations/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.PUT, "/locations/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.DELETE, "/locations/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.GET, "/sensors/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.POST, "/sensors/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.DELETE, "/sensors/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.PUT, "/sensors/**").hasAuthority("ADMIN");
                        http.requestMatchers(HttpMethod.POST, "/sensorData/**").hasAuthority("ADMIN");
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
