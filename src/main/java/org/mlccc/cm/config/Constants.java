package org.mlccc.cm.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String PENDING_STATUS = "PENDING";
    public static final String CONFIRMED_STATUS = "CONFIRMED";
    public static final String CANCELLED_STATUS = "CANCELLED";
    public static final String CANCELLED_NEED_REFUND_STATUS = "CANCELLED_NEED_REFUND_STATUS";
    public static final String CANCELLED_REFUNDED_STATUS = "CANCELLED_REFUNDED_STATUS";
    public static final String OPEN_STATUS = "OPEN";
    public static final String FULL_STATUS = "FULL";
    public static final String CLOSED_STATUS = "CLOSED";
    public static final String ALMOST_FULL_STATUS = "ALMOST FULL";

    private Constants() {
    }
}
