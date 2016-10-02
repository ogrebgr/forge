package com.bolyartech.forge.base.session;



public interface Session {
    @SuppressWarnings("unused")
    boolean isLoggedIn();

    /**
     * @param ttl seconds
     */
    @SuppressWarnings("unused")
    void startSession(int ttl);

    /**
     * mark session as last active current time
     */
    @SuppressWarnings("unused")
    void prolong();

    @SuppressWarnings("unused")
    void logout();
}
