/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.setting.props;

import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.resource.NoResourceException;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Map;

/**
 * Props工具类<br>
 * 提供静态方法获取配置文件
 *
 * @author looly
 * @since 5.1.3
 */
public class PropsUtil {

	/**
	 * 配置文件缓存
	 */
	private static final Map<String, Props> propsMap = new SafeConcurrentHashMap<>();

	/**
	 * 获取当前环境下的配置文件<br>
	 * name可以为不包括扩展名的文件名（默认.properties），也可以是文件名全称
	 *
	 * @param name 文件名，如果没有扩展名，默认为.properties
	 * @return 当前环境下配置文件
	 */
	public static Props get(final String name) {
		return propsMap.computeIfAbsent(name, (filePath)->{
			final String extName = FileNameUtil.extName(filePath);
			if (StrUtil.isEmpty(extName)) {
				filePath = filePath + "." + Props.EXT_NAME;
			}
			return new Props(filePath);
		});
	}

	/**
	 * 获取给定路径找到的第一个配置文件<br>
	 * * name可以为不包括扩展名的文件名（默认.properties为结尾），也可以是文件名全称
	 *
	 * @param names 文件名，如果没有扩展名，默认为.properties
	 *
	 * @return 当前环境下配置文件
	 */
	public static Props getFirstFound(final String... names) {
		for (final String name : names) {
			try {
				return get(name);
			} catch (final NoResourceException e) {
				//ignore
			}
		}
		return null;
	}

	/**
	 * 获取系统参数，例如用户在执行java命令时定义的 -Duse=hutool
	 *
	 * @return 系统参数Props
	 * @since 5.5.2
	 */
	public static Props getSystemProps(){
		return new Props(System.getProperties());
	}
}
