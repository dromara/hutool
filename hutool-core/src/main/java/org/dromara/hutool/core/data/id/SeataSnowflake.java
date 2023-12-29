/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.lang.generator.Generator;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Seata改进的雪花算法ID<br>
 * 改进主要是更换了节点和时间戳的位置，以实现在单节点中单调递增
 * 来自：https://github.com/seata/seata/blob/2.x/common/src/main/java/io/seata/common/util/IdWorker.java
 * 相关说明见：
 * <ul>
 *     <li>https://zhuanlan.zhihu.com/p/648460337</li>
 *     <li>http://seata.io/zh-cn/blog/seata-snowflake-explain.html</li>
 * </ul>
 *
 * <pre>
 * 符号位（1bit） - 节点标志ID（10bit）- 时间戳相对值（41bit） - 递增序号（12bit）
 * (0) - (0000000000) - (0000000000 0000000000 0000000000 0000000000 0) - (000000000000)
 * </pre>
 *
 * @author funkye，selfishlover
 */
public class SeataSnowflake implements Generator<Long>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认的起始时间，为2020-05-03
	 */
	public static final long DEFAULT_TWEPOCH = 1588435200000L;

	// 节点ID长度
	private static final int NODE_ID_BITS = 10;
	// 节点ID的最大值，1023
	private final int MAX_NODE_ID = ~(-1 << NODE_ID_BITS);
	// 时间戳长度
	private static final int TIMESTAMP_BITS = 41;
	// 序列号12位（表示只允许序号的范围为：0-4095）
	private static final int SEQUENCE_BITS = 12;
	// 时间戳+序号的最大值
	private static final long timestampAndSequenceMask = ~(-1L << (TIMESTAMP_BITS + SEQUENCE_BITS));

	private long nodeId;
	private final AtomicLong timestampAndSequence;

	/**
	 * 构造
	 */
	public SeataSnowflake(){
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param nodeId    节点ID
	 */
	public SeataSnowflake(final Long nodeId){
		this(null, nodeId);
	}

	/**
	 * 构造
	 *
	 * @param epochDate 初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
	 * @param nodeId    节点ID
	 */
	public SeataSnowflake(final Date epochDate, final Long nodeId) {
		final long twepoch = (null == epochDate) ? DEFAULT_TWEPOCH : epochDate.getTime();
		final long timestampWithSequence = (System.currentTimeMillis() - twepoch) << SEQUENCE_BITS;
		this.timestampAndSequence = new AtomicLong(timestampWithSequence);

		initNodeId(nodeId);
	}

	/**
	 * 获取下一个雪花ID
	 *
	 * @return id
	 */
	@Override
	public Long next() {
		final long next = timestampAndSequence.incrementAndGet();
		final long timestampWithSequence = next & timestampAndSequenceMask;
		return nodeId | timestampWithSequence;
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
	 * 初始化节点ID
	 *
	 * @param nodeId 节点ID
	 */
	private void initNodeId(Long nodeId) {
		if (nodeId == null) {
			nodeId = generateNodeId();
		}
		if (nodeId > MAX_NODE_ID || nodeId < 0) {
			final String message = StrUtil.format("worker Id can't be greater than {} or less than 0", MAX_NODE_ID);
			throw new IllegalArgumentException(message);
		}
		this.nodeId = nodeId << (TIMESTAMP_BITS + SEQUENCE_BITS);
	}

	/**
	 * 基于网卡MAC地址生成节点ID，失败则使用随机数
	 *
	 * @return workerId
	 */
	private long generateNodeId() {
		try {
			return IdUtil.getDataCenterId(MAX_NODE_ID);
		} catch (final Exception e) {
			return RandomUtil.randomLong(MAX_NODE_ID + 1);
		}
	}
}
