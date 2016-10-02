package com.bolyartech.forge.base.misc;

/**
 * Time provider
 */
public class TimeProviderImpl implements TimeProvider {
    @Override
    public long getVmTime() {
        return System.nanoTime() / 1000000;
    }


    @Override
    public long getWallClockTime() {
        return System.currentTimeMillis();
    }
}
