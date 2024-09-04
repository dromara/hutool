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

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

/**
 * FastJSON2引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class FastJSON2Engine extends AbstractJSONEngine {

	private JSONReader.Context readerContext;
	private JSONWriter.Context writerContext;

	/**
	 * 构造
	 */
	public FastJSON2Engine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(JSONFactory.class);
	}

	@Override
	public void serialize(final Object bean, final Writer writer) {
		initEngine();
		try (final JSONWriter jsonWriter = JSONWriter.of(this.writerContext)) {
			if (bean == null) {
				jsonWriter.writeNull();
			} else {
				jsonWriter.setRootObject(bean);
				final Class<?> valueClass = bean.getClass();
				final ObjectWriter<?> objectWriter = this.writerContext.getObjectWriter(valueClass, valueClass);
				objectWriter.write(jsonWriter, bean, null, null, 0);
			}

			jsonWriter.flushTo(writer);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		final ObjectReader<T> objectReader = this.readerContext.getObjectReader((Type) type);

		try (final JSONReader jsonReader = JSONReader.of(reader, this.readerContext)) {
			if (jsonReader.isEnd()) {
				return null;
			}

			final T object = objectReader.readObject(jsonReader, (Type) type, null, 0);
			jsonReader.handleResolveTasks(object);
			return object;
		}
	}

	@Override
	protected void reset() {
		this.readerContext = null;
		this.writerContext = null;
	}

	@Override
	protected void initEngine() {
		if(null == this.readerContext){
			this.readerContext = JSONFactory.createReadContext();
			final String dateFormat = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::getDateFormat, "millis");
			this.readerContext.setDateFormat(ObjUtil.defaultIfNull(dateFormat, "millis"));
		}
		if(null == this.writerContext){
			final List<JSONWriter.Feature> features = ListUtil.of();
			if(ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false)){
				features.add(JSONWriter.Feature.PrettyFormat);
			}
			this.writerContext = JSONFactory.createWriteContext(features.toArray(new JSONWriter.Feature[0]));

			// 自定义配置
			final String dateFormat = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::getDateFormat, "millis");
			this.writerContext.setDateFormat(ObjUtil.defaultIfNull(dateFormat, "millis"));
		}
	}
}
