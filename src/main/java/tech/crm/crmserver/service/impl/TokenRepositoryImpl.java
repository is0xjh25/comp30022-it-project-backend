package tech.crm.crmserver.service.impl;

import org.springframework.core.log.LogMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class TokenRepositoryImpl extends JdbcDaoSupport implements PersistentTokenRepository {
    private String tokensBySeriesSql = "select email,series,token,last_used from user where series = ?";
    private String insertTokenSql = "update user set series = ?, token = ?, last_used = ? where email = ?";
    private String updateTokenSql = "update user set token = ?, last_used = ? where series = ?";
    private String removeUserTokensSql = "update user set series = null , token = null , last_used = null where email = ?";

    public TokenRepositoryImpl() {
    }


    public void createNewToken(PersistentRememberMeToken token) {
        this.getJdbcTemplate().update(this.insertTokenSql, new Object[]{token.getSeries(), token.getTokenValue(), token.getDate(),token.getUsername()});
    }

    public void updateToken(String series, String tokenValue, Date lastUsed) {
        this.getJdbcTemplate().update(this.updateTokenSql, new Object[]{tokenValue, lastUsed, series});
    }

    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            return (PersistentRememberMeToken)this.getJdbcTemplate().queryForObject(this.tokensBySeriesSql, this::createRememberMeToken, new Object[]{seriesId});
        } catch (EmptyResultDataAccessException var3) {
            this.logger.debug(LogMessage.format("Querying token for series '%s' returned no results.", seriesId), var3);
        } catch (IncorrectResultSizeDataAccessException var4) {
            this.logger.error(LogMessage.format("Querying token for series '%s' returned more than one value. Series should be unique", seriesId));
        } catch (DataAccessException var5) {
            this.logger.error("Failed to load token for series " + seriesId, var5);
        }

        return null;
    }

    private PersistentRememberMeToken createRememberMeToken(ResultSet rs, int rowNum) throws SQLException {
        return new PersistentRememberMeToken(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
    }

    public void removeUserTokens(String username) {
        this.getJdbcTemplate().update(this.removeUserTokensSql, new Object[]{username});
    }


}
