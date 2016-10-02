package com.bolyartech.forge.base.misc;

/**
 * This interface abstracts getting time for time measurements in order to provide
 * standard OS independent way of getting time in milliseconds. This is not wall clock time but real time,
 * i.e. time measured since VM is started for example
 */
public interface TimeProvider {
    /**
     * Returns VM's time, i.e. wall clock independent time in milliseconds.
     * Use it to measure elapsed time for timeouts, benchmarks, etc.
     * @return VM's time in millis
     */
    @SuppressWarnings("unused")
    long getVmTime();

    /**
     * Returns wall clock time
     * Use it to get time that will be shown to the user (optionally after timezone adjustment)
     * @return wall clock time
     */
    @SuppressWarnings("unused")
    long getWallClockTime();
}
