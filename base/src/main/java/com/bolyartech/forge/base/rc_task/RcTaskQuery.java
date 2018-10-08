package com.bolyartech.forge.base.rc_task;

public interface RcTaskQuery {
    int getId();

    boolean isEnded();

    boolean isCancelled();

    boolean isSuccess();

    boolean isFailure();
}
