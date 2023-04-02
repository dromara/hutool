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

package org.dromara.hutool.handler;

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
