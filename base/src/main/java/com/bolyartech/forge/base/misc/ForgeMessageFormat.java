package com.bolyartech.forge.base.misc;

import org.slf4j.helpers.MessageFormatter;


/**
 * Utility class for formatting messages
 */
public class ForgeMessageFormat {
    /**
     * Formats a message using SLF4J notation
     *
     * @param msg    Message with placeholders for values in SLF4J format
     * @param params parameters to be used to fill the placeholders
     * @return formatted message
     */
    public static String format(String msg, Object... params) {
        if (params.length == 1) {
            return MessageFormatter.format(msg, params[0]).getMessage();
        } else if (params.length > 1) {
            return MessageFormatter.arrayFormat(msg, params).getMessage();
        } else {
            return MessageFormatter.format(msg, params).getMessage();
        }
    }
}
