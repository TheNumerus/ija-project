package project.map;

public class Node<T> {
    public T x;
    public T y;

    public Node(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node<?> other = (Node<?>)o;
        return x == other.x && y == other.y;
    }
}
