package project;

import java.util.Objects;

/**
 * Tuple class
 * @param <T> first type
 * @param <R> second type
 */
public class Pair<T, R> {
    private T x;
    private R y;

    /**
     * Creates new {@link Class}
     * @param x
     * @param y
     */
    public Pair(T x, R y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets first parameter
     * @return first parameter
     */
    public T getX() {
        return x;
    }

    /**
     * Sets first parameter
     * @param x new first parameter
     */
    public void setX(T x) {
        this.x = x;
    }

    /**
     * Gets second parameter
     * @return second parameter
     */
    public R getY() {
        return y;
    }

    /**
     * Sets second parameter
     * @param y new second parameter
     */
    public void setY(R y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return x.equals(pair.x) && y.equals(pair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
