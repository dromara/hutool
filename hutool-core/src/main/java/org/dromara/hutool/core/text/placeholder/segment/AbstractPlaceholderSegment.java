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

package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 字符串模板-占位符-抽象 Segment
 * <p>例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public abstract class AbstractPlaceholderSegment implements StrTemplateSegment {
	/**
	 * 占位符变量
	 * <p>例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}</p>
	 */
	private final String placeholder;

	protected AbstractPlaceholderSegment(final String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public String getText() {
		return placeholder;
	}

	/**
	 * 获取占位符
	 *
	 * @return 占位符
	 */
	public String getPlaceholder() {
		return placeholder;
	}
}
