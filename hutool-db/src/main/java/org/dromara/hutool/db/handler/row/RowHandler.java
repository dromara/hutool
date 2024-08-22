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

package org.dromara.hutool.db.handler.row;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行处理器，用于处理一行数据<br>
 * 传入前，须调用{@link ResultSet#next()}定位到行。
 *
 * @param <R> 一行数据处理后的结果类型
 * @author looly
 * @since 6.0.0
 */
@FunctionalInterface
public interface RowHandler<R> {

	/**
	 * 处理一行数据
	 *
	 * @param rs {@link ResultSet}，传入前，须调用{@link ResultSet#next()}定位到行。
	 * @return 处理行结果对象
	 * @throws SQLException SQL异常
	 */
	R handle(ResultSet rs) throws SQLException;
}
