package com.santiago.tasks.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final Key secretKey;
    private final AuthenticationEntryPoint entryPoint;

    public JwtFilter(@Value("${jwt.secret}") String secret,
                     AuthenticationEntryPoint entryPoint) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.entryPoint = entryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            request.setAttribute("jwt_error", "Missing Bearer token");
            entryPoint.commence(request, response,
                    new InsufficientAuthenticationException("Missing Bearer token"));
        }

        String token = header.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();

            var auth = new UsernamePasswordAuthenticationToken(
                    userId, null, List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute("jwt_error", "JWT invalid or expired");
            entryPoint.commence(request, response,
                    new InsufficientAuthenticationException("JWT invalid or expired", e));
        }
    }
}