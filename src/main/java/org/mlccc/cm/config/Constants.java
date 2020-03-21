package org.mlccc.cm.config;

import java.util.Arrays;
import java.util.HashSet;

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
    public static final String FINISHED_STATUS = "FINISHED";
    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final String DISCOUNT_CODE_MULTICLASS = "MULTICLASS";
    public static final String DISCOUNT_CODE_EARLYBIRD = "EARLYBIRD";
    public static final String DISCOUNT_CODE_REGWAIVER = "REGWAIVER";
    public static final String INVOICE_PAID_STATUS = "PAID";
    public static final String INVOICE_UNPAID_STATUS = "UNPAID";
    public static final String PAYMENT_TYPES = "Cash,Check,Credit Card,Paypal";
    public static final String PAYMENT_TYPE_CASH = "Cash";
    public static final String PAYMENT_TYPE_CHECK = "Check";
    public static final String PAYMENT_TYPE_CC = "Credit Card";
    public static final String PAYMENT_TYPE_PAYPAL = "Paypal";

    private Constants() {
    }
}
