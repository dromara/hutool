package cn.hutool.event;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
     * @see EventManager#addListeners(cn.hutool.event.listener.BaseListener...)
     */
    @Override
    public void addListeners(BaseListener... listeners) {
        Validator.validateNotEmpty(listeners, "listeners can not be null");
        Validator.validateFalse(ArrayUtil.isEmpty(listeners), "listeners can not be null or empty");
        try {
            lock.writeLock().lock();
            List<BaseListener<BaseEvent>> copy = new ArrayList<>(this.defaultListenerKeeper.listeners);
            for (BaseListener listener : listeners) {
                copy.add(listener);
            }
            Collections.sort(copy, DefaultComparator.INSTANCE);
            //            this.defaultListenerKeeper.listeners = new CopyOnWriteArraySet<>(copy);
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
    public Collection<BaseListener<BaseEvent>> getAllListeners() {
        try {
            lock.readLock().lock();
            return this.defaultListenerKeeper.retrieveListeners();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @see EventManager#getAllListeners(cn.hutool.event.domain.BaseEvent)
     */
    @Override
    public Collection<BaseListener<BaseEvent>> getAllListeners(BaseEvent event) {
        Validator.validateNotEmpty(event, "event can not be null");
        try {
            lock.readLock().lock();
            LinkedList<BaseListener<BaseEvent>> allListeners = new LinkedList<>();
            for (BaseListener listener : this.defaultListenerKeeper.listeners) {
                // Check whether the event is supported by the listener
                if (listener.isSupportEvent(event)) {
                    allListeners.add(listener);
                }
            }
            return allListeners;
        } finally {
            lock.readLock().unlock();
        }

    }

    /**
     * Class ListenerKeeper...
     * 监听器本地容器
     *
     * @author Ted.L
     * Created on 2019-09-28
     */
    private class ListenerKeeper {
        /**注册事件容器*/
        public final Set<BaseListener<BaseEvent>> listeners;

        /**
         * 构造方法
         */
        public ListenerKeeper() {
            listeners = new LinkedHashSet<BaseListener<BaseEvent>>();
        }

        /**
         * 获取容器注册的监听器
         * @return
         */
        public Collection<BaseListener<BaseEvent>> retrieveListeners() {
            LinkedList<BaseListener<BaseEvent>> allListeners = new LinkedList<BaseListener<BaseEvent>>();
            for (BaseListener<BaseEvent> listener : this.listeners) {
                allListeners.add(listener);
            }
            return allListeners;
        }
    }

    /**
     * BaseListener 根据order排序规则
     */
    private enum DefaultComparator implements Comparator<BaseListener> {

                                                                        INSTANCE;

        @Override
        public int compare(BaseListener o1, BaseListener o2) {
            if (o1.order() > o2.order()) {
                return 1;
            } else if (o1.order() == o2.order()) {
                return 0;
            }
            return -1;
        }
    }
}