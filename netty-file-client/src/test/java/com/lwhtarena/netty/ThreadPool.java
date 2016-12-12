package com.lwhtarena.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static ExecutorService executorService;

    private ThreadPool() {
    }

    public static void addWork(final QueueTask task) {
        //创建一个可重用固定线程数的线程池
        if (executorService == null) {
            synchronized (ThreadPool.class) {
                if (executorService == null) {
                    /*executorService = new ThreadPoolExecutor(2, 1024,
                            60L, TimeUnit.SECONDS, workQueue);*/
                    executorService = Executors.newCachedThreadPool();
                }
            }
        }

        executorService.execute(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                try {
                    task.executeTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void shutdown() {
        executorService.shutdown();
    }
}