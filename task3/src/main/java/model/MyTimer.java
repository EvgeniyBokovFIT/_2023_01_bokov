package model;

import model.listener.TimerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
    private int seconds;
    private Timer timer;
    private final List<TimerListener> timerListeners = new ArrayList<>();

    public void addTimerListener(TimerListener timerListener) {
        this.timerListeners.add(timerListener);
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void start() {
        stop();
        this.seconds = 0;
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MyTimer.this.seconds++;
                MyTimer.this.timerListeners.forEach(listener -> listener.onTimerUpdate(MyTimer.this.seconds));
            }
        }, 1000, 1000);
    }

    public void stop() {
        if(this.timer != null) {
            this.timer.cancel();
        }
    }
}
