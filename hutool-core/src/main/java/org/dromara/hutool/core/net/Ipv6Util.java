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

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.exception.HutoolException;
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
		// 确保 BigInteger 在 IPv6 地址范围内
		if (bigInteger.compareTo(BigInteger.ZERO) < 0 || bigInteger.compareTo(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16)) > 0) {
			throw new IllegalArgumentException("BigInteger value is out of IPv6 range");
		}

		// 将 BigInteger 转换为 16 字节的字节数组
		byte[] bytes = bigInteger.toByteArray();
		if (bytes.length > 16) {
			// 如果字节数组长度大于 16，去掉前导零
			final int offset = bytes[0] == 0 ? 1 : 0;
			final byte[] newBytes = new byte[16];
			System.arraycopy(bytes, offset, newBytes, 0, 16);
			bytes = newBytes;
		} else if (bytes.length < 16) {
			// 如果字节数组长度小于 16，前面补零
			final byte[] paddedBytes = new byte[16];
			System.arraycopy(bytes, 0, paddedBytes, 16 - bytes.length, bytes.length);
			bytes = paddedBytes;
		}

		// 将字节数组转换为 IPv6 地址字符串
		try {
			return Inet6Address.getByAddress(bytes).getHostAddress();
		} catch (final UnknownHostException e) {
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
		if (null == localhostName) {
			synchronized (Ipv4Util.class) {
				if (null == localhostName) {
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
		final LinkedHashSet<InetAddress> localAddressList = NetUtil.localAddressList(address ->
			// 需为IPV6地址
			address instanceof Inet6Address
				// 非loopback地址，::1
				&& !address.isLoopbackAddress()
				// 非地区本地地址，fec0::/10
				&& !address.isSiteLocalAddress()
				// 非链路本地地址，fe80::/10
				&& !address.isLinkLocalAddress()
		);

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

	/**
	 * 规范IPv6地址，转换scope名称为scope id，如：
	 * <pre>
	 *     fe80:0:0:0:894:aeec:f37d:23e1%en0
	 *                   |
	 *     fe80:0:0:0:894:aeec:f37d:23e1%5
	 * </pre>
	 * <p>
	 * 地址后的“%5” 叫做 scope id.
	 * <p>
	 * 方法来自于Dubbo
	 *
	 * @param address IPv6地址
	 * @return 规范之后的IPv6地址，使用scope id
	 */
	public static InetAddress normalizeV6Address(final Inet6Address address) {
		final String addr = address.getHostAddress();
		final int index = addr.lastIndexOf('%');
		if (index > 0) {
			try {
				return InetAddress.getByName(addr.substring(0, index) + '%' + address.getScopeId());
			} catch (final UnknownHostException e) {
				throw new HutoolException(e);
			}
		}
		return address;
	}
}
