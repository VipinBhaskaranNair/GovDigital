package com.dge.chat.filter;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> {
            // 100 requests per minute
            Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));
            return Bucket4j.builder().addLimit(limit).build();
        });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = request.getHeader("X-API-KEY");
        if (key == null || key.isEmpty()) {
            key = request.getRemoteAddr();
        }
        Bucket bucket = resolveBucket(key);
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests\"}");
            logger.warn("Rate limit exceeded for key={}", key);
        }
    }
}
