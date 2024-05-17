import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyHashSet<E> implements Iterable<E>,Serializable{
    private Object[] hashArray;
    private int size;
    private DLList<E> myList = new DLList<E>();

    public MyHashSet() {
        hashArray = new Object[100];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean add(E ele) {
        if (hashArray[ele.hashCode()] == null) {
            hashArray[ele.hashCode()] = ele;
            size++;
            myList.add(ele);
            return true;
        }
        return false;
    }

    public void clear() {
        size = 0;
        hashArray = new Object[2180];
    }

    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (hashArray[o.hashCode()] == null) {
            return false;
        } else {
            hashArray[o.hashCode()] = null;
            myList.remove((E) o);
            return true;
        }
    }

    public boolean contains(Object ele) {
        return hashArray[ele.hashCode()] != null;
    }

    public DLList<E> toDLList() {
        return myList;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyHashSetIterator();
    }

    private class MyHashSetIterator implements Iterator<E> {
        private int currentIndex = 0;

        public boolean hasNext() {
            while (currentIndex < hashArray.length && hashArray[currentIndex] == null) {
                currentIndex++;
            }
            return currentIndex < hashArray.length;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) hashArray[currentIndex++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
