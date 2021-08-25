package tech.crm.crmserver.security;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;


public class TokenRepositoryImpl extends JdbcTokenRepositoryImpl {

    private String removeUserTokensSql = "delete from persistent_logins where series = ?";


    @Override
    public void removeUserTokens(String series) {
        getJdbcTemplate().update(this.removeUserTokensSql, series);
    }

    public String getUsernameFromSeries(String seriesId) {

        PersistentRememberMeToken persistentRememberMeToken = getTokenForSeries(seriesId);
        if(persistentRememberMeToken == null){
            return null;
        }
        return persistentRememberMeToken.getUsername();
    }

}
