package net.zetaeta.bukkit.util.collections;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Map that works both ways, one can get a key from a value aswell as the other way round.
 * It still implements {@link java.util.Map Map}, and still has the key,value format, 
 * the difference being you can get the key from the value with {@link #getByValue(Object) getByValue(Object o)} 
 * or the generic {@link #getFull(Object) getFull(Object o)} which searches both keys and values. 
 * The {@link #get(Object) get(Object o)} can be used for both only if the map is of type SimpleInterHashMap<K, K>, 
 * i.e. it uses the same type for key and value. As it tries to convert returned objects to type V due to restrictions in the Map interface, 
 * it you use it with a map of two different types and it gets a key by a value, it will throw a ClassCastException.
 * */
public class SimpleInterHashMap<K, V> implements Map<K, V> {
	private Map<K, V> mapto = new HashMap<K, V>();
	private Map<V, K> mapfrom = new HashMap<V, K>();
	
	/**
	 * Gets
	 * */
	public V getByKey(K key) {
		return mapto.get(key);
	}
	
	public K getByValue(V value) {
		return mapfrom.get(value);
	}
	
	public V putKey(K key, V value) {
		V v = mapto.get(key);
		mapto.put(key, value);
		mapfrom.put(value, key);
		return v;
	}
	
	public K putValue(V value, K key) {
		K k = mapfrom.get(value);
		mapfrom.put(value, key);
		mapto.put(key, value);
		return k;
	}
	
	public boolean contains(Object o) {
		return mapto.containsKey(o) || mapfrom.containsKey(o);
	}
	
	public Object removeFull(Object o) {
		if (mapto.containsKey(o)) {
			removeReverse(mapfrom, o);
			return mapto.remove(o);
		}
		else if (mapfrom.containsKey(o)) {
			removeReverse(mapto, o);
			return mapfrom.remove(o);
		}
		return null;
	}
	
	public Object getFull(Object o) {
		if (mapto.containsKey(o)) {
			return mapto.get(o);
		}
		else if (mapfrom.containsKey(o)) {
			return mapfrom.get(o);
		}
		return null;
	}
	
	public V removeKey(K key) {
		if (mapto.containsKey(key)) {
			removeReverse(mapfrom, key);
			return mapto.remove(key);
		}
		return null;
	}
	
	public K removeValue(V value) {
		if (mapfrom.containsKey(value)) {
			removeReverse(mapto, value);
			return mapfrom.remove(value);
		}
		return null;
	}

	@Override
	public void clear() {
		mapto.clear();
		mapfrom.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return mapto.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return mapfrom.containsKey(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return mapto.entrySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) throws ClassCastException {
		if (mapto.containsKey(key)) {
			return mapto.get(key);
		}
		else if (mapfrom.containsKey(key)) {
			return (V) mapfrom.get(key);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return mapto.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return mapto.keySet();
	}

	@Override
	public V put(K key, V value) {
		if (contains(key)) {
			removeFull(key);
		}
		mapfrom.put(value, key);
		return mapto.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		mapto.putAll(m);
		mapfrom.putAll(reverseMap(m));
	}

	@Override
	public V remove(Object key) {
		removeReverse(mapfrom, key);
		return mapto.remove(key);
	}

	@Override
	public int size() {
		if (mapto.size() == mapfrom.size()) {
			return mapto.size();
		}
		return -1;
	}

	@Override
	public Collection<V> values() {
		return mapto.values();
	}
	
	// TODO: Fix up generic code
	@SuppressWarnings("unchecked")
	public static <T, S> Map<S, T> reverseMap (Map<T, S> map) {
		Set<Map.Entry<T, S>> entrySet = map.entrySet();
		Map<S, T> newMap;
		try {
			newMap =  map.getClass().getConstructor(new Class<?>[] {}).newInstance();
		} catch (InstantiationException e) {
			newMap = new HashMap<S, T>();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			newMap = new HashMap<S, T>();
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			newMap = new HashMap<S, T>();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			newMap = new HashMap<S, T>();
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			newMap = new HashMap<S, T>();
			e.printStackTrace();
		} catch (SecurityException e) {
			newMap = new HashMap<S, T>();
			e.printStackTrace();
		}
		
		for (Map.Entry<T, S> entry : entrySet) {
			newMap.put(entry.getValue(), entry.getKey());
		}
		
		return newMap;
	}
	
	public static <T, S> void removeReverse(Map<T, S> map, Object value) {
		Set<Map.Entry<T, S>> entrySet = map.entrySet();
		for (Map.Entry<T, S> entry : entrySet) {
			if (entry.getValue().equals(value)) {
				entrySet.remove(entry);
			}
		} 
	}
}
