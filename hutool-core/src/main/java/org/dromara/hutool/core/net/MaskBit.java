/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.net;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.BiMap;

import java.util.HashMap;

/**
 * 掩码位和掩码之间的Map对应
 *
 * @since 5.4.1
 * @author looly
 */
public class MaskBit {

	/**
	 * 掩码位与掩码的点分十进制的双向对应关系
	 */
	private static final BiMap<Integer, String> MASK_BIT_MAP;
	static {
		MASK_BIT_MAP = new BiMap<>(new HashMap<>(32));
		MASK_BIT_MAP.put(1, "128.0.0.0");
		MASK_BIT_MAP.put(2, "192.0.0.0");
		MASK_BIT_MAP.put(3, "224.0.0.0");
		MASK_BIT_MAP.put(4, "240.0.0.0");
		MASK_BIT_MAP.put(5, "248.0.0.0");
		MASK_BIT_MAP.put(6, "252.0.0.0");
		MASK_BIT_MAP.put(7, "254.0.0.0");
		MASK_BIT_MAP.put(8, "255.0.0.0");
		MASK_BIT_MAP.put(9, "255.128.0.0");
		MASK_BIT_MAP.put(10, "255.192.0.0");
		MASK_BIT_MAP.put(11, "255.224.0.0");
		MASK_BIT_MAP.put(12, "255.240.0.0");
		MASK_BIT_MAP.put(13, "255.248.0.0");
		MASK_BIT_MAP.put(14, "255.252.0.0");
		MASK_BIT_MAP.put(15, "255.254.0.0");
		MASK_BIT_MAP.put(16, "255.255.0.0");
		MASK_BIT_MAP.put(17, "255.255.128.0");
		MASK_BIT_MAP.put(18, "255.255.192.0");
		MASK_BIT_MAP.put(19, "255.255.224.0");
		MASK_BIT_MAP.put(20, "255.255.240.0");
		MASK_BIT_MAP.put(21, "255.255.248.0");
		MASK_BIT_MAP.put(22, "255.255.252.0");
		MASK_BIT_MAP.put(23, "255.255.254.0");
		MASK_BIT_MAP.put(24, "255.255.255.0");
		MASK_BIT_MAP.put(25, "255.255.255.128");
		MASK_BIT_MAP.put(26, "255.255.255.192");
		MASK_BIT_MAP.put(27, "255.255.255.224");
		MASK_BIT_MAP.put(28, "255.255.255.240");
		MASK_BIT_MAP.put(29, "255.255.255.248");
		MASK_BIT_MAP.put(30, "255.255.255.252");
		MASK_BIT_MAP.put(31, "255.255.255.254");
		MASK_BIT_MAP.put(32, "255.255.255.255");
	}

	/**
	 * 根据掩码位获取掩码
	 *
	 * @param maskBit 掩码位
	 * @return 掩码
	 */
	public static String get(final int maskBit) {
		return MASK_BIT_MAP.get(maskBit);
	}

	/**
	 * 根据掩码获取掩码位
	 *
	 * @param mask 掩码的点分十进制表示，如 255.255.255.0
	 *
	 * @return 掩码位，如 24；如果掩码不合法，则返回null
	 * @since 5.6.5
	 */
	public static Integer getMaskBit(final String mask) {
		return MASK_BIT_MAP.getKey(mask);
	}

	/**
	 * 根据掩码位获取掩码IP(Long型)
	 *
	 * @param maskBit 掩码位
	 * @return 掩码IP(Long型)
	 * @since 6.0.0
	 */
	public static long getMaskIpLong(final int maskBit) {
		Assert.isTrue(MASK_BIT_MAP.containsKey(maskBit), "非法的掩码位数：{}", maskBit);
		return (Ipv4Pool.IPV4_NUM_MAX << (Ipv4Pool.IPV4_MASK_BIT_MAX - maskBit)) & Ipv4Pool.IPV4_NUM_MAX;
	}
}
