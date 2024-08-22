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

package org.dromara.hutool.setting.profile;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.setting.Setting;

/**
 * 全局的Profile配置中心
 *
 * @author Looly
 *
 */
public class GlobalProfile {

	private GlobalProfile() {
	}

	// -------------------------------------------------------------------------------- Static method start
	/**
	 * 设置全局环境
	 * @param profile 环境
	 * @return {@link Profile}
	 */
	public static Profile setProfile(final String profile) {
		return Singleton.get(Profile.class, profile);
	}

	/**
	 * 获得全局的当前环境下对应的配置文件
	 * @param settingName 配置文件名，可以忽略默认后者（.setting）
	 * @return {@link Setting}
	 */
	public static Setting getSetting(final String settingName) {
		return Singleton.get(Profile.class).getSetting(settingName);
	}
	// -------------------------------------------------------------------------------- Static method end
}
