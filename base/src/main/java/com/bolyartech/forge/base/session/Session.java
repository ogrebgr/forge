package com.bolyartech.forge.base.session;


import com.bolyartech.forge.base.misc.StringUtils;


public interface Session {
    boolean isLoggedIn();

    /**
     * @param ttl seconds
     */
    void startSession(int ttl, Info info);

    Info getInfo();

    /**
     * mark session as last active current time
     */
    void prolong();

    void logout();


    class Info {
        private final long mUserId;
        private String mScreenName;


        public Info(long userId, String screenName) {
            mUserId = userId;
            mScreenName = screenName;
        }


        public long getUserId() {
            return mUserId;
        }


        public void setScreenName(String screenName) {
            mScreenName = screenName;
        }


        public String getScreenName() {
            return mScreenName;
        }


        public boolean hasScreenName() {
            return StringUtils.isNotEmpty(mScreenName);
        }
    }
}
