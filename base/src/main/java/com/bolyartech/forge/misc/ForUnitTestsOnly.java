package com.bolyartech.forge.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ogre on 2015-10-12
 */


/**
 * Marks methods that exist solely to be used in unit test and NOT in normal code
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ForUnitTestsOnly {
}
