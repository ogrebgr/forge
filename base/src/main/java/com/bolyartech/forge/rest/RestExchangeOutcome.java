package com.bolyartech.forge.rest;


/**
 * Represents outcome of a rest exchange
 *
 * @param <T> Type of the result of the exchange
 *            thread safety: full (this object is immutable)
 */
public class RestExchangeOutcome<T> {
    private final RestExchange<T> mExchange;
    private final T mResult;
    private final boolean mError;


    /**
     * Creates new RestExchangeOutcome
     *
     * @param exchange {@link RestExchange} that was executed
     * @param result   Result of the exchange of type <code>T</code> if exchange execution was successful
     * @param error    indicates if there was an error during exchange execution. If <code>true</code> may be <code>null</code>
     */
    public RestExchangeOutcome(RestExchange<T> exchange, T result, boolean error) {
        super();
        mExchange = exchange;
        mResult = result;
        mError = error;
    }


    /**
     * @return <code>true</code> if there was error in exchange execution, <code>false</code> otherwise
     */
    public boolean isError() {
        return mError;
    }


    /**
     * @return executed exchange
     */
    public RestExchange<T> getExchange() {
        return mExchange;
    }


    /**
     * @return Result ot the exchange of type <code>T</code>
     */
    public T getResult() {
        return mResult;
    }
}