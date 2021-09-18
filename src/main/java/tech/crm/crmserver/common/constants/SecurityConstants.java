package tech.crm.crmserver.common.constants;

/**
 * Spring Security constants
 * @author Lingxiao Li
 */
public final class SecurityConstants {

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final long EXPIRATION = 60 * 60 * 24 * 7L;

    private SecurityConstants() {
    }

}
