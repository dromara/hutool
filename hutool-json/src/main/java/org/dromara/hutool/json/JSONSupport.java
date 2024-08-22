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

package org.dromara.hutool.json;

import org.dromara.hutool.core.bean.copier.BeanCopier;
import org.dromara.hutool.json.serialize.JSONDeserializer;
import org.dromara.hutool.json.serialize.JSONStringer;

/**
 * JSON支持<br>
 * 继承此类实现实体类与JSON的相互转换
 *
 * @author Looly
 */
public class JSONSupport implements JSONStringer, JSONDeserializer<Object> {

	/**
	 * JSON String转Bean
	 *
	 * @param jsonString JSON String
	 */
	public void deserialize(final String jsonString) {
		deserialize(new JSONObject(jsonString));
	}

	/**
	 * JSON转Bean
	 *
	 * @param json JSON
	 */
	@Override
	public Object deserialize(final JSON json) {
		BeanCopier.of(json,
				this, this.getClass(),
				InternalJSONUtil.toCopyOptions(json.config())).copy();
		return this;
	}

	/**
	 * @return JSON对象
	 */
	public JSONObject toJSON() {
		return new JSONObject(this);
	}

	@Override
	public String toJSONString() {
		return toJSON().toString();
	}

	/**
	 * 美化的JSON（使用回车缩进显示JSON），用于打印输出debug
	 *
	 * @return 美化的JSON
	 */
	public String toPrettyString() {
		return toJSON().toStringPretty();
	}

	@Override
	public String toString() {
		return toJSONString();
	}
}
