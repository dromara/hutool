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

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.Singleton;

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

	private static volatile String localhostName;

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
	public static LinkedHashSet<String> localIps() {
		final LinkedHashSet<InetAddress> localAddressList = NetUtil.localAddressList(
			t -> t instanceof Inet6Address);

		return NetUtil.toIpList(localAddressList);
	}

	/**
	 * 获取主机名称，一次获取会缓存名称<br>
	 * 注意此方法会触发反向DNS解析，导致阻塞，阻塞时间取决于网络！
	 *
	 * @return 主机名称
	 * @since 5.4.4
	 */
	public static String getLocalHostName() {
		if(null == localhostName){
			synchronized (Ipv4Util.class){
				if(null == localhostName){
					localhostName = NetUtil.getAddressName(getLocalhostDirectly());
				}
			}
		}
		return localhostName;
	}

	/**
	 * 获得本机MAC地址，默认使用获取到的IPv6本地地址对应网卡
	 *
	 * @return 本机MAC地址
	 */
	public static String getLocalMacAddress() {
		return MacAddressUtil.getMacAddress(getLocalhost());
	}

	/**
	 * 获得本机物理地址（IPv6网卡）
	 *
	 * @return 本机物理地址
	 */
	public static byte[] getLocalHardwareAddress() {
		return MacAddressUtil.getHardwareAddress(getLocalhost());
	}

	/**
	 * 获取本机网卡IPv6地址，规则如下：
	 *
	 * <ul>
	 *     <li>必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv6地址</li>
	 *     <li>多网卡则返回第一个满足条件的地址</li>
	 *     <li>如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址</li>
	 * </ul>
	 *
	 * <p>
	 * 此方法不会抛出异常，获取失败将返回{@code null}<br>
	 *
	 * @return 本机网卡IP地址，获取失败返回{@code null}
	 */
	public static InetAddress getLocalhost() {
		return Singleton.get(Ipv6Util.class.getName(), Ipv6Util::getLocalhostDirectly);
	}

	/**
	 * 获取本机网卡IPv6地址，不使用缓存，规则如下：
	 *
	 * <ul>
	 *     <li>必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv6地址</li>
	 *     <li>多网卡则返回第一个满足条件的地址</li>
	 *     <li>如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址</li>
	 * </ul>
	 *
	 * <p>
	 * 此方法不会抛出异常，获取失败将返回{@code null}<br>
	 * <p>
	 * 见：https://github.com/dromara/hutool/issues/428
	 *
	 * @return 本机网卡IP地址，获取失败返回{@code null}
	 */
	public static InetAddress getLocalhostDirectly() {
		final LinkedHashSet<InetAddress> localAddressList = NetUtil.localAddressList(address -> {
			// 非loopback地址
			return !address.isLoopbackAddress()
				// 非地区本地地址
				&& !address.isSiteLocalAddress()
				// 需为IPV6地址
				&& address instanceof Inet6Address;
		});

		if (CollUtil.isNotEmpty(localAddressList)) {
			// 如果存在多网卡，返回首个地址
			return CollUtil.getFirst(localAddressList);
		}

		try {
			final InetAddress localHost = InetAddress.getLocalHost();
			if (localHost instanceof Inet6Address) {
				return localHost;
			}
		} catch (final UnknownHostException e) {
			// ignore
		}

		return null;
	}
}
