package com.bolyartech.forge.base.rc_task.failing;


import com.bolyartech.forge.base.rc_task.AbstractRcTask;


public abstract class FailingAbstractRcTask<ERROR_VALUE> extends AbstractRcTask<FailingRcTaskResult<ERROR_VALUE>> {
    protected FailingAbstractRcTask(int id) {
        super(id);
    }
}
