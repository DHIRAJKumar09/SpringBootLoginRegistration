package com.tcs.registration_login.userauthentication.security;


import ch.qos.logback.core.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticaitonFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticaitonFilter(JwtTokenProvider jwtTokenProvider){
        this.tokenProvider = jwtTokenProvider;
    }
    public String resolveToken(HttpServletRequest request ){
        String bearer = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }

    protected  void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException, IOException{

        String token = resolveToken(request);
        if(token != null){
            try{
                Jws<Claims>  claimsJws  =tokenProvider.validateTokenAndGetCalims(token);
                Claims claims = claimsJws.getBody();
                String username = tokenProvider.getUsernameFromClaims(claims);
                List<String> roles = tokenProvider.getRolesFromClaims(claims);

                var authroties =  roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                var auth  = new UsernamePasswordAuthenticationToken(username,null,authroties);
                SecurityContextHolder.getContext().setAuthentication(auth);

            }catch(Exception e){
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid or expired JWT\"}");
                return;

            }
        }
        filterChain.doFilter(request,response);

    }
}
