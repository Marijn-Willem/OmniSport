package com.sports.logic.async;

import java.util.List;

public class ThreadUtil {
    public static void executeAsync(List<ThreadWorker> threadWorkerList, boolean block, int maxThreads) {
        int nrThreads = Math.min(threadWorkerList.size(), maxThreads);
        Thread[] threadList = new Thread[nrThreads];

        for (int i = 0; i < nrThreads; i++) {
            Thread thread = getThread(threadWorkerList, i, nrThreads);
            threadList[i] = thread;
            thread.start();
        }

        if (block)
            try {
                for (Thread thread : threadList)
                    thread.join();
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

    private static Thread getThread(List<ThreadWorker> threadWorkerList, int startI, int step) {
        return new Thread(() -> {
            for (int i = startI; i < threadWorkerList.size(); i += step)
                executeThreadWorker(threadWorkerList.get(i));
        });
    }

    private static void executeThreadWorker(ThreadWorker threadWorker) {
        int retryCount = 0;

        while (retryCount < 10) {
            try {
                threadWorker.doWork();
                retryCount = 10;
            }
            catch (Exception e) {
                e.printStackTrace();
                retryCount += 1;
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}
