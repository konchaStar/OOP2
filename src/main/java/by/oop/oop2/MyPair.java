package by.oop.oop2;

import java.io.Serializable;

public class MyPair<K, V> implements Serializable {
    private K key;
    private V value;
    public MyPair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
