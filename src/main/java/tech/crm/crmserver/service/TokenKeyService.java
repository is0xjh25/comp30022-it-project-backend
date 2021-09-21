package tech.crm.crmserver.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import tech.crm.crmserver.dao.TokenKey;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.User;
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

    /**
     * Create Token for user to auto login
     * will save the key in the database
     *
     * @param user user who own this token
     * @return the token
     */
    public String createToken(User user);

    /**
     * Remove the Token key in the database
     *
     * @param token the token need to be removed
     */
    public void removeToken(String token);

    /**
     * Get Authentication from the Token
     *
     * @param token the token user owned
     * @return the Authentication
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String token);

    /**
     * Delete the invalid token key in the database
     */
    public void deleteInvalidToken();

}
