package com.bolyartech.forge.base.rc_task;


abstract public class AbstractRcTask<T extends RcTaskResult> implements RcTask<T> {
    private final int id;

    private volatile boolean isCancelled = false;
    private volatile boolean isEnded = false;
    private volatile boolean isSuccess = false;

    private volatile T result;


    protected AbstractRcTask(int id) {
        this.id = id;
    }


    @Override
    public int getId() {
        return id;
    }


    @Override
    public void cancel() {
        isCancelled = true;
    }


    @Override
    public boolean isEnded() {
        return isEnded;
    }


    @Override
    public boolean isCancelled() {
        return isCancelled;
    }


    @Override
    public boolean isSuccess() {
        return isSuccess;
    }


    @Override
    public boolean isFailure() {
        return !isSuccess;
    }


    @Override
    public T getResult() {
        if (!isEnded) {
            throw new IllegalStateException("Task is not ended");
        }
        return result;
    }


    protected void setTaskResult(T result) {
        this.isEnded = true;
        this.result = result;
        this.isSuccess = result.isSuccess();
    }
}
