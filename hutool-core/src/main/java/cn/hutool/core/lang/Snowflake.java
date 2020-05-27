package cn.hutool.core.lang;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;

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
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
 *
 * @author Looly
 * @since 3.0.1
 */
public class Snowflake implements Serializable {
	private static final long serialVersionUID = 1L;

	private final long twepoch;
	private final long workerIdBits = 5L;
	private final long dataCenterIdBits = 5L;
	//// 最大支持机器节点数0~31，一共32个
	// 最大支持数据中心节点数0~31，一共32个
	@SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	@SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
	private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
	// 序列号12位
	private final long sequenceBits = 12L;
	// 机器节点左移12位
	private final long workerIdShift = sequenceBits;
	// 数据中心节点左移17位
	private final long dataCenterIdShift = sequenceBits + workerIdBits;
	// 时间毫秒数左移22位
	private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
	@SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);// 4095

	private final long workerId;
	private final long dataCenterId;
	private final boolean useSystemClock;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	/**
	 * 构造
	 *
	 * @param workerId     终端ID
	 * @param dataCenterId 数据中心ID
	 */
	public Snowflake(long workerId, long dataCenterId) {
		this(workerId, dataCenterId, false);
	}

	/**
	 * 构造
	 *
	 * @param workerId         终端ID
	 * @param dataCenterId     数据中心ID
	 * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
	 */
	public Snowflake(long workerId, long dataCenterId, boolean isUseSystemClock) {
		this(null, workerId, dataCenterId, isUseSystemClock);
	}

	/**
	 * @param epochDate        初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
	 * @param workerId         工作机器节点id
	 * @param dataCenterId     数据中心id
	 * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
	 * @since 5.1.3
	 */
	public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock) {
		if (null != epochDate) {
			this.twepoch = epochDate.getTime();
		} else{
			// Thu, 04 Nov 2010 01:42:54 GMT
			this.twepoch = 1288834974657L;
		}
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(StrUtil.format("worker Id can't be greater than {} or less than 0", maxWorkerId));
		}
		if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
			throw new IllegalArgumentException(StrUtil.format("datacenter Id can't be greater than {} or less than 0", maxDataCenterId));
		}
		this.workerId = workerId;
		this.dataCenterId = dataCenterId;
		this.useSystemClock = isUseSystemClock;
	}

	/**
	 * 根据Snowflake的ID，获取机器id
	 *
	 * @param id snowflake算法生成的id
	 * @return 所属机器的id
	 */
	public long getWorkerId(long id) {
		return id >> workerIdShift & ~(-1L << workerIdBits);
	}

	/**
	 * 根据Snowflake的ID，获取数据中心id
	 *
	 * @param id snowflake算法生成的id
	 * @return 所属数据中心
	 */
	public long getDataCenterId(long id) {
		return id >> dataCenterIdShift & ~(-1L << dataCenterIdBits);
	}

	/**
	 * 根据Snowflake的ID，获取生成时间
	 *
	 * @param id snowflake算法生成的id
	 * @return 生成的时间
	 */
	public long getGenerateDateTime(long id) {
		return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
	}

	/**
	 * 下一个ID
	 *
	 * @return ID
	 */
	public synchronized long nextId() {
		long timestamp = genTime();
		if (timestamp < lastTimestamp) {
			if(lastTimestamp - timestamp < 2000){
				// 容忍2秒内的回拨，避免NTP校时造成的异常
				timestamp = lastTimestamp;
			} else{
				// 如果服务器时间有问题(时钟后退) 报错。
				throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
			}
		}

		if (timestamp == lastTimestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	/**
	 * 下一个ID（字符串形式）
	 *
	 * @return ID 字符串形式
	 */
	public String nextIdStr() {
		return Long.toString(nextId());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------ Private method start

	/**
	 * 循环等待下一个时间
	 *
	 * @param lastTimestamp 上次记录的时间
	 * @return 下一个时间
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = genTime();
		// 循环直到操作系统时间戳变化
		while (timestamp == lastTimestamp) {
			timestamp = genTime();
		}
		if (timestamp < lastTimestamp) {
			// 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
			throw new IllegalStateException(
					StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
		}
		return timestamp;
	}

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
