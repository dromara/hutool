package cn.hutool.core.lang;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;

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
 * 
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * 
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
 * 
 * @author Looly
 * @since 3.0.1
 */
public class Snowflake {

	// Thu, 04 Nov 2010 01:42:54 GMT
	private final long twepoch = 1288834974657L;
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	//// 最大支持机器节点数0~31，一共32个
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	// 最大支持数据中心节点数0~31，一共32个
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	// 序列号12位
	private final long sequenceBits = 12L;
	// 机器节点左移12位
	private final long workerIdShift = sequenceBits;
	// 数据中心节点左移17位
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	// 时间毫秒数左移22位
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);// 4095

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;
	private boolean useSystemClock;

	/**
	 * 构造
	 * 
	 * @param workerId 终端ID
	 * @param datacenterId 数据中心ID
	 */
	public Snowflake(long workerId, long datacenterId) {
		this(workerId, datacenterId, false);
	}

	/**
	 * 构造
	 * 
	 * @param workerId 终端ID
	 * @param datacenterId 数据中心ID
	 * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
	 */
	public Snowflake(long workerId, long datacenterId, boolean isUseSystemClock) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(StrUtil.format("worker Id can't be greater than {} or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(StrUtil.format("datacenter Id can't be greater than {} or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
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
		return id >> datacenterIdShift & ~(-1L << datacenterIdBits);
	}

	/**
	 *根据Snowflake的ID，获取生成时间
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
			// 如果服务器时间有问题(时钟后退) 报错。
			throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
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
		while (timestamp <= lastTimestamp) {
			timestamp = genTime();
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
