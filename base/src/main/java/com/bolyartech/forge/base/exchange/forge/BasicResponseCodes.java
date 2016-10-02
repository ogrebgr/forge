package com.bolyartech.forge.base.exchange.forge;


/**
 * Utility class for basic response codes
 *
 * For non-basic, i.e. your response codes please use values greater than 100 for OKs and smaller than -100 for errors
 */
@SuppressWarnings("unused")
public class BasicResponseCodes {
    /**
     * Disabling instantiation
     */
    private BasicResponseCodes() {
        throw new AssertionError("Non-instantiable utility class");
    }


    /**
     * Response code for operations that completed successfully
     */
    @SuppressWarnings("unused")
    public static final int OK = 1;


    /**
     * Contains error codes
     */
    public static class Errors {
        private Errors() {
            throw new AssertionError("Non-instantiable utility class");
        }


        /**
         * used as general error when we cant/don't want to specify more details
         */
        @SuppressWarnings("unused")
        public static final int UNSPECIFIED_ERROR = -1;
        /**
         * missing required parameters
         */
        @SuppressWarnings("unused")
        public static final int MISSING_PARAMETERS = -2;
        /**
         * HTTPS protocol must be used
         */
        @SuppressWarnings("unused")
        public static final int REQUIRES_HTTPS = -3;
        /**
         * parameter value is invalid. For example: string is passed where int is expected
         */
        @SuppressWarnings("unused")
        public static final int INVALID_PARAMETER_VALUE = -4;
        /**
         * some serious problem occurred on the server
         */
        @SuppressWarnings("unused")
        public static final int INTERNAL_SERVER_ERROR = -5;
        /**
         * client version is too old and unsupported
         */
        @SuppressWarnings("unused")
        public static final int UPGRADE_NEEDED = -6;
    }
}
