package com.bolyartech.forge.base.session;

import com.bolyartech.forge.base.misc.TimeProvider;

import javax.inject.Inject;


/**
 * User session
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
    public boolean isLoggedIn() {
        checkSessionExpired();
        return mIsLoggedIn;
    }


    @Override
    public void startSession(int ttl) {
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
    public void prolong() {
        if (mIsLoggedIn) {
            mLastSessionProlong = (mTimeProvider.getVmTime() / 1_000);
        }
    }


    @Override
    public void logout() {
        setIsLoggedIn(false);
    }
}
