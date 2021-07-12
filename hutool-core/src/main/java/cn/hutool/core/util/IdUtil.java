package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.net.NetUtil;

/**
 * ID生成器工具类，此工具类中主要封装：
 *
 * <pre>
 * 1. 唯一性ID生成器：UUID、ObjectId（MongoDB）、Snowflake
 * </pre>
 *
 * <p>
 * ID相关文章见：http://calvin1978.blogcn.com/articles/uuid.html
 *
 * @author looly
 * @since 4.1.13
 */
public class IdUtil {

	// ------------------------------------------------------------------- UUID

	/**
	 * 获取随机UUID
	 *
	 * @return 随机UUID
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 简化的UUID，去掉了横线
	 *
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID() {
		return UUID.randomUUID().toString(true);
	}

	/**
	 * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
	 *
	 * @return 随机UUID
	 * @since 4.1.19
	 */
	public static String fastUUID() {
		return UUID.fastUUID().toString();
	}

	/**
	 * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
	 *
	 * @return 简化的UUID，去掉了横线
	 * @since 4.1.19
	 */
	public static String fastSimpleUUID() {
		return UUID.fastUUID().toString(true);
	}

	/**
	 * 创建MongoDB ID生成策略实现<br>
	 * ObjectId由以下几部分组成：
	 *
	 * <pre>
	 * 1. Time 时间戳。
	 * 2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
	 * 3. PID 进程ID。确保同一机器中不冲突
	 * 4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
	 * </pre>
	 * <p>
	 * 参考：http://blog.csdn.net/qxc1281/article/details/54021882
	 *
	 * @return ObjectId
	 */
	public static String objectId() {
		return ObjectId.next();
	}

	/**
	 * 创建Twitter的Snowflake 算法生成器。
	 * <p>
	 * 特别注意：此方法调用后会创建独立的{@link Snowflake}对象，每个独立的对象ID不互斥，会导致ID重复，请自行保证单例！
	 * </p>
	 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
	 *
	 * <p>
	 * snowflake的结构如下(每部分用-分开):<br>
	 *
	 * <pre>
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
	 * </pre>
	 * <p>
	 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
	 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
	 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
	 *
	 * <p>
	 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
	 *
	 * @param workerId     终端ID
	 * @param datacenterId 数据中心ID
	 * @return {@link Snowflake}
	 * @deprecated 此方法容易产生起义：多个Snowflake实例产生的ID会产生重复，此对象在单台机器上必须单例！
	 */
	@Deprecated
	public static Snowflake createSnowflake(long workerId, long datacenterId) {
		return new Snowflake(workerId, datacenterId);
	}

	/**
	 * 获取单例的Twitter的Snowflake 算法生成器对象<br>
	 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
	 *
	 * <p>
	 * snowflake的结构如下(每部分用-分开):<br>
	 *
	 * <pre>
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
	 * </pre>
	 * <p>
	 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
	 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
	 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
	 *
	 * <p>
	 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
	 *
	 * @param workerId     终端ID
	 * @param datacenterId 数据中心ID
	 * @return {@link Snowflake}
	 * @since 4.5.9
	 */
	public static Snowflake getSnowflake(long workerId, long datacenterId) {
		return Singleton.get(Snowflake.class, workerId, datacenterId);
	}

	/**
	 * 获取单例的Twitter的Snowflake 算法生成器对象<br>
	 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
	 *
	 * <p>
	 * snowflake的结构如下(每部分用-分开):<br>
	 *
	 * <pre>
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
	 * </pre>
	 * <p>
	 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
	 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
	 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
	 *
	 * <p>
	 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
	 *
	 * @param workerId     终端ID
	 * @return {@link Snowflake}
	 * @since 5.7.3
	 */
	public static Snowflake getSnowflake(long workerId) {
		return Singleton.get(Snowflake.class, workerId);
	}

	/**
	 * 获取单例的Twitter的Snowflake 算法生成器对象<br>
	 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
	 *
	 * <p>
	 * snowflake的结构如下(每部分用-分开):<br>
	 *
	 * <pre>
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
	 * </pre>
	 * <p>
	 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
	 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
	 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
	 *
	 * <p>
	 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
	 *
	 * @return {@link Snowflake}
	 * @since 5.7.3
	 */
	public static Snowflake getSnowflake() {
		return Singleton.get(Snowflake.class);
	}

	/**
	 * 获取数据中心ID<br>
	 * 数据中心ID依赖于本地网卡MAC地址。
	 * <p>
	 * 此算法来自于mybatis-plus#Sequence
	 * </p>
	 *
	 * @param maxDatacenterId 最大的中心ID
	 * @return 数据中心ID
	 * @since 5.7.3
	 */
	public static long getDataCenterId(long maxDatacenterId) {
		long id = 1L;
		final byte[] mac = NetUtil.getLocalHardwareAddress();
		if (null != mac) {
			id = ((0x000000FF & (long) mac[mac.length - 2])
					| (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
			id = id % (maxDatacenterId + 1);
		}

		return id;
	}

	/**
	 * 获取机器ID，使用进程ID配合数据中心ID生成<br>
	 * 机器依赖于本进程ID或进程名的Hash值。
	 *
	 * <p>
	 * 此算法来自于mybatis-plus#Sequence
	 * </p>
	 *
	 * @param datacenterId 数据中心ID
	 * @param maxWorkerId  最大的机器节点ID
	 * @return ID
	 * @since 5.7.3
	 */
	public static long getWorkerId(long datacenterId, long maxWorkerId) {
		final StringBuilder mpid = new StringBuilder();
		mpid.append(datacenterId);
		try {
			mpid.append(RuntimeUtil.getPid());
		} catch (UtilException igonre) {
			//ignore
		}
		/*
		 * MAC + PID 的 hashcode 获取16个低位
		 */
		return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
	}

	// ------------------------------------------------------------------- NanoId
	/**
	 * 获取随机NanoId
	 *
	 * @return 随机NanoId
	 * @since 5.7.5
	 */
	public static String nanoId() {
		return NanoId.randomNanoId();
	}

	/**
	 * 获取随机NanoId
	 *
	 * @param size ID中的字符数量
	 * @return 随机NanoId
	 * @since 5.7.5
	 */
	public static String nanoId(int size){
		return NanoId.randomNanoId(size);
	}
}
