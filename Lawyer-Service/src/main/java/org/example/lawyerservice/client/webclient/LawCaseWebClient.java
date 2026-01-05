package org.example.lawyerservice.client.webclient;

import org.example.lawyerservice.client.dto.LawCaseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@Component
@HttpExchange
public interface LawCaseWebClient {

    static String ID = "lawyerId";
    static String ID_PATH = "/{lawyerId}";

    @GetExchange("/api/cases/webclient/sendLawCases" + ID_PATH)
    public List<LawCaseDto> getLawCasesByLawyerId( @PathVariable(ID) String lawyerId);

    /*
    @GetExchange("/api/lawcase/webclient/sendLawCases-WithLawClients" + ID_PATH)
    public List<LawCaseDto> getLawCasesWithLawClientsByLawyerId(@PathVariable(ID) String lawyerId);
     */

}