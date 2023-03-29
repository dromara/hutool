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

package cn.hutool.extra.mail;

import cn.hutool.core.array.ArrayUtil;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 邮件内部工具类
 * @author looly
 * @since 3.2.3
 */
public class InternalMailUtil {

	/**
	 * 将多个字符串邮件地址转为{@link InternetAddress}列表<br>
	 * 单个字符串地址可以是多个地址合并的字符串
	 *
	 * @param addrStrs 地址数组
	 * @param charset 编码（主要用于中文用户名的编码）
	 * @return 地址数组
	 * @since 4.0.3
	 */
	public static InternetAddress[] parseAddressFromStrs(final String[] addrStrs, final Charset charset) {
		final List<InternetAddress> resultList = new ArrayList<>(addrStrs.length);
		InternetAddress[] addrs;
		for (final String addrStr : addrStrs) {
			addrs = parseAddress(addrStr, charset);
			if (ArrayUtil.isNotEmpty(addrs)) {
				Collections.addAll(resultList, addrs);
			}
		}
		return resultList.toArray(new InternetAddress[0]);
	}

	/**
	 * 解析第一个地址
	 *
	 * @param address 地址字符串
	 * @param charset 编码，{@code null}表示使用系统属性定义的编码或系统编码
	 * @return 地址列表
	 */
	public static InternetAddress parseFirstAddress(final String address, final Charset charset) {
		final InternetAddress[] internetAddresses = parseAddress(address, charset);
		if (ArrayUtil.isEmpty(internetAddresses)) {
			try {
				return new InternetAddress(address);
			} catch (final AddressException e) {
				throw new MailException(e);
			}
		}
		return internetAddresses[0];
	}

	/**
	 * 将一个地址字符串解析为多个地址<br>
	 * 地址间使用" "、","、";"分隔
	 *
	 * @param address 地址字符串
	 * @param charset 编码，{@code null}表示使用系统属性定义的编码或系统编码
	 * @return 地址列表
	 */
	public static InternetAddress[] parseAddress(final String address, final Charset charset) {
		final InternetAddress[] addresses;
		try {
			addresses = InternetAddress.parse(address);
		} catch (final AddressException e) {
			throw new MailException(e);
		}
		//编码用户名
		if (ArrayUtil.isNotEmpty(addresses)) {
			final String charsetStr = null == charset ? null : charset.name();
			for (final InternetAddress internetAddress : addresses) {
				try {
					internetAddress.setPersonal(internetAddress.getPersonal(), charsetStr);
				} catch (final UnsupportedEncodingException e) {
					throw new MailException(e);
				}
			}
		}

		return addresses;
	}

	/**
	 * 编码中文字符<br>
	 * 编码失败返回原字符串
	 *
	 * @param text 被编码的文本
	 * @param charset 编码
	 * @return 编码后的结果
	 */
	public static String encodeText(final String text, final Charset charset) {
		try {
			return MimeUtility.encodeText(text, charset.name(), null);
		} catch (final UnsupportedEncodingException e) {
			// ignore
		}
		return text;
	}
}
