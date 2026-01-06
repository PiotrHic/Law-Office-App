package org.example.apigateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

@Configuration
public class GatewayRoutesConfig {

    private final JwtGatewayFilter jwtFilter;

    public GatewayRoutesConfig(JwtGatewayFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // ===== LAWYER SERVICE =====
                .route("Lawyer-Service", r -> r
                        .path("/api/lawyers/**")
                        .and()
                        .method(HttpMethod.GET,HttpMethod.POST,HttpMethod.PUT, HttpMethod.DELETE)
                        .uri("http://localhost:8011")
                )
                /*
                .route("Lawyer-Service-Protected", r -> r
                        .path("/api/lawyers/**")
                        .and()
                        .method(HttpMethod.POST,HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .filter(jwtFilter)
                        )
                        .uri("http://localhost:8011")
                )
                */
                // ===== LAWCASE SERVICE =====
                .route("LawCase-Service", r -> r
                        .path("/api/cases/**")
                        .and()
                        .method(HttpMethod.GET,HttpMethod.POST,HttpMethod.PUT, HttpMethod.DELETE)
                        .uri("http://localhost:8012")
                )
                /*
                .route("LawCase-Service-Protected", r -> r
                        .path("/api/cases/**")
                        .and()
                        .method(HttpMethod.POST,HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .filter(jwtFilter)
                        )
                        .uri("http://localhost:8012")
                )
                */
                // ===== LAWCLIENT SERVICE =====
                .route("LawClient-Service", r -> r
                        .path("/api/clients/**")
                        .and()
                        .method(HttpMethod.GET,HttpMethod.POST,HttpMethod.PUT, HttpMethod.DELETE)
                        .uri("http://localhost:8013")
                )
                /*
                .route("LawClient-Service-Protected", r -> r
                        .path("/api/clients/**")
                        .and()
                        .method(HttpMethod.POST,HttpMethod.PUT, HttpMethod.DELETE)
                        .filters(f -> f
                                .filter(jwtFilter)
                        )
                        .uri("http://localhost:8013")
                )
                */
                .build();
    }

    /*
    @Bean
    public ErrorWebExceptionHandler globalExceptionHandler( ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                            ServerCodecConfigurer serverCodecConfigurer) {
        DefaultErrorWebExceptionHandler handler =
                new DefaultErrorWebExceptionHandler(
                        new DefaultErrorAttributes (),
                        new ResourceProperties(),
                        new ErrorProperties (),
                        ApplicationContextProvider.getApplicationContext()
                );
        handler.setMessageWriters(serverCodecConfigurer.getWriters());
        handler.setViewResolvers(viewResolversProvider.getIfAvailable( Collections::emptyList));
        return handler;
    }

     */
}
