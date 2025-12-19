package org.example.lawclientservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.lawclientservice.document.IdempotencyKeyDocument;
import org.example.lawclientservice.repository.IdempotencyKeyRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyKeyRepository repository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String idempotencyKey = request.getHeader("Idempotency-Key");

        if (idempotencyKey == null || !"POST".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var existing = repository.findById(idempotencyKey);
        if (existing.isPresent()) {
            var record = existing.get();
            response.setStatus(record.getResponseStatus());
            response.getWriter().write(record.getResponseBody());
            return;
        }

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        String responseBody =
                new String(responseWrapper.getContentAsByteArray());

        repository.save(new IdempotencyKeyDocument (
                idempotencyKey,
                responseBody,
                responseWrapper.getStatus(),
                Instant.now()
        ));

        responseWrapper.copyBodyToResponse();
    }
}