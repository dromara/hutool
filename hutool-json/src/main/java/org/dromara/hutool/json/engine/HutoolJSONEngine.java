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

package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.json.writer.JSONWriter;

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
		final JSON json = JSONUtil.parse(bean, this.hutoolSJONConfig);
		final JSONWriter jsonWriter = JSONWriter.of(writer,
			ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false) ? 2 : 0,
			0,
			this.hutoolSJONConfig);
		json.write(jsonWriter);
	}

	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		final JSON json = JSONUtil.parse(reader, this.hutoolSJONConfig);
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

		// 自定义配置
		final JSONConfig hutoolSJONConfig = JSONConfig.of();
		if(null != this.config){
			hutoolSJONConfig.setDateFormat(this.config.getDateFormat());
			hutoolSJONConfig.setIgnoreNullValue(this.config.isIgnoreNullValue());
		}

		this.hutoolSJONConfig = hutoolSJONConfig;
	}
}
