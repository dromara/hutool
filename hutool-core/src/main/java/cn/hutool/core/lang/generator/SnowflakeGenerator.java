package cn.hutool.core.lang.generator;

import cn.hutool.core.lang.Snowflake;

/**
 * Snowflake生成器<br>
 * 注意，默认此生成器必须单例使用，否则会有重复<br>
 * 默认构造的终端ID和数据中心ID都为0，不适用于分布式环境。
 *
 * @author looly
 * @since 5.4.3
 */
public class SnowflakeGenerator implements Generator<Long> {

	private final Snowflake snowflake;

	/**
	 * 构造
	 */
	public SnowflakeGenerator() {
		this(0, 0);
	}

	/**
	 * 构造
	 *
	 * @param workerId     终端ID
	 * @param dataCenterId 数据中心ID
	 */
	public SnowflakeGenerator(long workerId, long dataCenterId) {
		snowflake = new Snowflake(workerId, dataCenterId);
	}

	@Override
	public Long next() {
		return this.snowflake.nextId();
	}
}
