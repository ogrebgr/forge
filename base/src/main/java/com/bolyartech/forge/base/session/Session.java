package com.bolyartech.forge.base.session;



public interface Session {
    boolean isLoggedIn();

    /**
     * @param ttl seconds
     */
    void startSession(int ttl);

    /**
     * mark session as last active current time
     */
    void prolong();

    void logout();
}
