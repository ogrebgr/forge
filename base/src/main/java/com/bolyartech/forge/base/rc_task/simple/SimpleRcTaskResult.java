package com.bolyartech.forge.base.rc_task.simple;


import com.bolyartech.forge.base.rc_task.RcTaskResult;


public class SimpleRcTaskResult<SUCCESS_VALUE> extends RcTaskResult<SUCCESS_VALUE, Void> {
    public SimpleRcTaskResult(SUCCESS_VALUE success_value) {
        super(true, success_value, null);
    }
}
