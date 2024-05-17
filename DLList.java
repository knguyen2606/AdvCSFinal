import java.io.Serializable;

public class DLList<E> implements Serializable{

    private int size;
    private Node<E> head;
    private Node<E> tail;
    
    public DLList(){
        head = new Node<E>(null);
        tail = new Node<E>(null);
        head.setPrev(null);
        head.setNext(tail);
        tail.setPrev(head);
        tail.setNext(null);
        size = 0;
    }


    private Node<E> getNode(int i) {
        if (i >= size || i < 0) {
            return null;
        //If located in second half
        } else if (i > size/2) {
            Node<E>current = tail;
            for (int j = 0; j < size - i; j++) {
                current = current.prev();
            }
            return current;
        //If located in first half
        } else {
            Node<E>current = head.next();
            for (int j = 0; j < i; j ++) {
                current = current.next();
            }
            return current;
        }
            

    }
    
    
    public boolean add(E e){
        if(e != null){
            Node<E> newNode = new Node<E>(e);
            Node<E> before = tail.prev();
            newNode.setPrev(before);
            newNode.setNext(tail);
            before.setNext(newNode);
            tail.setPrev(newNode);
            size++;
            return true;

        }
        return false;
       
        
    }

    public void add(int i, E e){
        if (i < 0 || i > size) {
            return;
        }
        Node<E> current = head;
        for(int j = 0; j<size + 1;j++){
            if(i==0){
                Node<E> newNode = new Node<E>(e);
                Node<E> after = head.next();
                newNode.setPrev(head);
                head.setNext(newNode);
                newNode.setNext(after);
                after.setPrev(newNode);
                size++;
                break;
            }
            
            if(j==i){
                Node<E> newNode = new Node<E>(e);
                Node<E> before = current;
                Node<E> after = current.next();
                newNode.setPrev(before);
                newNode.setNext(after);
                before.setNext(newNode);
                after.setPrev(newNode);
                size++;
                break;
            }
            current = current.next();
        }
    }

    public E get(int node2) {
        if (getNode(node2) != null)
            return getNode(node2).get();
        
        return null;
    }

    public int size() {
        return size;
    }

    public String toString() {
        String s = "";
        Node<E>current = head.next();
        for (int i = 0; i < size; i++) {
            s += current.get().toString() + "\n";
            current = current.next();
        }
        return s;
    }

    public E remove(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = getNode(i);
        if (current == null) {
            return null;
        }
        E removedData = current.get();
        Node<E> before = current.prev();
        Node<E> after = current.next();
        before.setNext(after);
        after.setPrev(before);
        size--;
        return removedData;
    }

    public void remove(E e) {
        Node<E>current = head.next();
        for (int i = 0; i < size; i++) {
            if (current.get().equals(e)) {
                Node<E> before = current.prev();
                current.prev().setNext(current.next());
                current.next().setPrev(before);
                size--;
                return;
            }
            current = current.next();
        }
    }
    

    public void set(int i, E e) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> current = head.next();
    
        for (int j = 0; j < size; j++) {
            if (j == i) {
                current.set(e);
                return;
            }
    
            current = current.next();
        }
    }
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
    public boolean contains(E e) {
        Node<E> current = head.next();
        while (current != tail) {
            if (current.get().equals(e)) {
                return true;
            }
            current = current.next();
        }
        return false;
    }
    public int get(E value) {
        Node<E> current = head.next();
        int index = 0;
        while (current != tail) {
            if (current.get().equals(value)) {
                return index;
            }
            current = current.next();
            index++;
        }
        return -1; 
    }







    
}