package cn.hutool.event;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
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
public final class DefaultEventManager extends AbstractEventManager {
    /** logger */
    private static final Log                        log = LogFactory.get();

    private final AtomicReference<ExecutorService>  reference;

    /** 异常处理 */
    private Thread.UncaughtExceptionHandler         executeExceptionHandler;

    /** 事件拒绝分发异常 */
    private AbstractEventMulticasterRejectedHandler eventMulticasterRejectedHandler;

    /**
     * 无参构造
     */
    public DefaultEventManager() {
        reference = new AtomicReference<>();
    }

    /**
     * 设置线程池及线程执行异常处理器的构造
     * @param executorService 线程池
     * @param executeExceptionHandler 线程执行异常处理器
     */
    public DefaultEventManager(ExecutorService executorService, Thread.UncaughtExceptionHandler executeExceptionHandler) {
        this.reference = new AtomicReference<>();
        this.reference.lazySet(executorService);
        this.executeExceptionHandler = executeExceptionHandler;
    }

    /**
     * 设置线程池、线程执行异常处理、拒绝提交事件构造
     * @param executorService 线程池
     * @param executeExceptionHandler 线程执行异常处理器
     * @param eventMulticasterRejectedHandler 事件派发拒绝处理器
     */
    public DefaultEventManager(ExecutorService executorService, Thread.UncaughtExceptionHandler executeExceptionHandler,
                               AbstractEventMulticasterRejectedHandler eventMulticasterRejectedHandler) {
        this.reference = new AtomicReference<>();
        this.reference.lazySet(executorService);
        this.executeExceptionHandler = executeExceptionHandler;
        this.eventMulticasterRejectedHandler = eventMulticasterRejectedHandler;
    }

    /**
     * 设置 线程执行异常处理、拒绝提交事件构造
     * @param executeExceptionHandler 线程执行异常处理器
     * @param eventMulticasterRejectedHandler 事件派发拒绝处理器
     */
    public DefaultEventManager(Thread.UncaughtExceptionHandler executeExceptionHandler, AbstractEventMulticasterRejectedHandler eventMulticasterRejectedHandler) {
        this.reference = new AtomicReference<>();
        this.executeExceptionHandler = executeExceptionHandler;
        this.eventMulticasterRejectedHandler = eventMulticasterRejectedHandler;
    }

    /**
     * 设置 拒绝提交事件构造
     * @param eventMulticasterRejectedHandler 事件派发拒绝处理器
     */
    public DefaultEventManager(AbstractEventMulticasterRejectedHandler eventMulticasterRejectedHandler) {
        this.reference = new AtomicReference<>();
        this.eventMulticasterRejectedHandler = eventMulticasterRejectedHandler;
    }

    /**
     * 设置 线程执行异常处理器 的构造器
     * @param executeExceptionHandler 线程执行异常处理器
     */
    public DefaultEventManager(Thread.UncaughtExceptionHandler executeExceptionHandler) {
        this.reference = new AtomicReference<>();
        this.executeExceptionHandler = executeExceptionHandler;
    }

    /**
     * @see EventManager#broadcastEvent(cn.hutool.event.domain.BaseEvent, boolean)
     */
    @Override
    public void broadcastEvent(final BaseEvent event, boolean sync) {
        for (final BaseListener listener : getAllListeners(event)) {
            if (sync) {
                // sync
                try {
                    listener.onEventHappened(event);
                } catch (Exception e) {
                    throw new ListenerExecuteException(e);
                }
                continue;
            }
            // async
            try {
                getExecutorService().execute(new AnonymityEventHandler(event, listener));
            } catch (Exception e) {
                log.error("[Process a async event, failure] event: " + event.getClass().getName() + ":" + event.getId(), e);
            }
        }
    }

    /**
     * 获取任务执行器
     *
     * @return
     */
    public Executor getExecutorService() {
        ExecutorService executorService = reference.get();

        if (executorService == null) {
            synchronized (reference) {
                executorService = reference.get();
                if (executorService == null) {
                    executorService = ExecutorBuilder.create()
                            .setWorkQueue(new LinkedBlockingQueue<Runnable>(getDefaultQueueSize()))
                            .setCorePoolSize(getDefaultCoreSize())
                            .setMaxPoolSize(getDefaultMaxSize())
                            .setThreadFactory(ThreadUtil.newNamedThreadFactory("event-Multicaster", null, true, executeExceptionHandler))
                            .setHandler(eventMulticasterRejectedHandler).build();
                    reference.lazySet(executorService);
                }
            }
        }

        return executorService;
    }

    /**
     * 默认每2MB内存一个size,最小1024
     * @return  默认队列size
     */
    private int getDefaultQueueSize() {
        int minVal = 1024;
        long maxMemory = Runtime.getRuntime().maxMemory() / (2<<20);
        maxMemory--;
        maxMemory |= maxMemory >> 1;
        maxMemory |= maxMemory >> 2;
        maxMemory |= maxMemory >> 4;
        maxMemory |= maxMemory >> 8;
        maxMemory |= maxMemory >> 16;
        maxMemory++;

        return (int) (maxMemory < minVal ? minVal : maxMemory);
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
