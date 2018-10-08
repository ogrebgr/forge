package com.bolyartech.forge.base.rc_task.failing;

import com.bolyartech.forge.base.rc_task.RcTaskResult;


public class FailingRcTaskResult<ERROR_VALUE> extends RcTaskResult<Void, ERROR_VALUE> {
    public FailingRcTaskResult(boolean isSuccess, ERROR_VALUE error_value) {
        super(isSuccess, null, error_value);
    }
}
