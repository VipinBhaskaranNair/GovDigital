package com.dge.chat.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);
    private final String headerName = "X-API-KEY";
    private final String expectedKey;

    public ApiKeyAuthFilter(Environment env) {
        this.expectedKey = env.getProperty("security.api-key");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(headerName);
        log.info("API key: {}", apiKey);
        // Allow actuator and swagger without API key
        String path = request.getRequestURI();
        if (path.startsWith("/actuator") || path.startsWith("/v3") || path.startsWith("/swagger")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (expectedKey == null || expectedKey.isEmpty()) {
            logger.warn("X-API-KEY not set. API key validation disabled (dev only).");
            filterChain.doFilter(request, response);
            return;
        }
        if (apiKey == null || !apiKey.equals(expectedKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing or invalid API key\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
