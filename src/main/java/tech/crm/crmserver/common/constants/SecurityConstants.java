package tech.crm.crmserver.common.constants;

/**
 * @author
 * @description Spring Security constants
 */
public final class SecurityConstants {


    public static final String ROLE_CLAIMS = "rol";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final long EXPIRATION = 60 * 60 * 24 * 7L;

    private SecurityConstants() {
    }

}
