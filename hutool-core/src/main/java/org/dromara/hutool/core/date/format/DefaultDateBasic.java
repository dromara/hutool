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

package org.dromara.hutool.core.date.format;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 默认日期基本信息类，包括：
 * <ul>
 *     <li>{@link #getPattern()}返回{@code null}</li>
 *     <li>{@link #getTimeZone()} ()}返回{@link TimeZone#getDefault()}</li>
 *     <li>{@link #getLocale()} ()} ()}返回{@link Locale#getDefault()}</li>
 * </ul>
 *
 * @author looly
 */
public class DefaultDateBasic implements DateBasic, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String getPattern() {
		return null;
	}

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}
}
