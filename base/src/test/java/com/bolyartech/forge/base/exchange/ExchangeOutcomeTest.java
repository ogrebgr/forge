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
        Exchange<ForgeExchangeResult> x = mock(Exchange.class);
        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
        ExchangeOutcome<ForgeExchangeResult> out = new ExchangeOutcome<>(x, rez, false);

        assertTrue(out.getExchange() == x);
        assertTrue(out.getResult() == rez);
        //noinspection PointlessBooleanExpression
        assertTrue(out.isError() == false);
    }
}
