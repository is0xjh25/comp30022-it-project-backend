package tech.crm.crmserver.security;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;


public class TokenRepositoryImpl extends JdbcTokenRepositoryImpl {

    private String removeUserTokensSql = "delete from persistent_logins where series = ?";

    @Override
    public void removeUserTokens(String series) {
        getJdbcTemplate().update(this.removeUserTokensSql, series);
    }

}
