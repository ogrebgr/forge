package com.bolyartech.forge.base.misc;

/**
 * This interface abstracts getting time for time measurements in order to provide
 * standard OS independent way of getting time in milliseconds.
 */
public interface TimeProvider {
    /**
     * Returns wall clock independent time in milliseconds.
     * @return Time in millis
     */
    long getTime();
}
