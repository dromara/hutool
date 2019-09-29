package cn.hutool.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.event.domain.BaseEvent;
import cn.hutool.event.exception.ListenerExecuteException;
import cn.hutool.event.listener.BaseListener;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * Class DefaultEventManager...
 * 简单事件广播器实现，同步、异步广播事件，并从事件监听器注册容器中，获取正确的监听器进行事件处理
 * @author Ted.L
 * Created on 2019-09-28
 */
public final class DefaultEventManager extends AbstractEventManager implements Builder<DefaultEventManager> {
    /** logger */
    private static final Log                       log = LogFactory.get();

    /** 线程池 */
    private final AtomicReference<ExecutorService> reference;

    /** 异常处理 */
    private Thread.UncaughtExceptionHandler        executeExceptionHandler;

    /** 事件拒绝分发异常 */
    private AbstractEventBroadcastRejectedHandler  eventBroadcastRejectedHandler;

    public static final class EventManagerBuilder {
        /** 初始池大小 */
        private int                                   eventHandlerSize;
        /** 最大池大小（允许同时执行的最大线程数） */
        private int                                   eventHandlerMaxSize;
        /** 队列，用于存在未执行的线程 */
        private BlockingQueue<Runnable>               eventQueue;
        /** 异常处理 */
        private Thread.UncaughtExceptionHandler       executeExceptionHandler;

        /** 事件拒绝分发异常 */
        private AbstractEventBroadcastRejectedHandler eventBroadcastRejectedHandler;

        public EventManagerBuilder eventHandlerSize(int eventHandlerSize) {
            this.eventHandlerSize = eventHandlerSize;
            return this;
        }

        public EventManagerBuilder eventHandlerMaxSize(int eventHandlerMaxSize) {
            this.eventHandlerMaxSize = eventHandlerMaxSize;
            return this;
        }

        public EventManagerBuilder eventQueue(BlockingQueue<Runnable> eventQueue) {
            this.eventQueue = eventQueue;
            return this;
        }

        public EventManagerBuilder executeExceptionHandler(Thread.UncaughtExceptionHandler executeExceptionHandler) {
            this.executeExceptionHandler = executeExceptionHandler;
            return this;
        }

        public EventManagerBuilder eventBroadcastRejectedHandler(AbstractEventBroadcastRejectedHandler eventBroadcastRejectedHandler) {
            this.eventBroadcastRejectedHandler = eventBroadcastRejectedHandler;
            return this;
        }

        /**
         * 默认每2MB内存一个size,最小1024
         * @return  默认队列size
         */
        private BlockingQueue<Runnable> getDefaultQueue() {
            int minVal = 1024;
            long maxMemory = Runtime.getRuntime().maxMemory() / (2 << 20);
            maxMemory--;
            maxMemory |= maxMemory >> 1;
            maxMemory |= maxMemory >> 2;
            maxMemory |= maxMemory >> 4;
            maxMemory |= maxMemory >> 8;
            maxMemory |= maxMemory >> 16;
            maxMemory++;

            return new LinkedBlockingQueue<>((int) (maxMemory < minVal ? minVal : maxMemory));
        }

        /**
         * 默认 应用场景比例 IO密集型 > 计算密集型
         * 异步广播事件，常用于日志记录,kafka埋点，写文本log，又或者进行逻辑处理后写入缓存？
         * 复杂算法应用在大多数场景下并不常见。
         * @return  默认线程池最大线程数
         */
        private int getDefaultMaxSize() {
            int cpus = Runtime.getRuntime().availableProcessors();
            return cpus * 2;
        }

        /**
         * 默认采用虚拟CPU核心数,注意超线程和虚拟容器的设置
         * @return  默认线程池核心线程数
         */
        private int getDefaultCoreSize() {
            int cpus = Runtime.getRuntime().availableProcessors();
            return cpus;
        }

        /**
         * 实际建造
         * @return {@link DefaultEventManager}
         */
        public EventManager build() {
            //初始池大小
            int eventHandlerSize = this.eventHandlerSize <= 0 ? getDefaultCoreSize() : this.eventHandlerSize;
            //最大池大小
            int eventHandlerMaxSize = this.eventHandlerMaxSize <= 0 ? getDefaultMaxSize() : this.eventHandlerMaxSize;
            //队列，用于存在未执行的线程
            BlockingQueue<Runnable> eventQueue = ObjectUtil.defaultIfNull(this.eventQueue, getDefaultQueue());
            //事件拒绝分发异常
            AbstractEventBroadcastRejectedHandler eventBroadcastRejectedHandler = ObjectUtil.defaultIfNull(this.eventBroadcastRejectedHandler,
                new DefaultEventBroadcastRejectedHandler());
            //线程执行时未捕获异常处理器
            Thread.UncaughtExceptionHandler executeExceptionHandler = this.executeExceptionHandler;

            return new DefaultEventManager(eventHandlerSize, eventHandlerMaxSize, eventQueue, eventBroadcastRejectedHandler, executeExceptionHandler);
        }

    }

