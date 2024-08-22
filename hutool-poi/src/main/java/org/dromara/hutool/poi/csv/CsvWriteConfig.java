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

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.text.CharUtil;

import java.io.Serializable;

/**
 * CSV写出配置项
 *
 * @author looly
 */
public class CsvWriteConfig extends CsvConfig<CsvWriteConfig> implements Serializable {
	private static final long serialVersionUID = 5396453565371560052L;

	/**
	 * 是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 */
	protected boolean alwaysDelimitText;
	/**
	 * 换行符
	 */
	protected char[] lineDelimiter = {CharUtil.CR, CharUtil.LF};
	/**
	 * 是否使用安全模式，对可能存在DDE攻击的内容进行替换
	 */
	protected boolean ddeSafe;

	/**
	 * 文件末尾是否添加换行符<br>
	 * 按照https://datatracker.ietf.org/doc/html/rfc4180#section-2 规范，末尾换行符可有可无。
	 */
	protected boolean endingLineBreak;

	/**
	 * 默认配置
	 *
	 * @return 默认配置
	 */
	public static CsvWriteConfig defaultConfig() {
		return new CsvWriteConfig();
	}

	/**
	 * 设置是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 *
	 * @param alwaysDelimitText 是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 * @return this
	 */
	public CsvWriteConfig setAlwaysDelimitText(final boolean alwaysDelimitText) {
		this.alwaysDelimitText = alwaysDelimitText;
		return this;
	}

	/**
	 * 设置换行符
	 *
	 * @param lineDelimiter 换行符
	 * @return this
	 */
	public CsvWriteConfig setLineDelimiter(final char[] lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
		return this;
	}

	/**
	 * 设置是否动态数据交换安全，使用文本包装符包裹可能存在DDE攻击的内容<br>
	 * 见：https://blog.csdn.net/weixin_41924764/article/details/108665746
	 *
	 * @param ddeSafe dde安全
	 * @return this
	 */
	public CsvWriteConfig setDdeSafe(final boolean ddeSafe) {
		this.ddeSafe = ddeSafe;
		return this;
	}

	/**
	 * 文件末尾是否添加换行符<br>
	 * 按照https://datatracker.ietf.org/doc/html/rfc4180#section-2 规范，末尾换行符可有可无。
	 *
	 * @param endingLineBreak 文件末尾是否添加换行符
	 * @return this
	 */
	public CsvWriteConfig setEndingLineBreak(final boolean endingLineBreak) {
		this.endingLineBreak = endingLineBreak;
		return this;
	}
}
