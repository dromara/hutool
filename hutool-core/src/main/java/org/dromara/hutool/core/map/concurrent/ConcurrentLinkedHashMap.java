/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package org.dromara.hutool.core.map.concurrent;

import org.dromara.hutool.core.collection.queue.DiscardingQueue;
import org.dromara.hutool.core.collection.queue.Linked;
import org.dromara.hutool.core.collection.queue.LinkedDeque;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.util.RuntimeUtil;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

/**
 * A hash table supporting full concurrency of retrievals, adjustable expected
 * concurrency for updates, and a maximum capacity to bound the map by. This
 * implementation differs from {@link ConcurrentHashMap} in that it maintains a
 * page replacement algorithm that is used to evict an entry when the map has
 * exceeded its capacity. Unlike the {@code Java Collections Framework}, this
 * map does not have a publicly visible constructor and instances are created
 * through a {@link Builder}.
 * <p>
 * An entry is evicted from the map when the {@code weighted capacity} exceeds
 * its {@code maximum weighted capacity} threshold. A {@link EntryWeigher}
 * determines how many units of capacity that an entry consumes. The default
 * weigher assigns each value a selector of {@code 1} to bound the map by the
 * total number of key-value pairs. A map that holds collections may choose to
 * weigh values by the number of elements in the collection and bound the map
 * by the total number of elements that it contains. A change to a value that
 * modifies its selector requires that an update operation is performed on the
 * map.
 * <p>
 * An {@link BiConsumer} may be supplied for notification when an entry
 * is evicted from the map. This listener is invoked on a caller's thread and
 * will not block other threads from operating on the map. An implementation
 * should be aware that the caller's thread will not expect long execution
 * times or failures as a side effect of the listener being notified. Execution
 * safety and a fast turn around time can be achieved by performing the
 * operation asynchronously, such as by submitting a task to an
 * {@link java.util.concurrent.ExecutorService}.
 * <p>
 * The {@code concurrency level} determines the number of threads that can
 * concurrently modify the table. Using a significantly higher or lower value
 * than needed can waste space or lead to thread contention, but an estimate
 * within an order of magnitude of the ideal value does not usually have a
 * noticeable impact. Because placement in hash tables is essentially random,
 * the actual concurrency will vary.
 * <p>
 * This class and its views and iterators implement all of the
 * <em>optional</em> methods of the {@link Map} and {@link Iterator}
 * interfaces.
 * <p>
 * Like {@link Hashtable} but unlike {@link HashMap}, this class
 * does <em>not</em> allow {@code null} to be used as a key or value. Unlike
 * {@link LinkedHashMap}, this class does <em>not</em> provide
 * predictable iteration order. A snapshot of the keys and entries may be
 * obtained in ascending and descending order of retention.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author ben.manes@gmail.com (Ben Manes)
 * @see <a href="http://code.google.com/p/concurrentlinkedhashmap/">
 * http://code.google.com/p/concurrentlinkedhashmap/</a>
 */