    /**
     * {@link DefaultEventManager} 构造器
     * @return {@link EventManagerBuilder}
     */
    public static EventManagerBuilder toBuilder() {
        return new EventManagerBuilder();
    }

    /**
     *
     * @param eventHandlerSize
     * @param eventHandlerMaxSize
     * @param eventQueue
     * @param eventBroadcastRejectedHandler
     * @param executeExceptionHandler
     */
    private DefaultEventManager(int eventHandlerSize, int eventHandlerMaxSize, BlockingQueue<Runnable> eventQueue,
                                AbstractEventBroadcastRejectedHandler eventBroadcastRejectedHandler, Thread.UncaughtExceptionHandler executeExceptionHandler) {
        this.reference = new AtomicReference<>();
        this.executeExceptionHandler = executeExceptionHandler;
        this.eventBroadcastRejectedHandler = eventBroadcastRejectedHandler;
        ExecutorService executorService = reference.get();

        if (executorService == null) {
            synchronized (reference) {
                executorService = reference.get();
                if (executorService == null) {
                    executorService = ExecutorBuilder.create().setWorkQueue(eventQueue).setCorePoolSize(eventHandlerSize).setMaxPoolSize(eventHandlerMaxSize)
                        .setThreadFactory(ThreadUtil.newNamedThreadFactory("event-Multicaster", null, true, this.executeExceptionHandler))
                        .setHandler(this.eventBroadcastRejectedHandler).build();
                    reference.lazySet(executorService);
                }
            }
        }
    }

    /**
     * @see EventManager#broadcastEvent(cn.hutool.event.domain.BaseEvent, boolean)
     */
    @Override
    public void broadcastEvent(final BaseEvent event, boolean sync) {
        for (final BaseListener listener : getAllListeners(event)) {
            dealWithListener(event, listener, sync);
        }
    }

    /**
     * 指定某一监听器处理
     * 该监听器处理后，会被添加至监听器列表
     *
     * @param event
     * @param assignListener
     * @param sync
     */
    @Override
    public void broadcastEvent(BaseEvent event, BaseListener assignListener, boolean sync) {
        dealWithListener(event, assignListener, sync);
    }

    /**
     * 监听器处理事件
     * @param event 待处理事件
     * @param assignListener 指定的监听器
     * @param sync 是否同步
     */
    private void dealWithListener(BaseEvent event, BaseListener assignListener, boolean sync) {
        if (sync) {
            // sync
            try {
                assignListener.onEventHappened(event);
            } catch (Exception e) {
                throw new ListenerExecuteException(e);
            }
        }
        // async
        try {
            getExecutorService().execute(new AnonymityEventHandler(event, assignListener));
        } catch (Exception e) {
            log.error("[Process a async event, failure] event: " + event.getClass().getName() + ":" + event.getId(), e);
        }
    }

    /**
     * 获取任务执行器
     *
     * @return
     */
    public Executor getExecutorService() {
        return reference.get();
    }

    /**
     * 构建
     *
     * @return 被构建的对象
     */
    @Override
    public DefaultEventManager build() {
        return null;
    }

    /**
     * Class DefaultEventManager...
     * 异步事件处理
     * @author Ted.L
     * Created on 2019-09-28
     */
    final class AnonymityEventHandler implements Runnable {
        /** 事件 */
        private BaseEvent    event;

        /** 事件监听器 */
        private BaseListener listener;

        public BaseEvent getEvent() {
            return event;
        }

        /**
         * 构造函数
         *
         * @param event 事件
         * @param listener 事件监听器
         */
        public AnonymityEventHandler(BaseEvent event, BaseListener listener) {
            super();
            this.event = event;
            this.listener = listener;
        }

        /**
         * 事件处理
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                listener.onEventHappened(event);
            } catch (Exception e) {
                log.error("[Process a event, failure] event: " + event.getClass().getName() + ":" + event.getId(), e);
            }
        }
    }
}
