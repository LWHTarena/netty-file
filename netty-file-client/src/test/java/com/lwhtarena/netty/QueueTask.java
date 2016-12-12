package com.lwhtarena.netty;

/**
 * 队列任务接口
 *
 * @author Lee
 */
public interface QueueTask {
    /**
     * 队列执行任务
     */
    public void executeTask() throws Exception;
}