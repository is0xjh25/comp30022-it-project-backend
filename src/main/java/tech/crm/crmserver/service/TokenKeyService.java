package tech.crm.crmserver.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import tech.crm.crmserver.dao.TokenKey;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.LoginRequest;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-26
 */
public interface TokenKeyService extends IService<TokenKey> {

    public String createToken(LoginRequest loginRequest);

    public void removeToken(String token);

    public UsernamePasswordAuthenticationToken getAuthentication(String token);

}
