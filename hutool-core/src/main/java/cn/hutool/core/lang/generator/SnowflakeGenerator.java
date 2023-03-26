/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.core.lang.generator;

import cn.hutool.core.lang.id.Snowflake;

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
	public SnowflakeGenerator(final long workerId, final long dataCenterId) {
		snowflake = new Snowflake(workerId, dataCenterId);
	}

	@Override
	public Long next() {
		return this.snowflake.nextId();
	}
}
