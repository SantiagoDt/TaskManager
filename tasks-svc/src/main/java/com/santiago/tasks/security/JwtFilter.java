package com.santiago.tasks.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;
import java.util.List;

import io.jsonwebtoken.JwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {


    private Key secretKey;

    public JwtFilter(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = header.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey).build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();

            var auth = new UsernamePasswordAuthenticationToken(
                    userId, null, List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            request.setAttribute("userId", userId);
        }  catch (JwtException e) {
        System.out.println("JWT error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }

        filterChain.doFilter(request, response);
    }
}
