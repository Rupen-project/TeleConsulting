package com.had.teleconsulting.teleconsulting.Config;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .requestMatchers("/doctor/doctorLogin").permitAll()
                .requestMatchers("/doctor/registerDoctor").permitAll()
                .requestMatchers("/doctor/initialData").permitAll()

                .requestMatchers("/api/user/registerUser").permitAll()
                .requestMatchers("/api/user/userLogin").permitAll()
                .requestMatchers("/api/user/initialData").permitAll()


                .requestMatchers("/api/patientDetails/initialData").permitAll()
                .requestMatchers("/api/patientDetails/send").permitAll()
                .requestMatchers("/api/patientDetails/sendMessage").permitAll()
                .requestMatchers("/api/patientDetails/topic/message").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}