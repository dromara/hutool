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

package org.dromara.hutool.text.finder;

/**
 * 字符串查找接口，通过调用{@link #start(int)}查找开始位置，再调用{@link #end(int)}找结束位置
 *
 * @author looly
 * @since 5.7.14
 */
public interface Finder {

	/**
	 * 未找到的的位置表示，用-1表示
	 */
	int INDEX_NOT_FOUND = -1;

	/**
	 * 返回开始位置，即起始字符位置（包含），未找到返回-1
	 *
	 * @param from 查找的开始位置（包含）
	 * @return 起始字符位置，未找到返回-1
	 */
	int start(int from);

	/**
	 * 返回结束位置，即最后一个字符后的位置（不包含）
	 *
	 * @param start 找到的起始位置
	 * @return 结束位置，未找到返回-1
	 */
	int end(int start);

	/**
	 * 复位查找器，用于重用对象
	 *
	 * @return this
	 */
	default Finder reset() {
		return this;
	}
}
