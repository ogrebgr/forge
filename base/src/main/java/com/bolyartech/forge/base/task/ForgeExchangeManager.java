package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.ForgeExchangeResult;


/**
 * Created by ogre on 2016-01-13 11:10
 */
public class ForgeExchangeManager extends ExchangeManagerImpl<ForgeExchangeResult> {
    public ForgeExchangeManager(TaskExecutor<ForgeExchangeResult> taskExecutor) {
        super(taskExecutor);
    }

}