package tech.crm.crmserver.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.crm.crmserver.dao.PersistentLogins;
import tech.crm.crmserver.service.PersistentLoginsService;

import java.time.LocalDateTime;

@Component
public class cleanToken {


    @Autowired
    public PersistentTokenBasedRememberMeServicesImpl rememberMeServices;

    @Autowired
    public PersistentLoginsService persistentLoginsService;

    //clean invalid token every Wednesday noon
    @Scheduled(cron="0 0 12 ? * WED")
    public void deleteInvalidToken(){
        //get the invalid time from now
        Integer time = rememberMeServices.getValidTime();
        LocalDateTime invalidTime = LocalDateTime.now().minusSeconds(time);

        //delete invalid token
        QueryWrapper<PersistentLogins> wrapper = new QueryWrapper<PersistentLogins>();
        wrapper.lt("last_used",invalidTime);
        persistentLoginsService.getBaseMapper().delete(wrapper);

    }

}
