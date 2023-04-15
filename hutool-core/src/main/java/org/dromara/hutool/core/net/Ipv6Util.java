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

package org.dromara.hutool.core.net;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashSet;

/**
 * IPv6工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class Ipv6Util {
	/**
	 * 将IPv6地址字符串转为大整数
	 *
	 * @param ipv6Str 字符串
	 * @return 大整数, 如发生异常返回 null
	 */
	public static BigInteger ipv6ToBigInteger(final String ipv6Str) {
		try {
			final InetAddress address = InetAddress.getByName(ipv6Str);
			if (address instanceof Inet6Address) {
				return new BigInteger(1, address.getAddress());
			}
		} catch (final UnknownHostException ignore) {
		}
		return null;
	}

	/**
	 * 将大整数转换成ipv6字符串
	 *
	 * @param bigInteger 大整数
	 * @return IPv6字符串, 如发生异常返回 null
	 */
	public static String bigIntegerToIPv6(final BigInteger bigInteger) {
		try {
			return InetAddress.getByAddress(
				bigInteger.toByteArray()).toString().substring(1);
		} catch (final UnknownHostException ignore) {
			return null;
		}
	}

	/**
	 * 获得本机的IPv6地址列表<br>
	 * 返回的IP列表有序，按照系统设备顺序
	 *
	 * @return IP地址列表 {@link LinkedHashSet}
	 * @since 4.5.17
	 */
	public static LinkedHashSet<String> localIpv6s() {
		final LinkedHashSet<InetAddress> localAddressList = NetUtil.localAddressList(
			t -> t instanceof Inet6Address);

		return NetUtil.toIpList(localAddressList);
	}
}
