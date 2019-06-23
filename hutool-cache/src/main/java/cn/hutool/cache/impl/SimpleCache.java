package cn.hutool.cache.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.babel.Supplier;
import sun.misc.Cleaner;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCache<K,V> implements Cache<K, V> {
    private ConcurrentHashMap<K,SoftValue<K,V>> map;
    public SimpleCache() {
        this.map=new ConcurrentHashMap<K,SoftValue<K,V>>();
    }


    @Override
    public void put(K key, V value) {
        if (value==null){
            map.put(key,SoftValue.NULL_OBJECT);
        }
        map.put(key,new SoftValue<K,V>(this.map,key,value));
    }


    @Override
    public V get(K key) {
        int i = key.hashCode();
        SoftValue<K,V> sv = getValueWrapper(key);
        if (sv==null){
            return null;
        }
        if (sv.get()==null){
            return null;
        }
        V v = sv.get();
        return v==SoftValue.NULL_OBJECT?null:v;
    }
    public  Set<K> keySet(){
        return  map.keySet();
    }
    public V getOrDefault(K key, Supplier<V> supplier){
        SoftValue<K,V> sv = getValueWrapper(key);
        if (sv==null){
            return supplier.get();
        }
        if (sv.get()==null){
            return supplier.get();
        }
        V v = sv.get();
        return v==SoftValue.NULL_OBJECT?null:v;
    }
    private SoftValue<K,V> getValueWrapper(K key){
        return map.get(key);
    }
    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 简单缓存，不应该太在意这个是不是还在缓存中了，如果不是 调用方应当有办法处理
     * @param key KEY
     * @return
     */
    @Deprecated
    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public void put(K key, V value, long timeout) {
        put(key,value);
    }

    @Override
    public V get(K key, boolean isUpdateLastAccess) {
        return get(key);
    }

    /**
     * 简单缓存，没法支持这么复杂的功能
     * @return
     */
    @Deprecated
    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public V next() {
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }

    /**
     * 简单缓存，没法支持这么复杂的功能
     * @return
     */
    @Deprecated
    public Iterator<CacheObj<K, V>> cacheObjIterator() {
        return new Iterator<CacheObj<K, V>>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public CacheObj<K, V> next() {
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }

    @Deprecated
    @Override
    public int capacity() {
        return 0;
    }
    @Deprecated
    @Override
    public long timeout() {
        return 0;
    }

    @Deprecated
    @Override
    public int prune() {
        return 0;
    }
    @Deprecated
    @Override
    public boolean isFull() {
        return false;
    }
    @Deprecated
    @Override
    public void remove(K key) {
        map.remove(key);
    }
    private static class SoftValue<K,V> extends SoftReference<V>{
        public static final SoftValue NULL_OBJECT=null;

        public SoftValue(final ConcurrentHashMap<K,SoftValue<K,V>> cacheMap,final K key,final V referent) {
            super(referent);
            Cleaner.create(referent, new Runnable() {
                @Override
                public void run() {
                    cacheMap.remove(key);
                }
            });
        }
    }

}
