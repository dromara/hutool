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

package org.dromara.hutool.json.engine;

import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONUtil;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Hutool自身实现的JSON引擎
 *
 * @author Looly
 * @since 6.0.0
 */
public class HutoolJSONEngine implements JSONEngine {
	@Override
	public void serialize(final Object bean, final Writer writer) {
		final JSON json = (JSON) JSONUtil.parse(bean);
		json.write(writer);
	}

	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		final JSON json = (JSON) JSONUtil.parse(reader);
		return json.toBean((Type) type);
	}
}
