package com.weconnect.security;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.replace("Bearer ", "");
            try {
                Claims claims = JwtUtil.validateToken(token);
                req.setAttribute("userId", claims.get("userId", String.class));
                req.setAttribute("role", claims.get("role", String.class));
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Authentication required");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
