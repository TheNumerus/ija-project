/*
soubor: project.InternalClock.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: stará se o obsluhu vnitřních hodit simulace (spouštění, pozastavování, zrychlování, zpomalování, ...)
 */

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
    private final BiConsumer<Duration, Duration> tick;

    /**
     * Constructor
     * @param tick Tick function
     */
    public InternalClock(BiConsumer<Duration, Duration> tick) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    long mili = (long) (20.0 * speed);
                    Duration delta = Duration.ofMillis(mili);
                    time = time.plus(delta);
                    Platform.runLater(() -> tick.accept(time, delta));
                }
            }
        };
        this.tick = tick;
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

    /**
     * Jumps into future by speciied ammout
     *
     * Will not send event if deltaSecs is zero
     * @param deltaSecs time to jump
     * @return new time
     */
    public Duration jumpForward(Duration deltaSecs) {
        if (deltaSecs.isZero()) {
            return time;
        }
        time = time.plus(deltaSecs);
        tick.accept(time, deltaSecs);
        return time;
    }
}
