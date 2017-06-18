package com.accenture.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Customize the security context for the orders application
 */
@Configuration
public class OrderSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthFilter jwtAuthFilter;

    /**
     * Default constructor to allow injecting the filter class as dependency.
     * @param jwtAuthFilter authentication filter
     */
    @Autowired
    public OrderSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Handle the access for the different order routes
     * @param http web based security for specific http requests
     * @throws Exception exception threw if some rule is infringed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] unsecuredUrls = {"/health"};

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, unsecuredUrls).permitAll()
                .antMatchers(HttpMethod.GET, "/orders").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/orders").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/orders").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/**/*").hasAuthority("ROLE_USER")
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}