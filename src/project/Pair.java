package project;

public class Pair<T, R> {
    private T x;
    private R y;

    public Pair(T x, R y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public R getY() {
        return y;
    }

    public void setY(R y) {
        this.y = y;
    }
}
