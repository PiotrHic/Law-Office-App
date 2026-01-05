package org.example.lawyerservice.config;

import org.example.lawyerservice.client.webclient.LawCaseWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient lawCaseWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8012")
                .build();
    }

    @Bean
    public LawCaseWebClient lawCaseClient() {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory
                        .builderFor(WebClientAdapter.create(lawCaseWebClient()))
                        .build();
        return httpServiceProxyFactory.createClient(LawCaseWebClient.class);
    }
}
