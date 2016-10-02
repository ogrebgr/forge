package com.bolyartech.forge.base.session;


/**
 * Defines a user session
 */
public interface Session {
    /**
     * Checks if user is logged in, i.e. authenticated
     * @return true if the user is logged in, false otherwise
     */
    @SuppressWarnings("unused")
    boolean isLoggedIn();

    /**
     * Starts the session
     * @param ttl seconds
     */
    @SuppressWarnings("unused")
    void startSession(int ttl);

    /**
     * mark session as last active current time
     */
    @SuppressWarnings("unused")
    void prolong();

    /**
     * Logout, i.e. closing the session
     */
    @SuppressWarnings("unused")
    void logout();
}
