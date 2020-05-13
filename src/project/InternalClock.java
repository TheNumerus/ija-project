package project;

import javafx.application.Platform;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

public class InternalClock {
    private double speed = 1.0;
    private Duration time = Duration.ZERO;
    private BiConsumer<Duration, Duration> onTick;

    private Timer t;

    public InternalClock(BiConsumer<Duration, Duration> c) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long mili = (long)(20.0 * speed);
                Duration delta = Duration.ofMillis(mili);
                time = time.plus(delta);
                Platform.runLater(() ->c.accept(time, delta));
            }
        };
        t = new Timer();
        t.scheduleAtFixedRate(task, 0, 20);
    }

    public void cancel() {
        t.cancel();
    }
}
