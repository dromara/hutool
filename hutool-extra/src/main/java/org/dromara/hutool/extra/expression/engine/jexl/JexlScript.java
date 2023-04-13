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

package org.dromara.hutool.extra.expression.engine.jexl;

import org.apache.commons.jexl3.MapContext;
import org.dromara.hutool.core.func.Wrapper;
import org.dromara.hutool.extra.expression.Expression;

import java.util.Map;

/**
 * Jexl3引擎的{@link org.apache.commons.jexl3.JexlScript}包装
 *
 * @author looly
 */
public class JexlScript implements Expression, Wrapper<org.apache.commons.jexl3.JexlScript> {

	private final org.apache.commons.jexl3.JexlScript raw;

	/**
	 * 构造
	 *
	 * @param raw {@link org.apache.commons.jexl3.JexlScript}
	 */
	public JexlScript(final org.apache.commons.jexl3.JexlScript raw) {
		this.raw = raw;
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		final MapContext mapContext = new MapContext(context);
		return raw.execute(mapContext);
	}

	@Override
	public org.apache.commons.jexl3.JexlScript getRaw() {
		return raw;
	}
}
