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

package cn.hutool.json;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.json.serialize.JSONDeserializer;
import cn.hutool.json.serialize.JSONStringer;

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
				InternalJSONUtil.toCopyOptions(json.getConfig())).copy();
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
