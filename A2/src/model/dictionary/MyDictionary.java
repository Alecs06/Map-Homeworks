package model.dictionary;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {
    private final Map<K,V> content = new HashMap<>();

    @Override
    public V lookup(K key) {
        return content.get(key);
    }

    @Override
    public void add(K key, V value) {
        content.put(key, value);
    }

    @Override
    public boolean isDefined(K key) {
        return content.containsKey(key);
    }

    @Override
    public MyIDictionary<K,V> deepCopy() {
        MyDictionary<K,V> copy = new MyDictionary<>();
        copy.content.putAll(this.content);
        return copy;
    }

    @Override
    public Map<K,V> getContent() {
        return content;
    }
}

