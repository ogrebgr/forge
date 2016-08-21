package com.bolyartech.forge.base.exchange;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Created by ogre on 2015-10-15
 */
public class ExchangeOutcomeTest {
    @Test
    public void test() {
        @SuppressWarnings("unchecked") Exchange<com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult> x = mock(Exchange.class);
        com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult rez = new com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult(1, "");
        ExchangeOutcome<com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult> out = new ExchangeOutcome<>(x, rez, false);

        assertTrue(out.getExchange() == x);
        assertTrue(out.getResult() == rez);
        //noinspection PointlessBooleanExpression
        assertTrue(out.isError() == false);
    }
}
