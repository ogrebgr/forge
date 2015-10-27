/*
 * Copyright (C) 2012-2015 Ognyan Bankov
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

package com.bolyartech.forge.misc;

import hirondelle.date4j.DateTime;


/**
 * Provides convenience helper methods for manipulating DataTime objects
 */
public class DateTimeHelper {
    /**
     * Noninstantiable utility class
     */
    private DateTimeHelper() {
        throw new AssertionError();
    }


    public static DateTime plusMinutes(DateTime src, int minutesToAdd) {
        return src.plus(0,
                0,
                0,
                0,
                minutesToAdd,
                0,
                0,
                DateTime.DayOverflow.Spillover);
    }
}
