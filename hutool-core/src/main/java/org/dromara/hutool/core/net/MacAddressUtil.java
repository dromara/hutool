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

import org.dromara.hutool.core.exception.HutoolException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * MAC地址（硬件地址）相关工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class MacAddressUtil {
	/**
	 * 获得指定地址信息中的MAC地址，使用分隔符“-”
	 *
	 * @param inetAddress {@link InetAddress}
	 * @return MAC地址，用-分隔
	 */
	public static String getMacAddress(final InetAddress inetAddress) {
		return getMacAddress(inetAddress, "-");
	}

	/**
	 * 获得指定地址信息中的MAC地址
	 *
	 * @param inetAddress {@link InetAddress}
	 * @param separator   分隔符，推荐使用“-”或者“:”
	 * @return MAC地址，用-分隔
	 */
	public static String getMacAddress(final InetAddress inetAddress, final String separator) {
		if (null == inetAddress) {
			return null;
		}

		return toMacAddressStr(getHardwareAddress(inetAddress), separator);
	}

	/**
	 * 获得指定地址信息中的硬件地址（MAC地址）
	 *
	 * @param inetAddress {@link InetAddress}
	 * @return 硬件地址
	 * @since 5.7.3
	 */
	public static byte[] getHardwareAddress(final InetAddress inetAddress) {
		if (null == inetAddress) {
			return null;
		}

		try {
			// 获取地址对应网卡
			final NetworkInterface networkInterface =
				NetworkInterface.getByInetAddress(inetAddress);
			if (null != networkInterface) {
				return networkInterface.getHardwareAddress();
			}
		} catch (final SocketException e) {
			throw new HutoolException(e);
		}
		return null;
	}

	/**
	 * 将bytes类型的mac地址转换为可读字符串，通常地址每个byte位使用16进制表示，并用指定分隔符分隔
	 *
	 * @param mac       MAC地址（网卡硬件地址）
	 * @param separator 分隔符
	 * @return MAC地址字符串
	 */
	public static String toMacAddressStr(final byte[] mac, final String separator) {
		if (null == mac) {
			return null;
		}

		final StringBuilder sb = new StringBuilder(
			// 字符串长度 = byte个数*2（每个byte转16进制后占2位） + 分隔符总长度
			mac.length * 2 + (mac.length - 1) * separator.length());
		String s;
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append(separator);
			}
			// 字节转换为整数
			s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		return sb.toString();
	}
}
