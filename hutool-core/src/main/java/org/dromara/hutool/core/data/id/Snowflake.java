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

package org.dromara.hutool.core.data.id;

import org.dromara.hutool.core.date.SystemClock;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.generator.Generator;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 符号位（1bit）- 时间戳相对值（41bit）- 数据中心标志（5bit）- 机器标志（5bit）- 递增序号（12bit）
 * (0) - (0000000000 0000000000 0000000000 0000000000 0) - (00000) - (00000) - (000000000000)
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html<br>
 * 关于长度是18还是19的问题见：https://blog.csdn.net/unifirst/article/details/80408050
 *
 * @author Looly
 * @since 3.0.1
 */
public class Snowflake implements Generator<Long>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认的起始时间，为Thu, 04 Nov 2010 01:42:54 GMT
	 */
	public static final long DEFAULT_TWEPOCH = 1288834974657L;
	private static final long WORKER_ID_BITS = 5L;
	// 最大支持机器节点数0~31，一共32个
	private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
	private static final long DATA_CENTER_ID_BITS = 5L;
	// 最大支持数据中心节点数0~31，一共32个
	private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
	// 序列号12位（表示只允许序号的范围为：0-4095）
	private static final long SEQUENCE_BITS = 12L;
	// 机器节点左移12位
	private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
	// 数据中心节点左移17位
	private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
	// 时间毫秒数左移22位
	private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
	// 序列掩码，用于限定序列最大值不能超过4095
	private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);// 4095

	/**
	 * 初始化时间点
	 */
	private final long twepoch;
	private final long workerId;
	private final long dataCenterId;
	private final boolean useSystemClock;
	/**
	 * 当在低频模式下时，序号始终为0，导致生成ID始终为偶数<br>
	 * 此属性用于限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题。<br>
	 * 注意次数必须小于{@link #SEQUENCE_MASK}，{@code 0}表示不使用随机数。<br>
	 * 这个上限不包括值本身。
	 */
	private final long randomSequenceLimit;

	/**
	 * 自增序号，当高频模式下时，同一毫秒内生成N个ID，则这个序号在同一毫秒下，自增以避免ID重复。
	 */
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	/**
	 * 构造，使用自动生成的工作节点ID和数据中心ID
	 */
	public Snowflake() {
		this(IdUtil.getWorkerId(IdUtil.getDataCenterId(MAX_DATA_CENTER_ID), MAX_WORKER_ID));
	}

	/**
	 * 构造
	 *
	 * @param workerId 终端ID
	 */
	public Snowflake(final long workerId) {
		this(workerId, IdUtil.getDataCenterId(MAX_DATA_CENTER_ID));
	}

	/**
	 * 构造
	 *
	 * @param workerId     终端ID
	 * @param dataCenterId 数据中心ID
	 */
	public Snowflake(final long workerId, final long dataCenterId) {
		this(workerId, dataCenterId, false);
	}

	/**
	 * 构造
	 *
	 * @param workerId         终端ID
	 * @param dataCenterId     数据中心ID
	 * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
	 */
	public Snowflake(final long workerId, final long dataCenterId, final boolean isUseSystemClock) {
		this(null, workerId, dataCenterId, isUseSystemClock);
	}

	/**
	 * @param epochDate        初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
	 * @param workerId         工作机器节点id
	 * @param dataCenterId     数据中心id
	 * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
	 * @since 5.1.3
	 */
	public Snowflake(final Date epochDate, final long workerId, final long dataCenterId,
					 final boolean isUseSystemClock) {
		this(epochDate, workerId, dataCenterId, isUseSystemClock, 0);
	}

	/**
	 * @param epochDate           初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
	 * @param workerId            工作机器节点id
	 * @param dataCenterId        数据中心id
	 * @param isUseSystemClock    是否使用{@link SystemClock} 获取当前时间戳
	 * @param randomSequenceLimit 限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题，0表示无随机，上限不包括值本身。
	 * @since 5.8.0
	 */
	public Snowflake(final Date epochDate, final long workerId, final long dataCenterId,
					 final boolean isUseSystemClock, final long randomSequenceLimit) {
		this.twepoch = (null != epochDate) ? epochDate.getTime() : DEFAULT_TWEPOCH;
		this.workerId = Assert.checkBetween(workerId, 0, MAX_WORKER_ID);
		this.dataCenterId = Assert.checkBetween(dataCenterId, 0, MAX_DATA_CENTER_ID);
		this.useSystemClock = isUseSystemClock;
		this.randomSequenceLimit = Assert.checkBetween(randomSequenceLimit, 0, SEQUENCE_MASK);
	}

	/**
	 * 根据Snowflake的ID，获取机器id
	 *
	 * @param id snowflake算法生成的id
	 * @return 所属机器的id
	 */
	public long getWorkerId(final long id) {
		return id >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS);
	}

	/**
	 * 根据Snowflake的ID，获取数据中心id
	 *
	 * @param id snowflake算法生成的id
	 * @return 所属数据中心
	 */
	public long getDataCenterId(final long id) {
		return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
	}

	/**
	 * 根据Snowflake的ID，获取生成时间
	 *
	 * @param id snowflake算法生成的id
	 * @return 生成的时间
	 */
	public long getGenerateDateTime(final long id) {
		return (id >> TIMESTAMP_LEFT_SHIFT & ~(-1L << 41L)) + twepoch;
	}

	/**
	 * 下一个ID
	 *
	 * @return ID
	 */
	@Override
	public synchronized Long next() {
		long timestamp = genTime();
		if (timestamp < this.lastTimestamp) {
			timestamp = lastTimestamp;
		}

		if (timestamp == this.lastTimestamp) {
			// 时间出现回拨，递增序号
			final long sequence = (this.sequence + 1) & SEQUENCE_MASK;
			if (sequence == 0) {
				// 如果序号耗尽，不再等待时间追赶，而是采用“超前消费”方式，让时间递增。
				// 这样可以避免系统暂停等待，而选择在“未来”ID新增不快时追上来。
				timestamp += 1;
			}
			this.sequence = sequence;
		} else {
			// issue#I51EJY，通过随机数避免低频生成ID序号始终为0的问题
			sequence = randomSequenceLimit > 1 ? RandomUtil.randomLong(randomSequenceLimit) : 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT)
				| (dataCenterId << DATA_CENTER_ID_SHIFT)
				| (workerId << WORKER_ID_SHIFT)
				| sequence;
	}

	/**
	 * 下一个ID（字符串形式）
	 *
	 * @return ID 字符串形式
	 */
	public String nextStr() {
		return Long.toString(next());
	}

	/**
	 * 根据传入时间戳-计算ID起终点
	 *
	 * @param timestampStart 开始时间戳
	 * @param timestampEnd   结束时间戳
	 * @return key-ID起点，Value-ID终点
	 * @since 5.8.23
	 */
	public Pair<Long, Long> getIdScopeByTimestamp(final long timestampStart, final long timestampEnd) {
		return getIdScopeByTimestamp(timestampStart, timestampEnd, true);
	}

	/**
	 * 根据传入时间戳-计算ID起终点 Gitee/issues/I60M14
	 *
	 * @param timestampStart        开始时间戳
	 * @param timestampEnd          结束时间戳
	 * @param ignoreCenterAndWorker 是否忽略数据中心和机器节点的占位，忽略后可获得分布式环境全局可信赖的起终点。
	 * @return key-ID起点，Value-ID终点
	 * @since 5.8.23
	 */
	public Pair<Long, Long> getIdScopeByTimestamp(final long timestampStart, final long timestampEnd, final boolean ignoreCenterAndWorker) {
		final long startTimeMinId = (timestampStart - twepoch) << TIMESTAMP_LEFT_SHIFT;
		final long endTimeMinId = (timestampEnd - twepoch) << TIMESTAMP_LEFT_SHIFT;
		if (ignoreCenterAndWorker) {
			final long endId = endTimeMinId | ~(-1 << TIMESTAMP_LEFT_SHIFT);
			return Pair.of(startTimeMinId, endId);
		} else {
			final long startId = startTimeMinId | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT);
			final long endId = endTimeMinId | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | SEQUENCE_MASK;
			return Pair.of(startId, endId);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------ Private method start
	/**
	 * 生成时间戳
	 *
	 * @return 时间戳
	 */
	private long genTime() {
		return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
	}
	// ------------------------------------------------------------------------------------------------------------------------------------ Private method end
}
