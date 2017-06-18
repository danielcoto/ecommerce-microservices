package com.accenture.auth;

import com.accenture.login.LoginFilter;
import com.accenture.login.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Customize the security context for the accounts application
 */
@Configuration
public class AccountSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthFilter jwtAuthFilter;
    private LoginUserDetailsService loginUserDetailsService;

    /**
     * Default constructor to allow injecting the filter and service classes as dependencies.
     * @param jwtAuthFilter authentication filter
     * @param loginUserDetailsService login
     */
    @Autowired
    public AccountSecurityConfig(JwtAuthFilter jwtAuthFilter, LoginUserDetailsService loginUserDetailsService) {

        this.jwtAuthFilter = jwtAuthFilter;
        this.loginUserDetailsService = loginUserDetailsService;
    }

    /**
     * Handle the access for the different account routes
     * @param http web based security for specific http requests
     * @throws Exception exception threw if some rule is infringed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] unsecuredUrls = {"/health", "/login"};

        http.csrf().ignoringAntMatchers("/login");

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(unsecuredUrls).permitAll()
                .antMatchers(HttpMethod.GET, "/accounts").hasAuthority("ROLE_ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/accounts").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/accounts/id=**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.PUT, "/accounts/id=**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .and()
                .addFilterBefore(new LoginFilter("/login", authenticationManager(), loginUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    }
}