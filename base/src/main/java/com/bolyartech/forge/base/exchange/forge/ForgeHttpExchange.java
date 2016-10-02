package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.http.HttpFunctionality;
import okhttp3.Request;


/**
 * HTTP exchange that produces ForgeExchangeResult result
 *
 * {@see HttpExchange}
 */
public class ForgeHttpExchange extends HttpExchange<ForgeExchangeResult> {
    /**
     * Creates new ForgeHttpExchange
     * @param httpFunctionality HTTP functionality to be used
     * @param request Request to be send
     * @param resultProducer Result producer
     * @param tag Tag to be used. This is some object that you use to distinguish/identify different exchanges
     */
    @SuppressWarnings("unused")
    public ForgeHttpExchange(HttpFunctionality httpFunctionality,
                             Request request,
                             ResultProducer<ForgeExchangeResult> resultProducer,
                             Object tag) {
        super(httpFunctionality, request, resultProducer, tag);
    }


    /**
     * Creates new ForgeHttpExchange
     * @param httpFunctionality HTTP functionality to be used
     * @param request Request to be send
     * @param resultProducer Result producer
     */
    @SuppressWarnings("unused")
    public ForgeHttpExchange(HttpFunctionality httpFunctionality,
                             Request request,
                             ResultProducer<ForgeExchangeResult>
                                     resultProducer) {
        this(httpFunctionality, request, resultProducer,null);
    }
}
