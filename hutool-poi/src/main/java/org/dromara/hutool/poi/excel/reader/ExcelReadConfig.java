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

package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.poi.excel.ExcelConfig;

/**
 * Excel读取配置
 *
 * @author Looly
 */
public class ExcelReadConfig extends ExcelConfig {
	/**
	 * 是否忽略空行
	 */
	protected boolean ignoreEmptyRow = true;

	/**
	 * 是否忽略空行
	 * @return 是否忽略空行
	 */
	public boolean isIgnoreEmptyRow() {
		return this.ignoreEmptyRow;
	}

	/**
	 * 设置是否忽略空行
	 *
	 * @param ignoreEmptyRow 是否忽略空行
	 * @return this
	 */
	public ExcelReadConfig setIgnoreEmptyRow(final boolean ignoreEmptyRow) {
		this.ignoreEmptyRow = ignoreEmptyRow;
		return this;
	}
}
