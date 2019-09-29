package cn.hutool.event;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.event.domain.BaseEvent;
import cn.hutool.event.listener.BaseListener;

/**
 * Class AbstractEventManager...
 *
 * @author Ted.L
 * Created on 2019-09-28
 */
public abstract class AbstractEventManager implements EventManager {

    /**
     * 默认事件监听器容器，保存所有注册的事件监听器
     */
    private final ListenerKeeper defaultListenerKeeper = new ListenerKeeper();

    /**
     * 锁
     */
    private final ReadWriteLock  lock                  = new ReentrantReadWriteLock();

    /**
     * @see EventManager#addListener(cn.hutool.event.listener.BaseListener)
     */
    @Override
    public void addListener(BaseListener listener) {
        Validator.validateNotEmpty(listener, "listener can not be null");
        try {
            lock.writeLock().lock();
            this.defaultListenerKeeper.listeners.add(listener);
            List<BaseListener> copy = new ArrayList<>(this.defaultListenerKeeper.listeners);
            Collections.sort(copy,new Comparator<BaseListener>() {
                @Override
                public int compare(BaseListener o1, BaseListener o2) {
                    if (o1.order() > o2.order()) {
                        return 1;
                    } else if (o1.order() == o2.order()) {
                        return 0;
                    }
                    return -1;
                }
            });
            this.defaultListenerKeeper.listeners.clear();
            this.defaultListenerKeeper.listeners.addAll(copy);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see EventManager#addListeners(cn.hutool.event.listener.BaseListener...)
     */
    @Override
    public void addListeners(BaseListener... listeners) {
        Validator.validateNotEmpty(listeners, "listeners can not be null");
        Validator.validateFalse(ArrayUtil.isEmpty(listeners), "listeners can not be null or empty");
        try {
            lock.writeLock().lock();
            for (BaseListener listener : listeners) {
                this.defaultListenerKeeper.listeners.add(listener);
            }
            List<BaseListener> copy = new ArrayList<>(this.defaultListenerKeeper.listeners);
            Collections.sort(copy,new Comparator<BaseListener>() {
                @Override
                public int compare(BaseListener o1, BaseListener o2) {
                    if (o1.order() > o2.order()) {
                        return 1;
                    } else if (o1.order() == o2.order()) {
                        return 0;
                    }
                    return -1;
                }
            });
            this.defaultListenerKeeper.listeners.clear();
            this.defaultListenerKeeper.listeners.addAll(copy);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see EventManager#removeListener(cn.hutool.event.listener.BaseListener)
     */
    @Override
    public void removeListener(BaseListener listener) {
        Validator.validateNotEmpty(listener, "listener can not be null");
        try {
            lock.writeLock().lock();
            this.defaultListenerKeeper.listeners.remove(listener);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see EventManager#removeAllListeners()
     */
    @Override
    public void removeAllListeners() {
        try {
            lock.writeLock().lock();
            this.defaultListenerKeeper.listeners.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see EventManager#getAllListeners()
     */
    @Override
    public Collection<BaseListener> getAllListeners() {
        return this.defaultListenerKeeper.retrieveListeners();
    }

    /**
     * @see EventManager#getAllListeners(cn.hutool.event.domain.BaseEvent)
     */
    @Override
    public Collection<BaseListener> getAllListeners(BaseEvent event) {
        Validator.validateNotEmpty(event, "event can not be null");
        LinkedList<BaseListener> allListeners = new LinkedList<>();
        for (BaseListener listener : this.defaultListenerKeeper.listeners) {
            // Check whether the event is supported by the listener
            if (listener.isSupportEvent(event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    /**
     * Class ListenerKeeper...
     * 监听器本地容器
     *
     * @author Ted.L
     * Created on 2019-09-28
     */
    private class ListenerKeeper {
        /**
         * 注册事件容器
         */
        public final Set<BaseListener> listeners;

        /**
         * 构造方法
         */
        public ListenerKeeper() {
            listeners = new LinkedHashSet<>();
        }

        /**
         * 获取容器注册的监听器
         *
         * @return 监听器集合
         */
        public Collection<BaseListener> retrieveListeners() {
            LinkedList<BaseListener> allListeners = new LinkedList<>();
            for (BaseListener listener : this.listeners) {
                allListeners.add(listener);
            }
            return allListeners;
        }
    }
}