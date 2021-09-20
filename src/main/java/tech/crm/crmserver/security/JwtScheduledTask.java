package tech.crm.crmserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.crm.crmserver.service.TokenKeyService;


@Component
public class JwtScheduledTask {


    @Autowired
    TokenKeyService tokenKeyService;

    /**
     * delete invalid token every week
     */
    @Scheduled(cron = "0 0 12 ? * WED ")
    public void deleteInvalidTokenScheduledTask() {
        tokenKeyService.deleteInvalidToken();
    }
}
