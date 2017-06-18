package com.accenture.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Customize the security context for the cart application
 */
@Configuration
public class CartSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthFilter jwtAuthFilter;

    /**
     * Default constructor to allow injecting the filter class as dependency.
     * @param jwtAuthFilter authentication filter
     */
    @Autowired
    public CartSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Handle the access for the different cart routes
     * @param http web based security for specific http requests
     * @throws Exception exception threw if some rule is infringed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/health").permitAll()
                .antMatchers(HttpMethod.GET, "/cart").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/cart").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/**/*").hasAuthority("ROLE_USER")
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}