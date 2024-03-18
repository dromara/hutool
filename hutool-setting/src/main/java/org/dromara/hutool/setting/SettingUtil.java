/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.setting;

import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.resource.NoResourceException;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Map;

/**
 * Setting工具类<br>
 * 提供静态方法获取配置文件
 *
 * @author looly
 */
public class SettingUtil {
	/**
	 * 配置文件缓存
	 */
	private static final Map<String, Setting> SETTING_MAP = new SafeConcurrentHashMap<>();

	/**
	 * 获取当前环境下的配置文件<br>
	 * name可以为不包括扩展名的文件名（默认.setting为结尾），也可以是文件名全称
	 *
	 * @param name 文件名，如果没有扩展名，默认为.setting
	 * @return 当前环境下配置文件
	 */
	public static Setting get(final String name) {
		return SETTING_MAP.computeIfAbsent(name, (filePath)->{
			final String extName = FileNameUtil.extName(filePath);
			if (StrUtil.isEmpty(extName)) {
				filePath = filePath + "." + Setting.EXT_NAME;
			}
			return new Setting(filePath, true);
		});
	}

	/**
	 * 获取给定路径找到的第一个配置文件<br>
	 * * name可以为不包括扩展名的文件名（默认.setting为结尾），也可以是文件名全称
	 *
	 * @param names 文件名，如果没有扩展名，默认为.setting
	 *
	 * @return 当前环境下配置文件
	 * @since 5.1.3
	 */
	public static Setting getFirstFound(final String... names) {
		for (final String name : names) {
			try {
				return get(name);
			} catch (final NoResourceException e) {
				//ignore
			}
		}
		return null;
	}
}
