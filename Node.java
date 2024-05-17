import java.io.Serializable;

public class Node<E> implements Serializable {
    private Node<E> next;
    private Node<E> prev;
    private E data;

    public Node(E e) {
        next = null;
        prev = null;
        data = e;
    }

    public E get() {
        return data;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> prev() {
        return prev;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    public void set(E e) {
        data = e;
    }
}
