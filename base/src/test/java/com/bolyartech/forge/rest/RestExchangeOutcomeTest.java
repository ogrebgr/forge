package com.bolyartech.forge.rest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Created by ogre on 2015-10-15
 */
public class RestExchangeOutcomeTest {
    @Test
    public void test() {
        RestExchange<KhRestResult> x = mock(RestExchange.class);
        KhRestResult rez = new KhRestResult(1, "");
        RestExchangeOutcome<KhRestResult> out = new RestExchangeOutcome<>(x, rez, false);

        assertTrue(out.getExchange() == x);
        assertTrue(out.getResult() == rez);
        assertTrue(out.isError() == false);
    }
}
