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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSONException;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Gson引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class GsonEngine extends AbstractJSONEngine{

	private Gson gson;

	/**
	 * 构造
	 */
	public GsonEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Gson.class);
	}

	@Override
	public void serialize(final Object bean, final Writer writer) {
		initEngine();
		gson.toJson(bean, writer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		if(type instanceof Class){
			return gson.fromJson(reader, (Class<T>)type);
		} else if(type instanceof Type){
			return gson.fromJson(reader, (Type)type);
		}

		throw new JSONException("Unsupported type: {}", type.getClass());
	}

	@Override
	protected void reset() {
		this.gson = null;
	}

	@Override
	protected void initEngine() {
		if(null != this.gson){
			return;
		}

		final GsonBuilder builder = new GsonBuilder();
		if(ObjUtil.defaultIfNull(this.config, JSONEngineConfig::isPrettyPrint, false)){
			builder.setPrettyPrinting();
		}
		this.gson = builder.create();
	}
}
