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

package cn.hutool.setting.dialect;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.text.StrUtil;

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
