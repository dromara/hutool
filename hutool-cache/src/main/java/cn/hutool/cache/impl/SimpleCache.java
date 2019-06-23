package cn.hutool.cache.impl;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.babel.Supplier;
import sun.misc.Cleaner;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 您使用这个缓存的场景：
 *
 * 我不在乎这里有没有这个缓存，有最好没有也无所谓，但是无论如何不能让我的程序出现OOM
 * @param <K>
 * @param <V>
 */
public class SimpleCache<K,V> implements Cache<K, V> {
    private ConcurrentHashMap<K,SoftValue<K,V>> map;
    public SimpleCache() {
        this.map=new ConcurrentHashMap<K,SoftValue<K,V>>();
    }

    /**
     * 添加缓存 k-v
     * k存在就覆盖
     * @param key 键
     * @param value
     */
    @Override
    public void put(K key, V value) {
        if (value==null){
            map.put(key,SoftValue.NULL_OBJECT);
        }
        map.put(key,new SoftValue<K,V>(this.map,key,value));
    }

    /**
     * 通过k获取v
     * @param key 键
     * @return null if cache not available anymore OR cache value is null
     */
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

    /**
     * 当前仍在缓存中的键集合
     * @return
     */
    public  Set<K> keySet(){
        return  map.keySet();
    }

    /**
     * 通过k获取v，如果缓存不可用，就从supplier方法获取v
     * @param key
     * @param supplier
     * @return
     */
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
        map.clear();
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
    @Deprecated
    @Override
    public void put(K key, V value, long timeout) {
        put(key,value);
    }
    @Deprecated
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
