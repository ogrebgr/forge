package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.ResultProducer;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Produces ForgeExchangeResult from a HTTP response that contains 'X-Forge-Result-Code' header
 */
public class ForgeHeaderResultProducer implements ResultProducer<ForgeExchangeResult> {
    private static final String FORGE_RESULT_CODE_HEADER = "X-Forge-Result-Code";


    /**
     * Creates new ForgeHeaderResultProducer
     */
    @SuppressWarnings("unused")
    @Inject
    public ForgeHeaderResultProducer() {
    }


    @Override
    public ForgeExchangeResult produce(Response resp) throws ResultProducerException {
        String codeStr = resp.header(FORGE_RESULT_CODE_HEADER);

        if (codeStr != null) {
            try {
                ResponseBody b = resp.body();
                if (b != null) {
                    String body = b.string();
                    b.close();
                    return new ForgeExchangeResult(Integer.valueOf(codeStr), body);
                } else {
                    throw new ResultProducerException("Body is empty");
                }
            } catch (NumberFormatException e) {
                throw new ResultProducerException("Non integer result code in header " +
                        FORGE_RESULT_CODE_HEADER + ": " + codeStr);
            } catch (IOException e) {
                throw new ResultProducerException("Error getting response body.", e);
            }
        } else {
            throw new ResultProducerException("Missing header " + FORGE_RESULT_CODE_HEADER);
        }
    }
}
