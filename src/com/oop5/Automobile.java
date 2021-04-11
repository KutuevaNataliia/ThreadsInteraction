package com.oop5;

import java.util.concurrent.atomic.AtomicInteger;

public class Automobile implements Runnable{
    private volatile AtomicInteger distance = new AtomicInteger();
    private static final int sleepTime = 500;
    private int speed;
    private volatile boolean paused = false;
    private final Object lock = new Object();

    public class Engine
    {

    }

    class Wheel {

    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (lock) {
                if (paused) {
                    try {
                        synchronized (lock) {
                            lock.wait();
                        }
                    } catch (InterruptedException e) {}
                }
            }
            try {
                Thread.sleep(sleepTime);
                int prev = distance.addAndGet(speed * sleepTime);
                System.out.printf("%d %d \n", speed, distance.get());
                //               Thread.yield();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

    public Automobile(int speed) {
        this.speed = speed;
    }

    public int getDistance() {
        return distance.get();
    }

    public static int getSleepTime() {
        return sleepTime;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
