package com.bolyartech.forge.base.rc_task;


public interface RcTask<T extends RcTaskResult> extends RcTaskToExecutor, RcTaskQuery {
    // Manage
    void cancel();

    // Consume
    T getResult();


    interface Listener {
        void onTaskEnded(RcTask<?> task);
    }
}
