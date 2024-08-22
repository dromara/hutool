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

package org.dromara.hutool.db.config;

/**
 * 数据库配置解析接口，通过实现此接口，可完成不同类型的数据源解析为数据库配置
 *
 * @author Looly
 */
public interface ConfigParser {
	/**
	 * 解析，包括数据库基本连接信息、连接属性、连接池参数和其他配置项等
	 *
	 * @param group 分组，当配置文件中有多个数据源时，通过分组区分
	 * @return {@link DbConfig}
	 */
	DbConfig parse(String group);
}
