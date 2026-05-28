package br.edu.router.util;

public class Timer {
    private long startTime;
    private long endTime;

    public void start() {
        this.startTime = System.nanoTime();
    }
    public void stop() {
        this.endTime = System.nanoTime();
    }
    public long getElapsedTimeInNanoseconds() {
        return this.endTime - this.startTime;
    }
}
