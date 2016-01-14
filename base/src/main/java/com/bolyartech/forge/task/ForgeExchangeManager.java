package com.bolyartech.forge.task;

import com.bolyartech.forge.exchange.ForgeExchangeResult;
import com.bolyartech.forge.http.functionality.HttpFunctionality;


/**
 * Created by ogre on 2016-01-13 11:10
 */
public class ForgeExchangeManager extends ExchangeManagerImpl<ForgeExchangeResult> {
    public ForgeExchangeManager(TaskExecutor taskExecutor, HttpFunctionality httpFunc) {
        super(taskExecutor, httpFunc);
    }

}
