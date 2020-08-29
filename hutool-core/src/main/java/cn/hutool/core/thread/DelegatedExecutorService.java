package cn.hutool.core.thread;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ExecutorService代理
 *
 * @author loolly
 */
public class DelegatedExecutorService extends AbstractExecutorService {
    private final ExecutorService e;

    DelegatedExecutorService(ExecutorService executor) {
        e = executor;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void execute(Runnable command) {
        e.execute(command);
    }

    @Override
    public void shutdown() {
        e.shutdown();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public List<Runnable> shutdownNow() {
        return e.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return e.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return e.isTerminated();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return e.awaitTermination(timeout, unit);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Future<?> submit(Runnable task) {
        return e.submit(task);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public <T> Future<T> submit(Callable<T> task) {
        return e.submit(task);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public <T> Future<T> submit(Runnable task, T result) {
        return e.submit(task, result);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return e.invokeAll(tasks);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        return e.invokeAll(tasks, timeout, unit);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return e.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return e.invokeAny(tasks, timeout, unit);
    }
}
