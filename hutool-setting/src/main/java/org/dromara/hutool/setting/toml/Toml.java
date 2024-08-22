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

package org.dromara.hutool.setting.toml;

import org.dromara.hutool.core.io.resource.Resource;

import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;

/**
 * TOML读写封装<br>
 * TODO 参考https://github.com/TheElectronWill/night-config改造
 *
 * @author TheElectronWill
 */
public class Toml {

	/**
	 * 读取TOML
	 *
	 * @param resource 资源
	 * @return TOML信息
	 */
	public static Map<String, Object> read(final Resource resource){
		return new TomlReader(resource.readUtf8Str(), false).read();
	}

	/**
	 * 将TOML数据写出到Writer
	 * @param data TOML数据
	 * @param writer {@link Writer}
	 */
	public static void write(final Map<String, Object> data, final Writer writer){
		new TomlWriter(writer).write(data);
	}

	/**
	 * A DateTimeFormatter that uses the TOML format.
	 */
	public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ISO_LOCAL_DATE)
			.optionalStart()
			.appendLiteral('T')
			.append(DateTimeFormatter.ISO_LOCAL_TIME)
			.optionalStart()
			.appendOffsetId()
			.optionalEnd()
			.optionalEnd()
			.toFormatter();
}
