package com.project.cinemarest.security;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Method called whenever there is a http request
     * @param request http request
     * @param response http response
     * @param filterChain list of filters to be executed
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/cinema/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization"); //header which contains the Bearer token
        final String jwt;
        final String userEmail;
        if (!StringUtils.startsWith(authHeader, "Bearer ")) { //No token present
            filterChain.doFilter(request, response);
            return;
        }
        jwt = StringUtils.substring(authHeader, 7); //Extract token
        userEmail = extractUserEmail(response, jwt); //Extract user mail or throw exception i token expired

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) { //User not authenticated
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); //Set user authentication
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractUserEmail(HttpServletResponse response, String jwt) {
        try {
            return jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException exception) {
            response.setStatus(403); //FORBIDDEN
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            throw exception;
        }
    }
}
