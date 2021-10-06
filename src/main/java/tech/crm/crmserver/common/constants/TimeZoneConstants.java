package tech.crm.crmserver.common.constants;

import java.time.ZoneId;

/**
 * <p>
 *  Time Zone Constants
 * </p>
 * @author Lingxiao Li
 * @since 2021-09-29
 */
public final class TimeZoneConstants {

    public static final String TIME_ZONE_VALUE = "GMT";
    public static final ZoneId ZONE = ZoneId.of(TIME_ZONE_VALUE);
}