public final class ConcurrentLinkedHashMap<K, V> extends AbstractMap<K, V>
	implements ConcurrentMap<K, V>, Serializable {

	/*
	 * This class performs a best-effort bounding of a ConcurrentHashMap using a
	 * page-replacement algorithm to determine which entries to evict when the
	 * capacity is exceeded.
	 *
	 * The page replacement algorithm's data structures are kept eventually
	 * consistent with the map. An update to the map and recording of reads may
	 * not be immediately reflected on the algorithm's data structures. These
	 * structures are guarded by a lock and operations are applied in batches to
	 * avoid lock contention. The penalty of applying the batches is spread across
	 * threads so that the amortized cost is slightly higher than performing just
	 * the ConcurrentHashMap operation.
	 *
	 * A memento of the reads and writes that were performed on the map are
	 * recorded in buffers. These buffers are drained at the first opportunity
	 * after a write or when the read buffer exceeds a threshold size. The reads
	 * are recorded in a lossy buffer, allowing the reordering operations to be
	 * discarded if the draining process cannot keep up. Due to the concurrent
	 * nature of the read and write operations a strict policy ordering is not
	 * possible, but is observably strict when single threaded.
	 *
	 * Due to a lack of a strict ordering guarantee, a task can be executed
	 * out-of-order, such as a removal followed by its addition. The state of the
	 * entry is encoded within the value's selector.
	 *
	 * Alive: The entry is in both the hash-table and the page replacement policy.
	 * This is represented by a positive selector.
	 *
	 * Retired: The entry is not in the hash-table and is pending removal from the
	 * page replacement policy. This is represented by a negative selector.
	 *
	 * Dead: The entry is not in the hash-table and is not in the page replacement
	 * policy. This is represented by a selector of zero.
	 *
	 * The Least Recently Used page replacement algorithm was chosen due to its
	 * simplicity, high hit rate, and ability to be implemented with O(1) time
	 * complexity.
	 */

	/**
	 * The number of CPUs
	 */
	static final int NCPU = RuntimeUtil.getProcessorCount();

	/**
	 * The maximum weighted capacity of the map.
	 */
	static final long MAXIMUM_CAPACITY = Long.MAX_VALUE - Integer.MAX_VALUE;

	/**
	 * The number of read buffers to use.
	 */
	static final int NUMBER_OF_READ_BUFFERS = ceilingNextPowerOfTwo(NCPU);

	/**
	 * Mask value for indexing into the read buffers.
	 */
	static final int READ_BUFFERS_MASK = NUMBER_OF_READ_BUFFERS - 1;

	/**
	 * The number of pending read operations before attempting to drain.
	 */
	static final int READ_BUFFER_THRESHOLD = 32;

	/**
	 * The maximum number of read operations to perform per amortized drain.
	 */
	static final int READ_BUFFER_DRAIN_THRESHOLD = 2 * READ_BUFFER_THRESHOLD;

	/**
	 * The maximum number of pending reads per buffer.
	 */
	static final int READ_BUFFER_SIZE = 2 * READ_BUFFER_DRAIN_THRESHOLD;

	/**
	 * Mask value for indexing into the read buffer.
	 */
	static final int READ_BUFFER_INDEX_MASK = READ_BUFFER_SIZE - 1;

	/**
	 * The maximum number of write operations to perform per amortized drain.
	 */
	static final int WRITE_BUFFER_DRAIN_THRESHOLD = 16;

	@SuppressWarnings("SameParameterValue")
	static int ceilingNextPowerOfTwo(final int x) {
		// From Hacker's Delight, Chapter 3, Harry S. Warren Jr.
		return 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(x - 1));
	}

	// The backing data store holding the key-value associations
	final ConcurrentMap<K, Node<K, V>> data;
	final int concurrencyLevel;

	// These fields provide support to bound the map by a maximum capacity
	final long[] readBufferReadCount;
	final LinkedDeque<Node<K, V>> evictionDeque;

	final AtomicLong weightedSize;
	final AtomicLong capacity;

	final Lock evictionLock;
	final Queue<Runnable> writeBuffer;
	final AtomicLong[] readBufferWriteCount;
	final AtomicLong[] readBufferDrainAtWriteCount;
	final AtomicReference<Node<K, V>>[][] readBuffers;

	final AtomicReference<DrainStatus> drainStatus;
	final EntryWeigher<? super K, ? super V> weigher;

	// These fields provide support for notifying a listener.
	final Queue<Node<K, V>> pendingNotifications;
	final BiConsumer<K, V> listener;

	transient Set<K> keySet;
	transient Collection<V> values;
	transient Set<Entry<K, V>> entrySet;

	/**
	 * Creates an instance based on the builder's configuration.
	 */
	@SuppressWarnings({"unchecked", "cast"})
	private ConcurrentLinkedHashMap(final Builder<K, V> builder) {
		// The data store and its maximum capacity
		concurrencyLevel = builder.concurrencyLevel;
		capacity = new AtomicLong(Math.min(builder.capacity, MAXIMUM_CAPACITY));
		data = new SafeConcurrentHashMap<>(builder.initialCapacity, 0.75f, concurrencyLevel);

		// The eviction support
		weigher = builder.weigher;
		evictionLock = new ReentrantLock();
		weightedSize = new AtomicLong();
		evictionDeque = new LinkedDeque<>();
		writeBuffer = new ConcurrentLinkedQueue<>();
		drainStatus = new AtomicReference<>(DrainStatus.IDLE);

		readBufferReadCount = new long[NUMBER_OF_READ_BUFFERS];
		readBufferWriteCount = new AtomicLong[NUMBER_OF_READ_BUFFERS];
		readBufferDrainAtWriteCount = new AtomicLong[NUMBER_OF_READ_BUFFERS];
		readBuffers = new AtomicReference[NUMBER_OF_READ_BUFFERS][READ_BUFFER_SIZE];
		for (int i = 0; i < NUMBER_OF_READ_BUFFERS; i++) {
			readBufferWriteCount[i] = new AtomicLong();
			readBufferDrainAtWriteCount[i] = new AtomicLong();
			readBuffers[i] = new AtomicReference[READ_BUFFER_SIZE];
			for (int j = 0; j < READ_BUFFER_SIZE; j++) {
				readBuffers[i][j] = new AtomicReference<>();
			}
		}

		// The notification queue and listener
		listener = builder.listener;
		pendingNotifications = (listener == DiscardingListener.INSTANCE)
			? DiscardingQueue.getInstance() : new ConcurrentLinkedQueue<>();
	}

	/* ---------------- Eviction Support -------------- */

	/**
	 * Retrieves the maximum weighted capacity of the map.
	 *
	 * @return the maximum weighted capacity
	 */
	public long capacity() {
		return capacity.get();
	}

	/**
	 * Sets the maximum weighted capacity of the map and eagerly evicts entries
	 * until it shrinks to the appropriate size.
	 *
	 * @param capacity the maximum weighted capacity of the map
	 * @throws IllegalArgumentException if the capacity is negative
	 */
	public void setCapacity(final long capacity) {
		Assert.isTrue(capacity >= 0);
		evictionLock.lock();
		try {
			this.capacity.lazySet(Math.min(capacity, MAXIMUM_CAPACITY));
			drainBuffers();
			evict();
		} finally {
			evictionLock.unlock();
		}
		notifyListener();
	}

	/**
	 * Determines whether the map has exceeded its capacity.
	 */
	boolean hasOverflowed() {
		return weightedSize.get() > capacity.get();
	}

	/**
	 * Evicts entries from the map while it exceeds the capacity and appends
	 * evicted entries to the notification queue for processing.
	 */
	void evict() {
		// Attempts to evict entries from the map if it exceeds the maximum
		// capacity. If the eviction fails due to a concurrent removal of the
		// victim, that removal may cancel out the addition that triggered this
		// eviction. The victim is eagerly unlinked before the removal task so
		// that if an eviction is still required then a new victim will be chosen
		// for removal.
		while (hasOverflowed()) {
			final Node<K, V> node = evictionDeque.poll();

			// If weighted values are used, then the pending operations will adjust
			// the size to reflect the correct selector
			if (node == null) {
				return;
			}

			// Notify the listener only if the entry was evicted
			if (data.remove(node.key, node)) {
				pendingNotifications.add(node);
			}

			makeDead(node);
		}
	}

	/**
	 * Performs the post-processing work required after a read.
	 *
	 * @param node the entry in the page replacement policy
	 */
	void afterRead(final Node<K, V> node) {
		final int bufferIndex = readBufferIndex();
		final long writeCount = recordRead(bufferIndex, node);
		drainOnReadIfNeeded(bufferIndex, writeCount);
		notifyListener();
	}

	/**
	 * Returns the index to the read buffer to record into.
	 */
	static int readBufferIndex() {
		// A buffer is chosen by the thread's id so that tasks are distributed in a
		// pseudo evenly manner. This helps avoid hot entries causing contention
		// due to other threads trying to append to the same buffer.
		return ((int) Thread.currentThread().getId()) & READ_BUFFERS_MASK;
	}

	/**
	 * Records a read in the buffer and return its write count.
	 *
	 * @param bufferIndex the index to the chosen read buffer
	 * @param node        the entry in the page replacement policy
	 * @return the number of writes on the chosen read buffer
	 */
	long recordRead(final int bufferIndex, final Node<K, V> node) {
		// The location in the buffer is chosen in a racy fashion as the increment
		// is not atomic with the insertion. This means that concurrent reads can
		// overlap and overwrite one another, resulting in a lossy buffer.
		final AtomicLong counter = readBufferWriteCount[bufferIndex];
		final long writeCount = counter.get();
		counter.lazySet(writeCount + 1);

		final int index = (int) (writeCount & READ_BUFFER_INDEX_MASK);
		readBuffers[bufferIndex][index].lazySet(node);

		return writeCount;
	}

	/**
	 * Attempts to drain the buffers if it is determined to be needed when
	 * post-processing a read.
	 *
	 * @param bufferIndex the index to the chosen read buffer
	 * @param writeCount  the number of writes on the chosen read buffer
	 */
	void drainOnReadIfNeeded(final int bufferIndex, final long writeCount) {
		final long pending = (writeCount - readBufferDrainAtWriteCount[bufferIndex].get());
		final boolean delayable = (pending < READ_BUFFER_THRESHOLD);
		final DrainStatus status = drainStatus.get();
		if (status.shouldDrainBuffers(delayable)) {
			tryToDrainBuffers();
		}
	}

	/**
	 * Performs the post-processing work required after a write.
	 *
	 * @param task the pending operation to be applied
	 */
	void afterWrite(final Runnable task) {
		writeBuffer.add(task);
		drainStatus.lazySet(DrainStatus.REQUIRED);
		tryToDrainBuffers();
		notifyListener();
	}

	/**
	 * Attempts to acquire the eviction lock and apply the pending operations, up
	 * to the amortized threshold, to the page replacement policy.
	 */
	void tryToDrainBuffers() {
		if (evictionLock.tryLock()) {
			try {
				drainStatus.lazySet(DrainStatus.PROCESSING);
				drainBuffers();
			} finally {
				drainStatus.compareAndSet(DrainStatus.PROCESSING, DrainStatus.IDLE);
				evictionLock.unlock();
			}
		}
	}

	/**
	 * Drains the read and write buffers up to an amortized threshold.
	 */
	void drainBuffers() {
		drainReadBuffers();
		drainWriteBuffer();
	}

	/**
	 * Drains the read buffers, each up to an amortized threshold.
	 */
	void drainReadBuffers() {
		final int start = (int) Thread.currentThread().getId();
		final int end = start + NUMBER_OF_READ_BUFFERS;
		for (int i = start; i < end; i++) {
			drainReadBuffer(i & READ_BUFFERS_MASK);
		}
	}

	/**
	 * Drains the read buffer up to an amortized threshold.
	 */
	void drainReadBuffer(final int bufferIndex) {
		final long writeCount = readBufferWriteCount[bufferIndex].get();
		for (int i = 0; i < READ_BUFFER_DRAIN_THRESHOLD; i++) {
			final int index = (int) (readBufferReadCount[bufferIndex] & READ_BUFFER_INDEX_MASK);
			final AtomicReference<Node<K, V>> slot = readBuffers[bufferIndex][index];
			final Node<K, V> node = slot.get();
			if (node == null) {
				break;
			}

			slot.lazySet(null);
			applyRead(node);
			readBufferReadCount[bufferIndex]++;
		}
		readBufferDrainAtWriteCount[bufferIndex].lazySet(writeCount);
	}

	/**
	 * Updates the node's location in the page replacement policy.
	 */

	void applyRead(final Node<K, V> node) {
		// An entry may be scheduled for reordering despite having been removed.
		// This can occur when the entry was concurrently read while a writer was
		// removing it. If the entry is no longer linked then it does not need to
		// be processed.
		if (evictionDeque.contains(node)) {
			evictionDeque.moveToBack(node);
		}
	}

	/**
	 * Drains the read buffer up to an amortized threshold.
	 */

	void drainWriteBuffer() {
		for (int i = 0; i < WRITE_BUFFER_DRAIN_THRESHOLD; i++) {
			final Runnable task = writeBuffer.poll();
			if (task == null) {
				break;
			}
			task.run();
		}
	}

	/**
	 * Attempts to transition the node from the {@code alive} state to the
	 * {@code retired} state.
	 *
	 * @param node   the entry in the page replacement policy
	 * @param expect the expected weighted value
	 * @return if successful
	 */
	boolean tryToRetire(final Node<K, V> node, final WeightedValue<V> expect) {
		if (expect.isAlive()) {
			final WeightedValue<V> retired = new WeightedValue<>(expect.value, -expect.weight);
			return node.compareAndSet(expect, retired);
		}
		return false;
	}

	/**
	 * Atomically transitions the node from the {@code alive} state to the
	 * {@code retired} state, if a valid transition.
	 *
	 * @param node the entry in the page replacement policy
	 */
	void makeRetired(final Node<K, V> node) {
		for (; ; ) {
			final WeightedValue<V> current = node.get();
			if (!current.isAlive()) {
				return;
			}
			final WeightedValue<V> retired = new WeightedValue<>(current.value, -current.weight);
			if (node.compareAndSet(current, retired)) {
				return;
			}
		}
	}

	/**
	 * Atomically transitions the node to the {@code dead} state and decrements
	 * the {@code weightedSize}.
	 *
	 * @param node the entry in the page replacement policy
	 */

	void makeDead(final Node<K, V> node) {
		for (; ; ) {
			final WeightedValue<V> current = node.get();
			final WeightedValue<V> dead = new WeightedValue<>(current.value, 0);
			if (node.compareAndSet(current, dead)) {
				weightedSize.lazySet(weightedSize.get() - Math.abs(current.weight));
				return;
			}
		}
	}

	/**
	 * Notifies the listener of entries that were evicted.
	 */
	void notifyListener() {
		Node<K, V> node;
		while ((node = pendingNotifications.poll()) != null) {
			listener.accept(node.key, node.getValue());
		}
	}

	/**
	 * Adds the node to the page replacement policy.
	 */
	final class AddTask implements Runnable {
		final Node<K, V> node;
		final int weight;

		AddTask(final Node<K, V> node, final int weight) {
			this.weight = weight;
			this.node = node;
		}

		@Override

		public void run() {
			weightedSize.lazySet(weightedSize.get() + weight);

			// ignore out-of-order write operations
			if (node.get().isAlive()) {
				evictionDeque.add(node);
				evict();
			}
		}
	}

	/**
	 * Removes a node from the page replacement policy.
	 */
	final class RemovalTask implements Runnable {
		final Node<K, V> node;

		RemovalTask(final Node<K, V> node) {
			this.node = node;
		}

		@Override

		public void run() {
			// add may not have been processed yet
			evictionDeque.remove(node);
			makeDead(node);
		}
	}

	/**
	 * Updates the weighted size and evicts an entry on overflow.
	 */
	final class UpdateTask implements Runnable {
		final int weightDifference;
		final Node<K, V> node;

		public UpdateTask(final Node<K, V> node, final int weightDifference) {
			this.weightDifference = weightDifference;
			this.node = node;
		}

		@Override

		public void run() {
			weightedSize.lazySet(weightedSize.get() + weightDifference);
			applyRead(node);
			evict();
		}
	}

	/* ---------------- Concurrent Map Support -------------- */

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public int size() {
		return data.size();
	}

	/**
	 * Returns the weighted size of this map.
	 *
	 * @return the combined selector of the values in this map
	 */
	public long weightedSize() {
		return Math.max(0, weightedSize.get());
	}

	@Override
	public void clear() {
		evictionLock.lock();
		try {
			// Discard all entries
			Node<K, V> node;
			while ((node = evictionDeque.poll()) != null) {
				data.remove(node.key, node);
				makeDead(node);
			}

			// Discard all pending reads
			for (final AtomicReference<Node<K, V>>[] buffer : readBuffers) {
				for (final AtomicReference<Node<K, V>> slot : buffer) {
					slot.lazySet(null);
				}
			}

			// Apply all pending writes
			Runnable task;
			while ((task = writeBuffer.poll()) != null) {
				task.run();
			}
		} finally {
			evictionLock.unlock();
		}
	}

	@Override
	public boolean containsKey(final Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		Assert.notNull(value);

		for (final Node<K, V> node : data.values()) {
			if (node.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public V get(final Object key) {
		final Node<K, V> node = data.get(key);
		if (node == null) {
			return null;
		}
		afterRead(node);
		return node.getValue();
	}

	/**
	 * Returns the value to which the specified key is mapped, or {@code null}
	 * if this map contains no mapping for the key. This method differs from
	 * {@link #get(Object)} in that it does not record the operation with the
	 * page replacement policy.
	 *
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or
	 * {@code null} if this map contains no mapping for the key
	 * @throws NullPointerException if the specified key is null
	 */
	public V getQuietly(final K key) {
		final Node<K, V> node = data.get(key);
		return (node == null) ? null : node.getValue();
	}

	@Override
	public V put(final K key, final V value) {
		return put(key, value, false);
	}

	@Override
	public V putIfAbsent(final K key, final V value) {
		return put(key, value, true);
	}

	/**
	 * Adds a node to the list and the data store. If an existing node is found,
	 * then its value is updated if allowed.
	 *
	 * @param key          key with which the specified value is to be associated
	 * @param value        value to be associated with the specified key
	 * @param onlyIfAbsent a write is performed only if the key is not already
	 *                     associated with a value
	 * @return the prior value in the data store or null if no mapping was found
	 */
	V put(final K key, final V value, final boolean onlyIfAbsent) {
		Assert.notNull(key);
		Assert.notNull(value);

		final int weight = weigher.weightOf(key, value);
		final WeightedValue<V> weightedValue = new WeightedValue<>(value, weight);
		final Node<K, V> node = new Node<>(key, weightedValue);

		for (; ; ) {
			final Node<K, V> prior = data.putIfAbsent(node.key, node);
			if (prior == null) {
				afterWrite(new AddTask(node, weight));
				return null;
			} else if (onlyIfAbsent) {
				afterRead(prior);
				return prior.getValue();
			}
			for (; ; ) {
				final WeightedValue<V> oldWeightedValue = prior.get();
				if (!oldWeightedValue.isAlive()) {
					break;
				}

				if (prior.compareAndSet(oldWeightedValue, weightedValue)) {
					final int weightedDifference = weight - oldWeightedValue.weight;
					if (weightedDifference == 0) {
						afterRead(prior);
					} else {
						afterWrite(new UpdateTask(prior, weightedDifference));
					}
					return oldWeightedValue.value;
				}
			}
		}
	}

	@Override
	public V remove(final Object key) {
		final Node<K, V> node = data.remove(key);
		if (node == null) {
			return null;
		}

		makeRetired(node);
		afterWrite(new RemovalTask(node));
		return node.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(final Object key, final Object value) {
		final Node<K, V> node = data.get((K) key);
		if ((node == null) || (value == null)) {
			return false;
		}

		WeightedValue<V> weightedValue = node.get();
		for (; ; ) {
			if (weightedValue.contains(value)) {
				if (tryToRetire(node, weightedValue)) {
					if (data.remove(key, node)) {
						afterWrite(new RemovalTask(node));
						return true;
					}
				} else {
					weightedValue = node.get();
					if (weightedValue.isAlive()) {
						// retry as an intermediate update may have replaced the value with
						// an equal instance that has a different reference identity
						continue;
					}
				}
			}
			return false;
		}
	}

	@Override
	public V replace(final K key, final V value) {
		Assert.notNull(key);
		Assert.notNull(value);

		final int weight = weigher.weightOf(key, value);
		final WeightedValue<V> weightedValue = new WeightedValue<>(value, weight);

		final Node<K, V> node = data.get(key);
		if (node == null) {
			return null;
		}
		for (; ; ) {
			final WeightedValue<V> oldWeightedValue = node.get();
			if (!oldWeightedValue.isAlive()) {
				return null;
			}
			if (node.compareAndSet(oldWeightedValue, weightedValue)) {
				final int weightedDifference = weight - oldWeightedValue.weight;
				if (weightedDifference == 0) {
					afterRead(node);
				} else {
					afterWrite(new UpdateTask(node, weightedDifference));
				}
				return oldWeightedValue.value;
			}
		}
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		Assert.notNull(key);
		Assert.notNull(oldValue);
		Assert.notNull(newValue);

		final int weight = weigher.weightOf(key, newValue);
		final WeightedValue<V> newWeightedValue = new WeightedValue<>(newValue, weight);

		final Node<K, V> node = data.get(key);
		if (node == null) {
			return false;
		}
		for (; ; ) {
			final WeightedValue<V> weightedValue = node.get();
			if (!weightedValue.isAlive() || !weightedValue.contains(oldValue)) {
				return false;
			}
			if (node.compareAndSet(weightedValue, newWeightedValue)) {
				final int weightedDifference = weight - weightedValue.weight;
				if (weightedDifference == 0) {
					afterRead(node);
				} else {
					afterWrite(new UpdateTask(node, weightedDifference));
				}
				return true;
			}
		}
	}

	@Override
	public Set<K> keySet() {
		final Set<K> ks = keySet;
		return (ks == null) ? (keySet = new KeySet()) : ks;
	}

	/**
	 * Returns a unmodifiable snapshot {@link Set} view of the keys contained in
	 * this map. The set's iterator returns the keys whose order of iteration is
	 * the ascending order in which its entries are considered eligible for
	 * retention, from the least-likely to be retained to the most-likely.
	 * <p>
	 * Beware that, unlike in {@link #keySet()}, obtaining the set is <em>NOT</em>
	 * a constant-time operation. Because of the asynchronous nature of the page
	 * replacement policy, determining the retention ordering requires a traversal
	 * of the keys.
	 *
	 * @return an ascending snapshot view of the keys in this map
	 */
	public Set<K> ascendingKeySet() {
		return ascendingKeySetWithLimit(Integer.MAX_VALUE);
	}

	/**
	 * Returns an unmodifiable snapshot {@link Set} view of the keys contained in
	 * this map. The set's iterator returns the keys whose order of iteration is
	 * the ascending order in which its entries are considered eligible for
	 * retention, from the least-likely to be retained to the most-likely.
	 * <p>
	 * Beware that, unlike in {@link #keySet()}, obtaining the set is <em>NOT</em>
	 * a constant-time operation. Because of the asynchronous nature of the page
	 * replacement policy, determining the retention ordering requires a traversal
	 * of the keys.
	 *
	 * @param limit the maximum size of the returned set
	 * @return a ascending snapshot view of the keys in this map
	 * @throws IllegalArgumentException if the limit is negative
	 */
	public Set<K> ascendingKeySetWithLimit(final int limit) {
		return orderedKeySet(true, limit);
	}

	/**
	 * Returns an unmodifiable snapshot {@link Set} view of the keys contained in
	 * this map. The set's iterator returns the keys whose order of iteration is
	 * the descending order in which its entries are considered eligible for
	 * retention, from the most-likely to be retained to the least-likely.
	 * <p>
	 * Beware that, unlike in {@link #keySet()}, obtaining the set is <em>NOT</em>
	 * a constant-time operation. Because of the asynchronous nature of the page
	 * replacement policy, determining the retention ordering requires a traversal
	 * of the keys.
	 *
	 * @return a descending snapshot view of the keys in this map
	 */
	public Set<K> descendingKeySet() {
		return descendingKeySetWithLimit(Integer.MAX_VALUE);
	}

	/**
	 * Returns an unmodifiable snapshot {@link Set} view of the keys contained in
	 * this map. The set's iterator returns the keys whose order of iteration is
	 * the descending order in which its entries are considered eligible for
	 * retention, from the most-likely to be retained to the least-likely.
	 * <p>
	 * Beware that, unlike in {@link #keySet()}, obtaining the set is <em>NOT</em>
	 * a constant-time operation. Because of the asynchronous nature of the page
	 * replacement policy, determining the retention ordering requires a traversal
	 * of the keys.
	 *
	 * @param limit the maximum size of the returned set
	 * @return a descending snapshot view of the keys in this map
	 * @throws IllegalArgumentException if the limit is negative
	 */
	public Set<K> descendingKeySetWithLimit(final int limit) {
		return orderedKeySet(false, limit);
	}

	Set<K> orderedKeySet(final boolean ascending, final int limit) {
		Assert.isTrue(limit >= 0);
		evictionLock.lock();
		try {
			drainBuffers();

			final int initialCapacity = (weigher == Weighers.entrySingleton())
				? Math.min(limit, (int) weightedSize())
				: 16;
			final Set<K> keys = new LinkedHashSet<>(initialCapacity);
			final Iterator<Node<K, V>> iterator = ascending
				? evictionDeque.iterator()
				: evictionDeque.descendingIterator();
			while (iterator.hasNext() && (limit > keys.size())) {
				keys.add(iterator.next().key);
			}
			return Collections.unmodifiableSet(keys);
		} finally {
			evictionLock.unlock();
		}
	}

	@Override
	public Collection<V> values() {
		final Collection<V> vs = values;
		return (vs == null) ? (values = new Values()) : vs;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		final Set<Entry<K, V>> es = entrySet;
		return (es == null) ? (entrySet = new EntrySet()) : es;
	}

	/**
	 * Returns an unmodifiable snapshot {@link Map} view of the mappings contained
	 * in this map. The map's collections return the mappings whose order of
	 * iteration is the ascending order in which its entries are considered
	 * eligible for retention, from the least-likely to be retained to the
	 * most-likely.
	 * <p>
	 * Beware that obtaining the mappings is <em>NOT</em> a constant-time
	 * operation. Because of the asynchronous nature of the page replacement
	 * policy, determining the retention ordering requires a traversal of the
	 * entries.
	 *
	 * @return a ascending snapshot view of this map
	 */
	public Map<K, V> ascendingMap() {
		return ascendingMapWithLimit(Integer.MAX_VALUE);
	}

	/**
	 * Returns an unmodifiable snapshot {@link Map} view of the mappings contained
	 * in this map. The map's collections return the mappings whose order of
	 * iteration is the ascending order in which its entries are considered
	 * eligible for retention, from the least-likely to be retained to the
	 * most-likely.
	 * <p>
	 * Beware that obtaining the mappings is <em>NOT</em> a constant-time
	 * operation. Because of the asynchronous nature of the page replacement
	 * policy, determining the retention ordering requires a traversal of the
	 * entries.
	 *
	 * @param limit the maximum size of the returned map
	 * @return a ascending snapshot view of this map
	 * @throws IllegalArgumentException if the limit is negative
	 */
	public Map<K, V> ascendingMapWithLimit(final int limit) {
		return orderedMap(true, limit);
	}

	/**
	 * Returns an unmodifiable snapshot {@link Map} view of the mappings contained
	 * in this map. The map's collections return the mappings whose order of
	 * iteration is the descending order in which its entries are considered
	 * eligible for retention, from the most-likely to be retained to the
	 * least-likely.
	 * <p>
	 * Beware that obtaining the mappings is <em>NOT</em> a constant-time
	 * operation. Because of the asynchronous nature of the page replacement
	 * policy, determining the retention ordering requires a traversal of the
	 * entries.
	 *
	 * @return a descending snapshot view of this map
	 */
	public Map<K, V> descendingMap() {
		return descendingMapWithLimit(Integer.MAX_VALUE);
	}

	/**
	 * Returns an unmodifiable snapshot {@link Map} view of the mappings contained
	 * in this map. The map's collections return the mappings whose order of
	 * iteration is the descending order in which its entries are considered
	 * eligible for retention, from the most-likely to be retained to the
	 * least-likely.
	 * <p>
	 * Beware that obtaining the mappings is <em>NOT</em> a constant-time
	 * operation. Because of the asynchronous nature of the page replacement
	 * policy, determining the retention ordering requires a traversal of the
	 * entries.
	 *
	 * @param limit the maximum size of the returned map
	 * @return a descending snapshot view of this map
	 * @throws IllegalArgumentException if the limit is negative
	 */
	public Map<K, V> descendingMapWithLimit(final int limit) {
		return orderedMap(false, limit);
	}

	Map<K, V> orderedMap(final boolean ascending, final int limit) {
		Assert.isTrue(limit >= 0);
		evictionLock.lock();
		try {
			drainBuffers();

			final int initialCapacity = (weigher == Weighers.entrySingleton())
				? Math.min(limit, (int) weightedSize())
				: 16;
			final Map<K, V> map = new LinkedHashMap<>(initialCapacity);
			final Iterator<Node<K, V>> iterator = ascending
				? evictionDeque.iterator()
				: evictionDeque.descendingIterator();
			while (iterator.hasNext() && (limit > map.size())) {
				final Node<K, V> node = iterator.next();
				map.put(node.key, node.getValue());
			}
			return Collections.unmodifiableMap(map);
		} finally {
			evictionLock.unlock();
		}
	}

	/**
	 * The draining status of the buffers.
	 */
	enum DrainStatus {

		/**
		 * A drain is not taking place.
		 */
		IDLE {
			@Override
			boolean shouldDrainBuffers(final boolean delayable) {
				return !delayable;
			}
		},

		/**
		 * A drain is required due to a pending write modification.
		 */
		REQUIRED {
			@Override
			boolean shouldDrainBuffers(final boolean delayable) {
				return true;
			}
		},

		/**
		 * A drain is in progress.
		 */
		PROCESSING {
			@Override
			boolean shouldDrainBuffers(final boolean delayable) {
				return false;
			}
		};

		/**
		 * Determines whether the buffers should be drained.
		 *
		 * @param delayable if a drain should be delayed until required
		 * @return if a drain should be attempted
		 */
		abstract boolean shouldDrainBuffers(boolean delayable);
	}

	/**
	 * A value, its selector, and the entry's status.
	 */
	static final class WeightedValue<V> {
		final int weight;
		final V value;

		WeightedValue(final V value, final int weight) {
			this.weight = weight;
			this.value = value;
		}

		boolean contains(final Object o) {
			return (o == value) || value.equals(o);
		}

		/**
		 * If the entry is available in the hash-table and page replacement policy.
		 */
		boolean isAlive() {
			return weight > 0;
		}

		/**
		 * If the entry was removed from the hash-table and is awaiting removal from
		 * the page replacement policy.
		 */
		boolean isRetired() {
			return weight < 0;
		}

		/**
		 * If the entry was removed from the hash-table and the page replacement
		 * policy.
		 */
		boolean isDead() {
			return weight == 0;
		}
	}

	/**
	 * A node contains the key, the weighted value, and the linkage pointers on
	 * the page-replacement algorithm's data structures.
	 */
	@SuppressWarnings("serial")
	static final class Node<K, V> extends AtomicReference<WeightedValue<V>>
		implements Linked<Node<K, V>> {
		final K key;

		Node<K, V> prev;

		Node<K, V> next;

		/**
		 * Creates a new, unlinked node.
		 */
		Node(final K key, final WeightedValue<V> weightedValue) {
			super(weightedValue);
			this.key = key;
		}

		@Override
		public Node<K, V> getPrevious() {
			return prev;
		}

		@Override
		public void setPrevious(final Node<K, V> prev) {
			this.prev = prev;
		}

		@Override
		public Node<K, V> getNext() {
			return next;
		}

		@Override
		public void setNext(final Node<K, V> next) {
			this.next = next;
		}

		/**
		 * Retrieves the value held by the current {@code WeightedValue}.
		 */
		V getValue() {
			return get().value;
		}
	}

	/**
	 * An adapter to safely externalize the keys.
	 */
	final class KeySet extends AbstractSet<K> {
		final ConcurrentLinkedHashMap<K, V> map = ConcurrentLinkedHashMap.this;

		@Override
		public int size() {
			return map.size();
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator();
		}

		@Override
		public boolean contains(final Object obj) {
			return containsKey(obj);
		}

		@Override
		public boolean remove(final Object obj) {
			return (map.remove(obj) != null);
		}

		@Override
		public Object[] toArray() {
			return map.data.keySet().toArray();
		}

		@Override
		public <T> T[] toArray(final T[] array) {
			return map.data.keySet().toArray(array);
		}
	}

	/**
	 * An adapter to safely externalize the key iterator.
	 */
	final class KeyIterator implements Iterator<K> {
		final Iterator<K> iterator = data.keySet().iterator();
		K current;

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public K next() {
			current = iterator.next();
			return current;
		}

		@Override
		public void remove() {
			Assert.state(current != null);
			ConcurrentLinkedHashMap.this.remove(current);
			current = null;
		}
	}

	/**
	 * An adapter to safely externalize the values.
	 */
	final class Values extends AbstractCollection<V> {

		@Override
		public int size() {
			return ConcurrentLinkedHashMap.this.size();
		}

		@Override
		public void clear() {
			ConcurrentLinkedHashMap.this.clear();
		}

		@Override
		public Iterator<V> iterator() {
			return new ValueIterator();
		}

		@Override
		public boolean contains(final Object o) {
			return containsValue(o);
		}
	}

	/**
	 * An adapter to safely externalize the value iterator.
	 */
	final class ValueIterator implements Iterator<V> {
		final Iterator<Node<K, V>> iterator = data.values().iterator();
		Node<K, V> current;

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public V next() {
			current = iterator.next();
			return current.getValue();
		}

		@Override
		public void remove() {
			Assert.state(current != null);
			ConcurrentLinkedHashMap.this.remove(current.key);
			current = null;
		}
	}

	/**
	 * An adapter to safely externalize the entries.
	 */
	final class EntrySet extends AbstractSet<Entry<K, V>> {
		final ConcurrentLinkedHashMap<K, V> map = ConcurrentLinkedHashMap.this;

		@Override
		public int size() {
			return map.size();
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(final Object obj) {
			if (!(obj instanceof Entry)) {
				return false;
			}
			final Entry<K, Node<K, V>> entry = (Entry<K, Node<K, V>>) obj;
			final Node<K, V> node = map.data.get(entry.getKey());
			return (node != null) && (node.getValue().equals(entry.getValue()));
		}

		@Override
		public boolean add(final Entry<K, V> entry) {
			return (map.putIfAbsent(entry.getKey(), entry.getValue()) == null);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean remove(final Object obj) {
			if (!(obj instanceof Entry<?, ?>)) {
				return false;
			}
			final Entry<K, V> entry = (Entry<K, V>) obj;
			return map.remove(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * An adapter to safely externalize the entry iterator.
	 */
	final class EntryIterator implements Iterator<Entry<K, V>> {
		final Iterator<Node<K, V>> iterator = data.values().iterator();
		Node<K, V> current;

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Entry<K, V> next() {
			current = iterator.next();
			return new WriteThroughEntry(current);
		}

		@Override
		public void remove() {
			Assert.state(current != null);
			ConcurrentLinkedHashMap.this.remove(current.key);
			current = null;
		}
	}

	/**
	 * An entry that allows updates to write through to the map.
	 */
	final class WriteThroughEntry extends SimpleEntry<K, V> {
		private static final long serialVersionUID = 1;

		WriteThroughEntry(final Node<K, V> node) {
			super(node.key, node.getValue());
		}

		@Override
		public V setValue(final V value) {
			put(getKey(), value);
			return super.setValue(value);
		}

		Object writeReplace() {
			return new SimpleEntry<>(this);
		}
	}

	/**
	 * A weigher that enforces that the selector falls within a valid range.
	 */
	static final class BoundedEntryWeigher<K, V> implements EntryWeigher<K, V>, Serializable {
		private static final long serialVersionUID = 1;
		final EntryWeigher<? super K, ? super V> weigher;

		BoundedEntryWeigher(final EntryWeigher<? super K, ? super V> weigher) {
			Assert.notNull(weigher);
			this.weigher = weigher;
		}

		@Override
		public int weightOf(final K key, final V value) {
			final int weight = weigher.weightOf(key, value);
			Assert.isTrue(weight >= 1);
			return weight;
		}

		Object writeReplace() {
			return weigher;
		}
	}

	/**
	 * A listener that ignores all notifications.
	 */
	enum DiscardingListener implements BiConsumer<Object, Object> {
		INSTANCE;

		@Override
		public void accept(final Object key, final Object value) {
		}
	}

	/* ---------------- Serialization Support -------------- */

	private static final long serialVersionUID = 1;

	Object writeReplace() {
		return new SerializationProxy<>(this);
	}

	private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

	/**
	 * A proxy that is serialized instead of the map. The page-replacement
	 * algorithm's data structures are not serialized so the deserialized
	 * instance contains only the entries. This is acceptable as caches hold
	 * transient data that is recomputable and serialization would tend to be
	 * used as a fast warm-up process.
	 */
	static final class SerializationProxy<K, V> implements Serializable {
		final EntryWeigher<? super K, ? super V> weigher;
		final BiConsumer<K, V> listener;
		final int concurrencyLevel;
		final Map<K, V> data;
		final long capacity;

		SerializationProxy(final ConcurrentLinkedHashMap<K, V> map) {
			concurrencyLevel = map.concurrencyLevel;
			data = new HashMap<>(map);
			capacity = map.capacity.get();
			listener = map.listener;
			weigher = map.weigher;
		}

		Object readResolve() {
			final ConcurrentLinkedHashMap<K, V> map = new Builder<K, V>()
				.concurrencyLevel(concurrencyLevel)
				.maximumWeightedCapacity(capacity)
				.listener(listener)
				.weigher(weigher)
				.build();
			map.putAll(data);
			return map;
		}

		private static final long serialVersionUID = 1;
	}

	/* ---------------- Builder -------------- */

	/**
	 * A builder that creates {@link ConcurrentLinkedHashMap} instances. It
	 * provides a flexible approach for constructing customized instances with
	 * a named parameter syntax. It can be used in the following manner:
	 * <pre>{@code
	 * ConcurrentMap<Vertex, Set<Edge>> graph = new Builder<Vertex, Set<Edge>>()
	 *     .maximumWeightedCapacity(5000)
	 *     .weigher(Weighers.<Edge>set())
	 *     .build();
	 * }</pre>
	 */
	public static final class Builder<K, V> {
		static final int DEFAULT_CONCURRENCY_LEVEL = 16;
		static final int DEFAULT_INITIAL_CAPACITY = 16;

		BiConsumer<K, V> listener;
		EntryWeigher<? super K, ? super V> weigher;

		int concurrencyLevel;
		int initialCapacity;
		long capacity;

		/**
		 * 构造
		 */
		@SuppressWarnings("unchecked")
		public Builder() {
			capacity = -1;
			weigher = Weighers.entrySingleton();
			initialCapacity = DEFAULT_INITIAL_CAPACITY;
			concurrencyLevel = DEFAULT_CONCURRENCY_LEVEL;
			listener = (BiConsumer<K, V>) DiscardingListener.INSTANCE;
		}

		/**
		 * Specifies the initial capacity of the hash table (default {@code 16}).
		 * This is the number of key-value pairs that the hash table can hold
		 * before a resize operation is required.
		 *
		 * @param initialCapacity the initial capacity used to size the hash table
		 *                        to accommodate this many entries.
		 * @return this
		 * @throws IllegalArgumentException if the initialCapacity is negative
		 */
		public Builder<K, V> initialCapacity(final int initialCapacity) {
			Assert.isTrue(initialCapacity >= 0);
			this.initialCapacity = initialCapacity;
			return this;
		}

		/**
		 * Specifies the maximum weighted capacity to coerce the map to and may
		 * exceed it temporarily.
		 *
		 * @param capacity the weighted threshold to bound the map by
		 * @return this
		 * @throws IllegalArgumentException if the maximumWeightedCapacity is
		 *                                  negative
		 */
		public Builder<K, V> maximumWeightedCapacity(final long capacity) {
			Assert.isTrue(capacity >= 0);
			this.capacity = capacity;
			return this;
		}

		/**
		 * Specifies the estimated number of concurrently updating threads. The
		 * implementation performs internal sizing to try to accommodate this many
		 * threads (default {@code 16}).
		 *
		 * @param concurrencyLevel the estimated number of concurrently updating
		 *                         threads
		 * @return this
		 * @throws IllegalArgumentException if the concurrencyLevel is less than or
		 *                                  equal to zero
		 */
		public Builder<K, V> concurrencyLevel(final int concurrencyLevel) {
			Assert.isTrue(concurrencyLevel > 0);
			this.concurrencyLevel = concurrencyLevel;
			return this;
		}

		/**
		 * Specifies an optional listener that is registered for notification when
		 * an entry is evicted.
		 *
		 * @param listener the object to forward evicted entries to
		 * @return this
		 * @throws NullPointerException if the listener is null
		 */
		public Builder<K, V> listener(final BiConsumer<K, V> listener) {
			Assert.notNull(listener);
			this.listener = listener;
			return this;
		}

		/**
		 * Specifies an algorithm to determine how many the units of capacity a
		 * value consumes. The default algorithm bounds the map by the number of
		 * key-value pairs by giving each entry a selector of {@code 1}.
		 *
		 * @param weigher the algorithm to determine a value's selector
		 * @return this
		 * @throws NullPointerException if the weigher is null
		 */
		public Builder<K, V> weigher(final Weigher<? super V> weigher) {
			this.weigher = (weigher == Weighers.singleton())
				? Weighers.entrySingleton()
				: new BoundedEntryWeigher<>(Weighers.asEntryWeigher(weigher));
			return this;
		}

		/**
		 * Specifies an algorithm to determine how many the units of capacity an
		 * entry consumes. The default algorithm bounds the map by the number of
		 * key-value pairs by giving each entry a selector of {@code 1}.
		 *
		 * @param weigher the algorithm to determine a entry's selector
		 * @return this
		 * @throws NullPointerException if the weigher is null
		 */
		public Builder<K, V> weigher(final EntryWeigher<? super K, ? super V> weigher) {
			this.weigher = (weigher == Weighers.entrySingleton())
				? Weighers.entrySingleton()
				: new BoundedEntryWeigher<>(weigher);
			return this;
		}

		/**
		 * Creates a new {@link ConcurrentLinkedHashMap} instance.
		 *
		 * @return ConcurrentLinkedHashMap
		 * @throws IllegalStateException if the maximum weighted capacity was
		 *                               not set
		 */
		public ConcurrentLinkedHashMap<K, V> build() {
			Assert.state(capacity >= 0);
			return new ConcurrentLinkedHashMap<>(this);
		}
	}
}
