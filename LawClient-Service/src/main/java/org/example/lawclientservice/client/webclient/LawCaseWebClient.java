package org.example.lawclientservice.client.webclient;

import org.example.lawclientservice.client.dto.LawCaseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@Component
@HttpExchange
public interface LawCaseWebClient {

    static String ID_PATH = "/{lawClientId}";

    @GetExchange("/api/cases/webclient/sendLawCasesToLawCLientService" + ID_PATH)
    public List<LawCaseDto> getLawCasesByLawClientId(@PathVariable String lawClientId);
}
