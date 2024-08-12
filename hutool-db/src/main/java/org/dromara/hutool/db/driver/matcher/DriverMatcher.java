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

package org.dromara.hutool.db.driver.matcher;

import java.util.function.Predicate;

/**
 * 驱动匹配接口，通过实现此接口，可以：<br>
 * 通过{@link #test(Object)} 判断JDBC URL 是否匹配驱动的要求<br>
 * 通过{@link #getClassName()} 获取对应的驱动类名称
 *
 * @author looly
 */
public interface DriverMatcher extends Predicate<String> {

	/**
	 * 获取对应的驱动类名称
	 *
	 * @return 对应的驱动类名称
	 */
	String getClassName();
}
