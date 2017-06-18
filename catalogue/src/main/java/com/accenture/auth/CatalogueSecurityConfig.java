package com.accenture.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Customize the security context for the catalogue application
 */
@Configuration
public class CatalogueSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthFilter jwtAuthFilter;

    /**
     * Default constructor to allow injecting the filter class as dependency.
     * @param jwtAuthFilter authentication filter
     */
    @Autowired
    public CatalogueSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Handle the access for the different catalogue routes
     * @param http web based security for specific http requests
     * @throws Exception exception threw if some rule is infringed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] unsecuredUrls = {"/health", "/**/*"};

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, unsecuredUrls).permitAll()
                .antMatchers(HttpMethod.POST, "/catalogue/products").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/**/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/**/**").hasAuthority("ROLE_ADMIN")
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}