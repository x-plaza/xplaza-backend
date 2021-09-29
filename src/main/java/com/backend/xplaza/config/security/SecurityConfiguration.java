package com.backend.xplaza.config.security;

/*
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/swagger-ui/").permitAll()
                .antMatchers("/api/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/adminuser/add").permitAll()
                .antMatchers("/api/adminuser/confirm-account").permitAll();
    }
}
*/

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter
//{
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
//
//        // You can customize the following part based on your project, it's only a sample
//        http.authorizeRequests().antMatchers("/**").permitAll().anyRequest()
//                .authenticated().and().csrf().disable().cors().configurationSource(request -> corsConfiguration);
//
//    }
//}
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //other http security config
        http.cors().configurationSource(corsConfigurationSource());
    }

    //This can be customized as required
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowOrigins = Arrays.asList("*");
        configuration.setAllowedOrigins(allowOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        //in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
        //configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}