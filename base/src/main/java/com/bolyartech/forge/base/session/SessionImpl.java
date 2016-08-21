package com.bolyartech.forge.base.session;

import javax.inject.Inject;


public class SessionImpl implements Session {
    boolean mIsLoggedIn;


    private int mSessionTtl;
    private long mLastSessionProlong; //in seconds
    private Info mInfo;


    @Inject
    public SessionImpl() {
        super();
    }


    @Override
    public boolean isLoggedIn() {
        checkSessionExpired();
        return mIsLoggedIn;
    }


    @Override
    public void startSession(int ttl, Info info) {
        mSessionTtl = ttl;
        mInfo = info;
        setIsLoggedIn(true);
        prolong();
    }


    @Override
    public Info getInfo() {
        return mInfo;
    }


    private void checkSessionExpired() {
        if (mLastSessionProlong + mSessionTtl < (System.nanoTime() / 1_000_000_000)) {
            mIsLoggedIn = false;
        }
    }


    private void setIsLoggedIn(boolean isLoggedIn) {
        mIsLoggedIn = isLoggedIn;
    }


    @Override
    public void prolong() {
        if (mIsLoggedIn) {
            mLastSessionProlong = System.nanoTime() / 1_000_000_000;
        }
    }


    @Override
    public void logout() {
        setIsLoggedIn(false);
    }
}
