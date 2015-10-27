package com.bolyartech.forge.exchange;


/**
 * Represents outcome of a exchange exchange
 *
 * @param <T> Type of the result of the exchange
 *            thread safety: full (this object is immutable)
 */
public class ExchangeOutcome<T> {
    private final Exchange<T> mExchange;
    private final T mResult;
    private final boolean mError;


    /**
     * Creates new ExchangeOutcome
     *
     * @param exchange {@link Exchange} that was executed
     * @param result   Result of the exchange of type <code>T</code> if exchange execution was successful
     * @param error    indicates if there was an error during exchange execution. If <code>true</code> may be <code>null</code>
     */
    public ExchangeOutcome(Exchange<T> exchange, T result, boolean error) {
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
    public Exchange<T> getExchange() {
        return mExchange;
    }


    /**
     * @return Result ot the exchange of type <code>T</code>
     */
    public T getResult() {
        return mResult;
    }
}