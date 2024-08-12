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

package org.dromara.hutool.poi;

import org.dromara.hutool.core.exception.DependencyException;
import org.dromara.hutool.core.classloader.ClassLoaderUtil;

/**
 * POI引入检查器
 *
 * @author looly
 * @since 4.0.10
 */
public class PoiChecker {

	/** 没有引入POI的错误消息 */
	public static final String NO_POI_ERROR_MSG = "You need to add dependency of 'poi-ooxml' to your project, and version >= 5.3.0";

	/**
	 * 检查POI包的引入情况
	 */
	public static void checkPoiImport() {
		try {
			Class.forName("org.apache.poi.ss.usermodel.Workbook", false, ClassLoaderUtil.getClassLoader());
		} catch (final ClassNotFoundException | NoClassDefFoundError | NoSuchMethodError  e) {
			throw new DependencyException(e, NO_POI_ERROR_MSG);
		}
	}
}
