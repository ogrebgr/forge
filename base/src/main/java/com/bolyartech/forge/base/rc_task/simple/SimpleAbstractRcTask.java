package com.bolyartech.forge.base.rc_task.simple;


import com.bolyartech.forge.base.rc_task.AbstractRcTask;


public abstract class SimpleAbstractRcTask<SUCCESS_VALUE> extends AbstractRcTask<SimpleRcTaskResult<SUCCESS_VALUE>> {
    protected SimpleAbstractRcTask(int id) {
        super(id);
    }
}
