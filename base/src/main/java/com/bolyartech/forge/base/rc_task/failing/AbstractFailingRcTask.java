package com.bolyartech.forge.base.rc_task.failing;


import com.bolyartech.forge.base.rc_task.AbstractRcTask;


/**
 * Simplification of AbstractRcTask. Use it when a task does not provide success value but may provide error value/code.
 * For example executing HTTP request to delete a record, i.e. you just need to know if the operation is successful and if not - why?
 * @param <ERROR_VALUE>
 */
public abstract class AbstractFailingRcTask<ERROR_VALUE> extends AbstractRcTask<FailingRcTaskResult<ERROR_VALUE>> {
    protected AbstractFailingRcTask(int id) {
        super(id);
    }
}
