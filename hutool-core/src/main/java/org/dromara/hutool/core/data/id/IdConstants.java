/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.util.RandomUtil;

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
	 * 默认的节点ID。
	 * 这个方法是静态的，且最终返回的节点ID是基于数据中心ID生成，生成失败则使用随机数
	 */
	public static final long DEFAULT_SEATA_NODE_ID = generateNodeId();

	/**
	 * 默认的Snowflake单例，使用默认的Worker ID和数据中心ID。<br>
	 * 传入{@link #DEFAULT_WORKER_ID}和{@link #DEFAULT_DATACENTER_ID}作为参数。<br>
	 * 此单例对象保证在同一JVM实例中获取ID唯一，唯一性使用进程ID和MAC地址保证。
	 */
	public static final Snowflake DEFAULT_SNOWFLAKE = new Snowflake(DEFAULT_WORKER_ID, DEFAULT_DATACENTER_ID);

	/**
	 * 默认的Seata单例，使用默认的节点ID。<br>
	 * 传入{@link #DEFAULT_SEATA_NODE_ID}作为参数。<br>
	 * 此单例对象保证在同一JVM实例中获取ID唯一，唯一性使用进程ID和MAC地址保证。
	 */
	public static final SeataSnowflake DEFAULT_SEATA_SNOWFLAKE = new SeataSnowflake(DEFAULT_SEATA_NODE_ID);

	/**
	 * 基于网卡MAC地址生成节点ID，失败则使用随机数
	 *
	 * @return nodeId
	 */
	private static long generateNodeId() {
		try {
			return IdUtil.getDataCenterId(SeataSnowflake.MAX_NODE_ID);
		} catch (final Exception e) {
			return RandomUtil.randomLong(SeataSnowflake.MAX_NODE_ID + 1);
		}
	}
}
