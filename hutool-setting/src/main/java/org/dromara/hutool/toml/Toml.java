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

package org.dromara.hutool.toml;

import org.dromara.hutool.io.resource.Resource;

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
	public static void write(Map<String, Object> data, Writer writer){
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
