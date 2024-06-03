
import java.io.Serializable;
import java.util.Iterator;

import java.util.NoSuchElementException;

public class MyHashMap<K,V>  implements Iterable<V>,Serializable{
    
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;

    public MyHashMap() {
        size = 0;
        hashArray = new Object[100];
        keySet = new MyHashSet<K>();
    }

    public boolean isEmpty(){
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public V put(K k, V v) {
        
        Object hold = hashArray[k.hashCode()];
        hashArray[k.hashCode()] = v;
        size++;
        keySet.add(k);
        return (V)hold;
    }
    
    @SuppressWarnings("unchecked")
    public V get(Object o){ 
       // K ints= (K)o;
        return (V)hashArray[o.hashCode()];      
    }
    
    @SuppressWarnings("unchecked")
    public V remove(Object o) {
        Object hold = hashArray[o.hashCode()];
        hashArray[o.hashCode()] = null;
        keySet.remove(o);
        size--;
        return (V)hold;
    }

    public int size(){
        return size;
    }
    
    public  MyHashSet<K> keySet(){
        return keySet;
    }
    public DLList<V> values() {
        DLList<V> values = new DLList<>();
        for (Object entry : hashArray) {
            if (entry != null) {
                values.add((V) entry);
            }
        }
        return values;
    }
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            private int currentIndex = 0;

            public boolean hasNext() {
                while (currentIndex < hashArray.length && hashArray[currentIndex] == null) {
                    currentIndex++;
                }
                return currentIndex < hashArray.length;
            }

            public V next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (V) hashArray[currentIndex++];
            }

          
        };
    }
    public V getOrDefault(Object o, V defaultValue) {
        V value = get(o);
        return value != null ? value : defaultValue;
    }
   

  


    

    
}