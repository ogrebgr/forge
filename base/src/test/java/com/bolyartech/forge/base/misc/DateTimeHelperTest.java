package com.bolyartech.forge.base.misc;

import hirondelle.date4j.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Created by ogre on 2015-10-11
 */
public class DateTimeHelperTest {
    @Test
    public void test_plusMinutes() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2000, 1, 1, 0, 10, 0, 0);
        DateTime rez = DateTimeHelper.plusMinutes(dt, 10);
        assertTrue("plusMinutes() not working correctly", rez.equals(dt2));
    }

}
