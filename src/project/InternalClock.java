package project;

import javafx.application.Platform;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

/**
 * Class for controlling simulation time
 */
public class InternalClock {
    private Duration time = Duration.ZERO;
    private boolean paused = false;

    private static final double[] speeds = {0.1, 0.2, 0.5, 1.0, 2.0, 5.0, 10.0};
    private int speedIndex = 3;
    private double speed = speeds[speedIndex];

    private final Timer t;

    /**
     * Constructor
     * @param c Tick function
     */
    public InternalClock(BiConsumer<Duration, Duration> c) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (paused) {
                    return;
                }
                long mili = (long)(20.0 * speed);
                Duration delta = Duration.ofMillis(mili);
                time = time.plus(delta);
                Platform.runLater(() ->c.accept(time, delta));
            }
        };
        t = new Timer();
        t.scheduleAtFixedRate(task, 0, 20);
    }

    /**
     * Stops timer
     *
     * Must be called, or the program won't close
     */
    public void cancel() {
        t.cancel();
    }

    /**
     * Returns sim speed
     * @return Simulation speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Resets sim speed
     */
    public void resetSpeed() {
        speedIndex = 3;
        speed = speeds[speedIndex];
    }

    /**
     * Returns if sim is paused
     * @return Sim state
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets paused state
     * @param paused new state
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Resets internal clock
     */
    public void resetTime() {
        time = Duration.ZERO;
    }

    /**
     * Speeds up internal clock
     * @return new speed
     */
    public double speedUp() {
        if (speedIndex + 1 <= speeds.length) {
            speedIndex++;
            speed = speeds[speedIndex];
        }
        return speed;
    }

    /**
     * Slows internal clock
     * @return new speed
     */
    public double speedDown() {
        if (speedIndex != 0) {
            speedIndex--;
            speed = speeds[speedIndex];
        }
        return speed;
    }
}
