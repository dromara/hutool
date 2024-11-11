/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.engine.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONPrimitive;

import java.io.IOException;

/**
 * Hutool JSON序列化器
 *
 * @author looly
 * @since 6.0.0
 */
public class HutoolJSONSerializer extends StdSerializer<JSON> {
	private static final long serialVersionUID = 1L;

	protected HutoolJSONSerializer() {
		super(JSON.class);
	}

	@Override
	public void serialize(final JSON json, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
		if(json instanceof JSONPrimitive){
			jsonGenerator.writeObject(((JSONPrimitive) json).getValue());
		}else if(json instanceof JSONObject){
			jsonGenerator.writeStartObject();
			json.asJSONObject().forEach((k, v)->{
				try {
					jsonGenerator.writeObjectField(k, v);
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			});
			jsonGenerator.writeEndObject();
		}else if(json instanceof JSONArray){
			jsonGenerator.writeStartArray();
			json.asJSONArray().forEach(v->{
				try {
					jsonGenerator.writeObject(v);
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			});
			jsonGenerator.writeEndArray();
		}
	}
}
