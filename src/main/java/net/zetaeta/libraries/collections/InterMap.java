package net.zetaeta.libraries.collections;

import java.util.Map;

public interface InterMap<K, V> extends Map<K, V> {
    public V getByKey(K key);
    
    public K getByValue(V value);
    
    public V putKey(K key, V value);
    
    public K putValue(V value, K key);
    
    public boolean contains(Object o);
    
    public Object removeFull(Object o);
    
    public Object getFull(Object o);
    
    
}
