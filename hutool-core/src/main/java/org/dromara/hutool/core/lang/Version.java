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

package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.comparator.CompareUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串版本表示，用于解析版本号的不同部分并比较大小。<br>
 * 来自：java.lang.module.ModuleDescriptor.Version
 *
 * @author Looly
 */
public class Version implements Comparable<Version>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 解析版本字符串为Version对象
	 *
	 * @param v 版本字符串
	 * @return The resulting {@code Version}
	 * @throws IllegalArgumentException 如果 {@code v} 为 {@code null}或 ""或无法解析的字符串，抛出此异常
	 */
	public static Version of(final String v) {
		return new Version(v);
	}

	private final String version;

	private final List<Object> sequence;
	private final List<Object> pre;
	private final List<Object> build;

	/**
	 * 版本对象，格式：tok+ ( '-' tok+)? ( '+' tok+)?，版本之间使用'.'或'-'分隔，版本号可能包含'+'<br>
	 * 数字部分按照大小比较，字符串按照字典顺序比较。
	 *
	 * <ol>
	 *     <li>sequence: 主版本号</li>
	 *     <li>pre: 次版本号</li>
	 *     <li>build: 构建版本</li>
	 * </ol>
	 *
	 * @param v 版本字符串
	 */
	public Version(final String v) {
		Assert.notNull(v, "Null version string");
		final int n = v.length();
		if (n == 0){
			throw new IllegalArgumentException("Empty version string");
		}
		this.version = v;
		this.sequence = new ArrayList<>(4);
		this.pre = new ArrayList<>(2);
		this.build = new ArrayList<>(2);

		int i = 0;
		char c = v.charAt(i);
		// 不检查开头字符为数字，字母按照字典顺序的数字对待

		final List<Object> sequence = this.sequence;
		final List<Object> pre = this.pre;
		final List<Object> build = this.build;

		// 解析主版本
		i = takeNumber(v, i, sequence);

		while (i < n) {
			c = v.charAt(i);
			if (c == '.') {
				i++;
				continue;
			}
			if (c == '-' || c == '+') {
				i++;
				break;
			}
			if (CharUtil.isNumber(c)){
				i = takeNumber(v, i, sequence);
			}else{
				i = takeString(v, i, sequence);
			}
		}

		if (c == '-' && i >= n){
			return;
		}

		// 解析次版本
		while (i < n) {
			c = v.charAt(i);
			if (c >= '0' && c <= '9')
				i = takeNumber(v, i, pre);
			else
				i = takeString(v, i, pre);
			if (i >= n){
				break;
			}
			c = v.charAt(i);
			if (c == '.' || c == '-') {
				i++;
				continue;
			}
			if (c == '+') {
				i++;
				break;
			}
		}

		if (c == '+' && i >= n){
			return;
		}

		// 解析build版本
		while (i < n) {
			c = v.charAt(i);
			if (c >= '0' && c <= '9') {
				i = takeNumber(v, i, build);
			}else {
				i = takeString(v, i, build);
			}
			if (i >= n){
				break;
			}
			c = v.charAt(i);
			if (c == '.' || c == '-' || c == '+') {
				i++;
			}
		}
	}

	@Override
	public int compareTo(final Version that) {
		int c = compareTokens(this.sequence, that.sequence);
		if (c != 0) {
			return c;
		}
		if (this.pre.isEmpty()) {
			if (!that.pre.isEmpty()) {
				return +1;
			}
		} else {
			if (that.pre.isEmpty()) {
				return -1;
			}
		}
		c = compareTokens(this.pre, that.pre);
		if (c != 0) {
			return c;
		}
		return compareTokens(this.build, that.build);
	}

	@Override
	public boolean equals(final Object ob) {
		if (!(ob instanceof Version)){
			return false;
		}
		return compareTo((Version) ob) == 0;
	}

	@Override
	public int hashCode() {
		return version.hashCode();
	}

	@Override
	public String toString() {
		return version;
	}

	// region ----- private methods
	/**
	 * 获取字符串中从位置i开始的数字，并加入到acc中<br>
	 * 如 a123b，则从1开始，解析到acc中为[1, 2, 3]
	 *
	 * @param s 字符串
	 * @param i 位置
	 * @param acc 数字列表
	 * @return 结束位置（不包含）
	 */
	private static int takeNumber(final String s, int i, final List<Object> acc) {
		char c = s.charAt(i);
		int d = (c - '0');
		final int n = s.length();
		while (++i < n) {
			c = s.charAt(i);
			if (CharUtil.isNumber(c)) {
				d = d * 10 + (c - '0');
				continue;
			}
			break;
		}
		acc.add(d);
		return i;
	}

	// Take a string token starting at position i
	// Append it to the given list
	// Return the index of the first character not taken
	// Requires: s.charAt(i) is not '.'
	//

	/**
	 * 获取字符串中从位置i开始的字符串，并加入到acc中<br>
	 * 字符串结束的位置为'.'、'-'、'+'和数字
	 *
	 * @param s 版本字符串
	 * @param i 开始位置
	 * @param acc 字符串列表
	 * @return 结束位置（不包含）
	 */
	private static int takeString(final String s, int i, final List<Object> acc) {
		final int b = i;
		final int n = s.length();
		while (++i < n) {
			final char c = s.charAt(i);
			if (c != '.' && c != '-' && c != '+' && !(c >= '0' && c <= '9')){
				continue;
			}
			break;
		}
		acc.add(s.substring(b, i));
		return i;
	}

	/**
	 * 比较节点
	 * @param ts1 节点1
	 * @param ts2 节点2
	 * @return 比较结果
	 */
	private int compareTokens(final List<Object> ts1, final List<Object> ts2) {
		final int n = Math.min(ts1.size(), ts2.size());
		for (int i = 0; i < n; i++) {
			final Object o1 = ts1.get(i);
			final Object o2 = ts2.get(i);
			if ((o1 instanceof Integer && o2 instanceof Integer)
				|| (o1 instanceof String && o2 instanceof String)) {
				final int c = CompareUtil.compare(o1, o2, null);
				if (c == 0){
					continue;
				}
				return c;
			}
			// Types differ, so convert number to string form
			final int c = o1.toString().compareTo(o2.toString());
			if (c == 0){
				continue;
			}
			return c;
		}
		final List<Object> rest = ts1.size() > ts2.size() ? ts1 : ts2;
		final int e = rest.size();
		for (int i = n; i < e; i++) {
			final Object o = rest.get(i);
			if (o instanceof Integer && ((Integer) o) == 0){
				continue;
			}
			return ts1.size() - ts2.size();
		}
		return 0;
	}
	// endregion
}
