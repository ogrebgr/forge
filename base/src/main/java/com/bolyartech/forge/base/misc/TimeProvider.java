package com.bolyartech.forge.base.misc;

/**
 * This interface abstracts getting time for time measurements in order to provide
 * standard OS independent way of getting time in milliseconds. This is not wall clock time but real time,
 * i.e. time measured since VM is started for example
 */
public interface TimeProvider {
    /**
     * Returns wall clock independent time in milliseconds.
     * @return Time in millis
     */
    long getTime();
}
