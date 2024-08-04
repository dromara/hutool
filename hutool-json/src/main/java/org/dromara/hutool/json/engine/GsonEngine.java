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
public class GsonEngine implements JSONEngine{

	private final Gson gson;

	/**
	 * 构造
	 */
	public GsonEngine() {
		this.gson = new GsonBuilder().create();
	}

	@Override
	public void serialize(final Object bean, final Writer writer) {
		gson.toJson(bean, writer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		if(type instanceof Class){
			return gson.fromJson(reader, (Class<T>)type);
		} else if(type instanceof Type){
			return gson.fromJson(reader, (Type)type);
		}

		throw new JSONException("Unsupported type: {}", type.getClass());
	}
}
