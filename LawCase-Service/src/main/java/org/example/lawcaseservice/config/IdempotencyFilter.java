package org.example.lawcaseservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.lawcaseservice.document.IdempotencyKeyDocument;
import org.example.lawcaseservice.repository.IdempotencyKeyRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final String IDEMPOTENCY_HEADER = "Idempotency-Key";

    private final IdempotencyKeyRepository repository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(
                HttpMethod.POST.matches(request.getMethod()) ||
                        HttpMethod.PUT.matches(request.getMethod()) ||
                        HttpMethod.PATCH.matches(request.getMethod())
        );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String key = request.getHeader(IDEMPOTENCY_HEADER);

        if (key == null || key.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<IdempotencyKeyDocument> cached = repository.findById(key);

        if (cached.isPresent()) {
            response.setStatus(cached.get().getResponseStatus());
            response.setContentType("application/json");
            response.getWriter().write(cached.get().getResponseBody());
            return;
        }

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        try {
            repository.save(
                    IdempotencyKeyDocument.of(
                            key,
                            new String(responseWrapper.getContentAsByteArray()),
                            responseWrapper.getStatus()
                    )
            );
        } catch (Exception ignored) {
            // duplicate key = race condition → OK
        }

        responseWrapper.copyBodyToResponse();
    }
}