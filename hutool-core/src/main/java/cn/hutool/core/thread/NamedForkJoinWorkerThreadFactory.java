package cn.hutool.core.thread;


import cn.hutool.core.util.StrUtil;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;

/**
 * ForkJoinThread创建工厂类，此工厂可选配置：
 *
 * <pre>
 * 1. 自定义线程命名前缀
 * 2. 自定义是否守护线程
 * </pre>
 *
 * @author luozongle
 * @since 2023/01/26
 */
public class NamedForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {

    /**
     * 线程前缀
     */
    private final String prefix;

    /**
     * 是否是守护线程
     */
    private final boolean isDaemon;

    /**
     * 线程编号
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public NamedForkJoinWorkerThreadFactory(String prefix) {
        this(prefix, true);
    }

    public NamedForkJoinWorkerThreadFactory(String prefix, boolean isDaemon) {
        this.prefix = StrUtil.blankToDefault(prefix, "Hutool-ForkJoinPool");
        this.isDaemon = isDaemon;
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        ForkJoinWorkerThread thread = defaultForkJoinWorkerThreadFactory.newThread(pool);
        thread.setDaemon(isDaemon);
        thread.setName(prefix + "-" + threadNumber.getAndIncrement());
        return thread;
    }

    public static NamedForkJoinWorkerThreadFactory create(String prefix) {
        return new NamedForkJoinWorkerThreadFactory(prefix);
    }

    public static NamedForkJoinWorkerThreadFactory create(String prefix, boolean isDaemon) {
        return new NamedForkJoinWorkerThreadFactory(prefix, isDaemon);
    }
}
