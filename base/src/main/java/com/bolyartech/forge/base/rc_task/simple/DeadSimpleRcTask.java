package com.bolyartech.forge.base.rc_task.simple;

import com.bolyartech.forge.base.rc_task.RcTask;


public interface DeadSimpleRcTask extends RcTask<SimpleRcTaskResult<?>> {
    void setDeadSimpleTaskResult(Object result);
}
