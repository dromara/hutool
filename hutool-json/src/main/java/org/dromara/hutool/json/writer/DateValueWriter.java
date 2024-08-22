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

package org.dromara.hutool.json.writer;

import org.dromara.hutool.json.serialize.DateJSONString;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类型的值写出器<br>
 * 支持包括：{@link Date}、{@link Calendar}、{@link TemporalAccessor}
 *
 * @author looly
 * @since 6.0.0
 */
public class DateValueWriter implements JSONValueWriter {
	/**
	 * 单例对象
	 */
	public static final DateValueWriter INSTANCE = new DateValueWriter();

	@Override
	public boolean test(final Object value) {
		return value instanceof Date || value instanceof Calendar || value instanceof TemporalAccessor;
	}

	@Override
	public void write(final JSONWriter writer, final Object value) {
		writer.writeRaw(new DateJSONString(value, writer.getConfig()).toJSONString());
	}
}
