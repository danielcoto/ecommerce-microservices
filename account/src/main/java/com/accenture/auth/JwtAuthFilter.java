package com.accenture.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters and authenticates the tokens received in the header of the requests
 */
@Component
public class JwtAuthFilter implements Filter {

    /**
     *  Indicates to the filter that it is being placed into service
     * @param filterConfig filter configuration object to pass information during the filter initialization.
     * @throws ServletException general exception threw by the servlet when it encounters difficulty
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Method called by the container each time a request/response pair is passed through the chain
     * due to a client request for a resource at the end of the chain.
     * @param request object to provide client request information to a servlet.
     * @param response object to assist a servlet in sending a response to the client.
     * @param chain object which gives a view into the invocation chain of a filtered request for a resource.
     * @throws IOException general exception threw by when a IO operation is interrupted.
     * @throws ServletException general exception threw by the servlet when it encounters difficulty.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String authorization = servletRequest.getHeader("Authorization");
        if (authorization != null) {
            JwtAuthToken token = new JwtAuthToken(authorization);

            String s = token.getCredentials().toString();
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey("secret")
                        .parseClaimsJws(s.replace("Bearer ", ""))
                        .getBody();

                String username = claims.getSubject();
                if (username.equals("admin")) {
                    JwtAuthProfile jwtAuthAdminProfile = new JwtAuthProfile(username, "ROLE_ADMIN");
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthAdminProfile);
                }
                else {
                    JwtAuthProfile jwtAuthUserProfile = new JwtAuthProfile(username, "ROLE_USER");
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthUserProfile);
                }
            }
            catch (AuthenticationException e) {
                throw e;
            }
        }
        chain.doFilter(request,response);
    }

    /**
     * Indicates to the filter that it is being taken out of service.
     */
    @Override
    public void destroy() {

    }
}