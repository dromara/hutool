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

package org.dromara.hutool.core.lang.id;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.RandomUtil;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB ID生成策略实现<br>
 * ObjectId由以下几部分组成：
 *
 * <pre>
 * 1. Time 时间戳。
 * 2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
 * 3. 随机数
 * 4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
 * </pre>
 *
 * <table summary="" border="1">
 *     <tr>
 *         <td>时间戳</td>
 *         <td>随机数</td>
 *         <td>自增计数器</td>
 *     </tr>
 *     <tr>
 *         <td>4</td>
 *         <td>4</td>
 *         <td>4</td>
 *     </tr>
 * </table>
 * <p>
 * 参考：<a href="https://github.com/mongodb/mongo-java-driver/blob/master/bson/src/main/org/bson/types/ObjectId.java">...</a>
 *
 * @author looly
 * @since 4.0.0
 */
public class ObjectId {
	/**
	 * 16进制字符
	 */
	private static final char[] HEX_UNIT = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/**
	 * 线程安全的下一个随机数,每次生成自增+1
	 */
	private static final AtomicInteger NEXT_INC = new AtomicInteger(RandomUtil.randomInt());
	/**
	 * 机器信息
	 */
	private static final char[] MACHINE_CODE = initMachineCode();

	/**
	 * 给定的字符串是否为有效的ObjectId
	 *
	 * @param s 字符串
	 * @return 是否为有效的ObjectId
	 */
	public static boolean isValid(String s) {
		if (s == null) {
			return false;
		}
		s = StrUtil.removeAll(s, "-");
		final int len = s.length();
		if (len != 24) {
			return false;
		}

		char c;
		for (int i = 0; i < len; i++) {
			c = s.charAt(i);
			if (c >= '0' && c <= '9') {
				continue;
			}
			if (c >= 'a' && c <= 'f') {
				continue;
			}
			if (c >= 'A' && c <= 'F') {
				continue;
			}
			return false;
		}
		return true;
	}

	/**
	 * 获取一个objectId的bytes表现形式
	 *
	 * @return objectId
	 * @since 4.1.15
	 */
	public static byte[] nextBytes() {
		return next().getBytes();
	}

	/**
	 * 获取一个objectId【没有下划线】。
	 *
	 * @return objectId
	 */
	public static String next() {
		final char[] ids = new char[24];
		int epoch = (int) ((System.currentTimeMillis() / 1000));
		// 4位字节 ： 时间戳
		for (int i = 7; i >= 0; i--) {
			ids[i] = HEX_UNIT[(epoch & 15)];
			epoch >>>= 4;
		}
		// 4位字节 ： 随机数
		System.arraycopy(MACHINE_CODE, 0, ids, 8, 8);
		// 4位字节： 自增序列。溢出后，相当于从0开始算。
		int seq = NEXT_INC.incrementAndGet();
		for (int i = 23; i >= 16; i--) {
			ids[i] = HEX_UNIT[(seq & 15)];
			seq >>>= 4;
		}
		return new String(ids);
	}

	/**
	 * 获取一个objectId
	 *
	 * @param withHyphen 是否包含分隔符
	 * @return objectId
	 */
	public static String next(final boolean withHyphen) {
		if (! withHyphen) {
			return next();
		}
		final char[] ids = new char[26];
		ids[8] = '-';
		ids[17] = '-';
		int epoch = (int) ((System.currentTimeMillis() / 1000));
		// 4位字节 ： 时间戳
		for (int i = 7; i >= 0; i--) {
			ids[i] = HEX_UNIT[(epoch & 15)];
			epoch >>>= 4;
		}
		// 4位字节 ： 随机数
		System.arraycopy(MACHINE_CODE, 0, ids, 9, 8);
		// 4位字节： 自增序列。溢出后，相当于从0开始算。
		int seq = NEXT_INC.incrementAndGet();
		for (int i = 25; i >= 18; i--) {
			ids[i] = HEX_UNIT[(seq & 15)];
			seq >>>= 4;
		}
		return new String(ids);
	}

	/**
	 * 初始化机器码
	 *
	 * @return 机器码
	 */
	private static char[] initMachineCode() {
		// 机器码 : 4位随机数，8个字节。避免docker容器中生成相同机器码的bug
		final char[] macAndPid = new char[8];
		final Random random = new Random();
		for (int i = 7; i >= 0; i--) {
			macAndPid[i] = HEX_UNIT[random.nextInt() & 15];
		}
		return macAndPid;
	}
}
