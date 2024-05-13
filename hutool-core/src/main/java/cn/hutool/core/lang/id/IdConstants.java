package cn.hutool.core.lang.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * ID相关常量
 *
 * @author Looly
 * @since 5.8.28
 */
public class IdConstants {
	/**
	 * 默认的数据中心ID。
	 * <p>此常量通过调用{@link IdUtil#getDataCenterId(long)}方法，传入{@link Snowflake#MAX_DATA_CENTER_ID}作为参数，
	 * 来获取一个默认的数据中心ID。它在系统中作为一个全局配置使用，标识系统默认运行在一个最大数据中心ID限定的环境中。</p>
	 *
	 * @see IdUtil#getDataCenterId(long)
	 * @see Snowflake#MAX_DATA_CENTER_ID
	 */
	public static final long DEFAULT_DATACENTER_ID = IdUtil.getDataCenterId(Snowflake.MAX_DATA_CENTER_ID);

	/**
	 * 默认的Worker ID生成。
	 * <p>这个静态常量是通过调用IdUtil的getWorkerId方法，使用默认的数据中心ID和Snowflake算法允许的最大Worker ID来获取的。</p>
	 *
	 * @see IdUtil#getWorkerId(long, long) 获取Worker ID的具体实现方法
	 * @see Snowflake#MAX_WORKER_ID Snowflake算法中定义的最大Worker ID
	 */
	public static final long DEFAULT_WORKER_ID = IdUtil.getWorkerId(DEFAULT_DATACENTER_ID, Snowflake.MAX_WORKER_ID);

	/**
	 * 默认的Snowflake单例，使用默认的Worker ID和数据中心ID。<br>
	 * 传入{@link #DEFAULT_WORKER_ID}和{@link #DEFAULT_DATACENTER_ID}作为参数。<br>
	 * 此单例对象保证在同一JVM实例中获取ID唯一，唯一性使用进程ID和MAC地址保证。
	 */
	public static final Snowflake DEFAULT_SNOWFLAKE = new Snowflake(DEFAULT_WORKER_ID, DEFAULT_DATACENTER_ID);
}
