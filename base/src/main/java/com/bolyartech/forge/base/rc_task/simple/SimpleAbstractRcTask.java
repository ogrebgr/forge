package com.bolyartech.forge.base.rc_task.simple;


import com.bolyartech.forge.base.rc_task.AbstractRcTask;


/**
 * Simplification of AbstractRcTask. Use it when the operation does not provide specific error value/code/
 * @param <SUCCESS_VALUE>
 * @deprecated Please use {@link AbstractSimpleRcTask}
 */
@Deprecated
public abstract class SimpleAbstractRcTask<SUCCESS_VALUE> extends AbstractRcTask<SimpleRcTaskResult<SUCCESS_VALUE>> {
    protected SimpleAbstractRcTask(int id) {
        super(id);
    }
}
