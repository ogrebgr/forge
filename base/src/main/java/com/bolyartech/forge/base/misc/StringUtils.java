/*
 * Copyright (C) 2012-2016 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bolyartech.forge.base.misc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Utility class that groups methods that facilitate working with strings
 */
public class StringUtils {
    /**
     * Non-instantiable utility class
     */
    private StringUtils() {
        throw new AssertionError();
    }


    /**
     * Calculates md5 sum for a string. This method is useful because it catches the potential NoSuchAlgorithmException
     * and rethrows it as RuntimeException. That way you don't have to use your own try-catch block
     *
     * @param str String for which md5 will be calculated
     * @return md5 hash
     */
    public static String md5(String str) {

        MessageDigest m;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        m.update(str.getBytes());

        return new BigInteger(1, m.digest()).toString(16);
    }


    /**
     * Calculates md5 hash for a long
     * <p/>
     * Internally long is converted to md5 and then md5 is calculated
     *
     * @param lng Value for which md5 will be calculated
     * @return md5 hash
     */
    public static String md5(long lng) {
        return md5(Long.toString(lng));
    }


    /**
     * Calculates md5 hash for an int
     * <p/>
     * Internally int is converted to md5 and then md5 is calculated
     *
     * @param i Value for which md5 will be calculated
     * @return md5 hash
     */
    public static String md5(int i) {
        return md5(Integer.toString(i));
    }


    /**
     * Checks if string is empty.
     * <p/>
     * String is considered empty if one of the following is true: it is null or it contains just white spaces
     *
     * @param str String to be checked
     * @return true if string is empty
     * @see #isNotEmpty(String)
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        String trimmedString = str.trim();

        return "".equals(trimmedString);
    }


    /**
     * Checks if string is not empty
     *
     * @param str String to be checked
     * @return true if the string is not empty
     * @see #isEmpty(String)
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
