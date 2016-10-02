package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.builders.ForgeGetHttpExchangeBuilder;
import com.bolyartech.forge.base.exchange.builders.ForgePostHttpExchangeBuilder;
import com.bolyartech.forge.base.task.ForgeExchangeManager;


public interface ForgeExchangeHelper {
    @SuppressWarnings("unused")
    ForgePostHttpExchangeBuilder createForgePostHttpExchangeBuilder(String endpoint);
    @SuppressWarnings("unused")
    ForgeGetHttpExchangeBuilder createForgeGetHttpExchangeBuilder(String endpoint);
    @SuppressWarnings("unused")
    ForgeExchangeManager getExchangeManager();
}
