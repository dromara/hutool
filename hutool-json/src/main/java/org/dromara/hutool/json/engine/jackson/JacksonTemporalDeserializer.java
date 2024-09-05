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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.temporal.TemporalAccessor;

public class JacksonTemporalDeserializer extends StdDeserializer<TemporalAccessor> {

	private final Class<? extends TemporalAccessor> type;
	private final String dateFormat;

	public JacksonTemporalDeserializer(final Class<? extends TemporalAccessor> type, final String dateFormat) {
		super(TemporalAccessor.class);
		this.type = type;
		this.dateFormat = dateFormat;
	}

	@Override
	public TemporalAccessor deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JacksonException {
		return null;
	}
}
