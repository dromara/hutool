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

package org.dromara.hutool.db.handler;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理接口<br>
 * 此接口用于实现{@link ResultSet} 转换或映射为用户指定的pojo对象
 * <p>
 * 默认实现有：
 * @see EntityHandler
 * @see EntityListHandler
 * @see EntitySetHandler
 * @see EntitySetHandler
 * @see NumberHandler
 * @see PageResultHandler
 *
 * @author Luxiaolei
 *
 * @param <T> 处理后的对象类型
 */
@FunctionalInterface
public interface RsHandler<T> extends Serializable{

	/**
	 * 处理结果集<br>
	 * 结果集处理后不需要关闭
	 * @param rs 结果集
	 * @return 处理后生成的对象
	 * @throws SQLException SQL异常
	 */
	T handle(ResultSet rs) throws SQLException;
}
