package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.builders.ForgeGetHttpExchangeBuilder;
import com.bolyartech.forge.base.exchange.builders.ForgePostHttpExchangeBuilder;
import com.bolyartech.forge.base.exchange.builders.ForgePostJsonBodyHttpExchangeBuilder;


/**
 * Defines interface for forge exchange helper
 */
public interface ForgeExchangeHelper {
    /**
     * Creates forge POST HTTP exchange builder
     * @param endpoint Endpoint path, e.g. 'api/1.0/login'
     * @return forge POST HTTP exchange builder
     */
    @SuppressWarnings("unused")
    ForgePostHttpExchangeBuilder createForgePostHttpExchangeBuilder(String endpoint);

    /**
     * Creates forge GET HTTP exchange builder
     * @param endpoint Endpoint path, e.g. 'api/1.0/login'
     * @return forge GET HTTP exchange builder
     */
    @SuppressWarnings("unused")
    ForgeGetHttpExchangeBuilder createForgeGetHttpExchangeBuilder(String endpoint);


    /**
     * Creates forge POST HTTP exchange builder (Json body)
     *
     * @param endpoint Endpoint path, e.g. 'api/1.0/login'
     * @return forge POST HTTP exchange builder
     */
    ForgePostJsonBodyHttpExchangeBuilder createForgePostJsonBodyHttpExchangeBuilder(String endpoint);
}
