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

package cn.hutool.crypto.digest;

import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.SecureUtil;

import java.security.MessageDigest;

/**
 * {@link Digester}创建简单工厂，用于生产{@link Digester}对象<br>
 * 参考Guava方式，工厂负责持有一个原始的{@link MessageDigest}对象，使用时优先通过clone方式创建对象，提高初始化性能。
 *
 * @author looly
 */
public class DigesterFactory {

	/**
	 * 创建工厂
	 *
	 * @param algorithm 算法
	 * @return DigesterFactory
	 */
	public static DigesterFactory ofJdk(final String algorithm) {
		return of(SecureUtil.createJdkMessageDigest(algorithm));
	}

	/**
	 * 创建工厂，使用{@link GlobalBouncyCastleProvider}找到的提供方。
	 *
	 * @param algorithm 算法
	 * @return DigesterFactory
	 */
	public static DigesterFactory of(final String algorithm) {
		return of(SecureUtil.createMessageDigest(algorithm));
	}

	/**
	 * 创建工厂
	 *
	 * @param messageDigest {@link MessageDigest}，可以通过{@link SecureUtil#createMessageDigest(String)} 创建
	 * @return DigesterFactory
	 */
	public static DigesterFactory of(final MessageDigest messageDigest) {
		return new DigesterFactory(messageDigest);
	}

	private final MessageDigest prototype;
	private final boolean cloneSupport;

	/**
	 * 构造
	 *
	 * @param messageDigest {@link MessageDigest}模板
	 */
	private DigesterFactory(final MessageDigest messageDigest) {
		this.prototype = messageDigest;
		this.cloneSupport = checkCloneSupport(messageDigest);
	}

	/**
	 * 创建{@link Digester}
	 *
	 * @return {@link Digester}
	 */
	public Digester createDigester() {
		return new Digester(createMessageDigester());
	}

	/**
	 * 创建{@link MessageDigest}
	 *
	 * @return {@link MessageDigest}
	 */
	public MessageDigest createMessageDigester() {
		if (cloneSupport) {
			try {
				return (MessageDigest) prototype.clone();
			} catch (final CloneNotSupportedException ignore) {
				// ignore
			}
		}
		return SecureUtil.createJdkMessageDigest(prototype.getAlgorithm());
	}

	/**
	 * 检查{@link MessageDigest}对象是否支持clone方法
	 *
	 * @param messageDigest {@link MessageDigest}
	 * @return 是否支持clone方法
	 */
	private static boolean checkCloneSupport(final MessageDigest messageDigest) {
		try {
			messageDigest.clone();
			return true;
		} catch (final CloneNotSupportedException e) {
			return false;
		}
	}
}
