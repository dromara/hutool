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

import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
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
public class HutoolJSONEngine extends AbstractJSONEngine {

	private JSONConfig hutoolSJONConfig;

	@Override
	public void serialize(final Object bean, final Writer writer) {
		initEngine();
		final JSON json = (JSON) JSONUtil.parse(bean, this.hutoolSJONConfig);
		json.write(writer, ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false) ? 4 : 0, 0, null);
	}

	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		final JSON json = (JSON) JSONUtil.parse(reader, this.hutoolSJONConfig);
		return json.toBean((Type) type);
	}

	@Override
	protected void reset() {
		hutoolSJONConfig = null;
	}

	@Override
	protected void initEngine() {
		if(null != hutoolSJONConfig){
			return;
		}

		hutoolSJONConfig = JSONConfig.of();
	}
}
