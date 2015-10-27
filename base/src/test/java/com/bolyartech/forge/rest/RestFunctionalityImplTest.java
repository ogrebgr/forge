package com.bolyartech.forge.rest;

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by ogre on 2015-10-15
 */
public class RestFunctionalityImplTest {

    @Test(expected = NullPointerException.class)
    public void test_constructor() {

        RestFunctionalityImpl impl = new RestFunctionalityImpl(null);
    }


    @Test
    public void test_execute() throws RestExchangeFailedException, IOException {
        HttpFunctionality http = mock(HttpFunctionality.class);
        @SuppressWarnings("unchecked") RestExchange<KhRestResult> x = mock(RestExchange.class);
        KhRestResult rez = new KhRestResult(1, "");
        when(x.execute(http)).thenReturn(rez);
        RestFunctionalityImpl impl = new RestFunctionalityImpl(http);
        KhRestResult rez2 = impl.execute(x);
        assertTrue(rez2 == rez);
    }
}
