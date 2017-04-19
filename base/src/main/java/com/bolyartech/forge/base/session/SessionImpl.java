package com.bolyartech.forge.base.session;

import com.bolyartech.forge.base.misc.TimeProvider;

import javax.inject.Inject;


/**
 * User session
 * This class is threadsafe
 */
@SuppressWarnings("WeakerAccess")
public class SessionImpl implements Session {
    boolean mIsLoggedIn;


    private int mSessionTtl;
    private long mLastSessionProlong; //in seconds
    private final TimeProvider mTimeProvider;


    /**
     * Creates new SessionImpl
     * @param timeProvider Time provider
     */
    @SuppressWarnings("unused")
    @Inject
    public SessionImpl(TimeProvider timeProvider) {
        mTimeProvider = timeProvider;
    }


    @Override
    public synchronized boolean isLoggedIn() {
        checkSessionExpired();
        return mIsLoggedIn;
    }


    @Override
    public synchronized void startSession(int ttl) {
        mSessionTtl = ttl;
        setIsLoggedIn(true);
        prolong();
    }


    private void checkSessionExpired() {
        if (mLastSessionProlong + mSessionTtl < (mTimeProvider.getVmTime() / 1_000)) {
            mIsLoggedIn = false;
        }
    }


    private void setIsLoggedIn(boolean isLoggedIn) {
        mIsLoggedIn = isLoggedIn;
    }


    @Override
    public synchronized void prolong() {
        if (mIsLoggedIn) {
            mLastSessionProlong = (mTimeProvider.getVmTime() / 1_000);
        }
    }


    @Override
    public synchronized void logout() {
        setIsLoggedIn(false);
    }
}
