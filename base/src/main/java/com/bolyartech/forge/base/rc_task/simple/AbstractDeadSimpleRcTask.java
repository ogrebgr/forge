package com.bolyartech.forge.base.rc_task.simple;

import com.bolyartech.forge.base.rc_task.AbstractRcTask;


abstract public class AbstractDeadSimpleRcTask extends AbstractRcTask<SimpleRcTaskResult<?>> implements DeadSimpleRcTask {
    protected AbstractDeadSimpleRcTask(int id) {
        super(id);
    }


    @Override
    public void setDeadSimpleTaskResult(Object result) {
        setTaskResult(new SimpleRcTaskResult<>(result));
    }
}
