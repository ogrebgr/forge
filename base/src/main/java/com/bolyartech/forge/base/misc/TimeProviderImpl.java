package com.bolyartech.forge.base.misc;

public class TimeProviderImpl implements TimeProvider {

    /**
     * Returns wall clock independent time in milliseconds.
     * Based on System.nanoTime().
     * @return
     */
    @Override
    public long getTime() {
        return System.nanoTime() / 1000000;
    }
}
