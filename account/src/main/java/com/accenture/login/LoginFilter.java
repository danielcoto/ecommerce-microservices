package com.accenture.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters and authenticates the users during login process
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final LoginUserDetailsService loginUserDetailsService;

    /**
     * Class constructor
     * @param url request route
     * @param authenticationManager instance of the running AuthenticationManager
     * @param loginUserDetailsService service for getting the account from the repository
     */
    public LoginFilter(String url, AuthenticationManager authenticationManager, LoginUserDetailsService loginUserDetailsService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
        this.loginUserDetailsService = loginUserDetailsService;
    }

    /**
     * Retrives the username and password from the login request and uses the AuthenticationManager to
     * verify that these details match with an existing account.
     * @param httpServletRequest object to provide request information for HTTP servlets.
     * @param httpServletResponse object to provide HTTP-specific functionality in sending a response.
     * @return an Authentication implementation designed for simple presentation of a username and password.
     * @throws AuthenticationException exception related to an Authentication object being invalid for whatever reason.
     * @throws IOException general exception threw by when a IO operation is interrupted.
     * @throws ServletException general exception threw by the servlet when it encounters difficulty.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {

        LoginCredentials creds = new ObjectMapper().readValue(httpServletRequest.getInputStream(), LoginCredentials.class);

        if (creds.getUsername() == null || creds.getPassword() == null)
            throw new ServletException("Please provide username and password.");

        UserDetails account = loginUserDetailsService.loadUserByUsername(creds.getUsername());

        if (account == null)
            throw new ServletException("Account username was not found.");

        if (account.getAuthorities() == null) throw new InsufficientAuthenticationException("User has no roles assigned.");

        Integer id = loginUserDetailsService.getAccountId(creds.getUsername());

        if (creds.getUsername().equals(account.getUsername()) && (creds.getPassword().equals(account.getPassword()))) {
            try {
                List<GrantedAuthority> authorities = account.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toList());
                return new UsernamePasswordAuthenticationToken(account.getUsername(), id, authorities);
            }
            catch (AuthenticationException e) {
                throw e;
            }
        }
        throw new ServletException("Invalid login: please check username and password provided.");
    }

    /**
     * Fetches the name and the account id from the authenticated account and adds a JWT to the response.
     * @param httpServletRequest object to provide request information for HTTP servlets.
     * @param httpServletResponse object to provide HTTP-specific functionality in sending a response.
     * @param chain object which gives a view into the invocation chain of a filtered request for a resource.
     * @param authentication is the token for an authentication request.
     * @throws IOException general exception threw by when a IO operation is interrupted.
     * @throws ServletException general exception threw by the servlet when it encounters difficulty.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                            FilterChain chain, Authentication authentication)
            throws IOException, ServletException {

        String JWT = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", authentication.getCredentials())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();

        httpServletResponse.addHeader("Authorization", "Bearer " + JWT);
    }
}