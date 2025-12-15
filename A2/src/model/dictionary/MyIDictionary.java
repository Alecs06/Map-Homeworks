package model.dictionary;

import java.util.Map;

public interface MyIDictionary<K,V> {
    V lookup(K key);
    void add(K key, V value);
    boolean isDefined(K key);
    MyIDictionary<K,V> deepCopy();
    Map<K,V> getContent();
}

