/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text;

/**
 * 常用字符串常量定义
 * @see CharPool
 *
 * @author looly
 * @since 5.6.3
 */
public interface StrPool {
	/**
	 * 字符串常量：制表符 {@code "\t"}
	 */
	String TAB = "	";

	/**
	 * 字符串常量：点 {@code "."}
	 */
	String DOT = ".";

	/**
	 * 字符串常量：双点 {@code ".."} <br>
	 * 用途：作为指向上级文件夹的路径，如：{@code "../path"}
	 */
	String DOUBLE_DOT = "..";

	/**
	 * 字符串常量：斜杠 {@code "/"}
	 */
	String SLASH = "/";

	/**
	 * 字符串常量：反斜杠 {@code "\\"}
	 */
	String BACKSLASH = "\\";

	/**
	 * 字符串常量：回车符 {@code "\r"} <br>
	 * 解释：该字符常用于表示 Linux 系统和 MacOS 系统下的文本换行
	 */
	String CR = "\r";

	/**
	 * 字符串常量：换行符 {@code "\n"}
	 */
	String LF = "\n";

	/**
	 * 字符串常量：Windows 换行 {@code "\r\n"} <br>
	 * 解释：该字符串常用于表示 Windows 系统下的文本换行
	 */
	String CRLF = "\r\n";

	/**
	 * 字符串常量：下划线 {@code "_"}
	 */
	String UNDERLINE = "_";

	/**
	 * 字符串常量：减号（连接符） {@code "-"}
	 */
	String DASHED = "-";

	/**
	 * 字符串常量：逗号 {@code ","}
	 */
	String COMMA = ",";

	/**
	 * 字符串常量：花括号（左） <code>"{"</code>
	 */
	String DELIM_START = "{";

	/**
	 * 字符串常量：花括号（右） <code>"}"</code>
	 */
	String DELIM_END = "}";

	/**
	 * 字符串常量：中括号（左） {@code "["}
	 */
	String BRACKET_START = "[";

	/**
	 * 字符串常量：中括号（右） {@code "]"}
	 */
	String BRACKET_END = "]";

	/**
	 * 字符串常量：冒号 {@code ":"}
	 */
	String COLON = ":";

	/**
	 * 字符串常量：艾特 {@code "@"}
	 */
	String AT = "@";

	/**
	 * 字符串常量：空 JSON {@code "{}"}
	 */
	String EMPTY_JSON = "{}";
}
