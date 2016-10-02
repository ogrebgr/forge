package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.ResponseCode;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
public class BasicResponseCodes {
    public enum Oks implements ResponseCode {
        @SuppressWarnings("unused")OK(1); // used as general code that indicates success

        private final int code;


        @SuppressWarnings("unused")
        Oks(int code) {
            if (code > 0) {
                this.code = code;
            } else {
                throw new IllegalArgumentException("Code must be positive");
            }
        }


        public int getCode() {
            return code;
        }
    }


    public enum Errors implements ResponseCode {
        ERROR(-1), // used as general error when we cant/don't want to specify more details
        MISSING_PARAMETERS(-2), // missing required parameters
        REQUIRES_HTTPS(-3), // HTTPS protocol must be used
        INVALID_PARAMETER_VALUE(-4), // parameter value is invalid. For example: string is passed where int is expected
        INTERNAL_SERVER_ERROR(-5), // some serious problem occurred on the server
        UPGRADE_NEEDED(-6); // client version is too old and unsupported


        private static final Map<Integer, Errors> mTypesByValue = new HashMap<>();

        static {
            for (Errors type : Errors.values()) {
                mTypesByValue.put(type.getCode(), type);
            }
        }


        private final int mCode;


        @SuppressWarnings("unused")
        Errors(int code) {
            if (code < 0) {
                this.mCode = code;
            } else {
                throw new IllegalArgumentException("Code must be negative");
            }
        }


        public int getCode() {
            return mCode;
        }

        @SuppressWarnings("unused")
        public static Errors fromInt(int code) {
            Errors ret = mTypesByValue.get(code);
            if (ret != null) {
                return ret;
            } else {
                return null;
            }
        }
    }
}
