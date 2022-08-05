package cn.hutool.core.stream.support;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 无限有序流 的Spliterator
 *
 * @author VampireAchao
 * @since 6.0.0
 */
class IterateSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
    public static <T> IterateSpliterator<T> create(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next) {
        return new IterateSpliterator<>(seed, hasNext, next);
    }

    /**
     * Creates a spliterator reporting the given estimated size and
     * additionalCharacteristics.
     */
    IterateSpliterator(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next) {
        super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE);
        this.seed = seed;
        this.hasNext = hasNext;
        this.next = next;
    }

    private final T seed;
    private final Predicate<? super T> hasNext;
    private final UnaryOperator<T> next;
    private T prev;
    private boolean started;
    private boolean finished;

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (finished) {
            return false;
        }
        T t;
        if (started) {
            t = next.apply(prev);
        } else {
            t = seed;
            started = true;
        }
        if (!hasNext.test(t)) {
            prev = null;
            finished = true;
            return false;
        }
        prev = t;
        action.accept(prev);
        return true;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (finished) {
            return;
        }
        finished = true;
        T t = started ? next.apply(prev) : seed;
        prev = null;
        while (hasNext.test(t)) {
            action.accept(t);
            t = next.apply(t);
        }
    }
}
