package org.example.lawyerservice.client.webclient;

import org.example.lawyerservice.client.dto.LawCaseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.UUID;

@Component
@HttpExchange
public interface LawCaseWebClient {

    static String ID_PATH = "/{lawyerId}";

    @GetExchange("/api/cases/webclient/sendLawCasesToLawyerService" + ID_PATH)
    public List<LawCaseDto> getLawCasesByLawyerId( @PathVariable UUID lawyerId);

}