package com.soft.netty.server;

/**
 * @author 野性的呼唤
 * @date 2022/11/13 15:50
 * @description
 */
public class WaitDemo {

    private static volatile WaitDemo waitNotify;

    private WaitDemo() {
    }

    public static WaitDemo getWaitNotify() {
        if (waitNotify == null) {
            synchronized(WaitDemo.class) {
                if (waitNotify == null) {
                    waitNotify = new WaitDemo();
                }
            }
        }

        return waitNotify;
    }

    public void read() {
        synchronized(this) {
            this.notifyAll();
        }
    }
}
