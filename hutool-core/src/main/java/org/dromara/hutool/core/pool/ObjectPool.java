/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.pool;

import java.io.Closeable;
import java.io.Serializable;

/**
 * 对象池接口，提供：
 * <ul>
 *     <li>{@link #borrowObject()}        对象借出。</li>
 *     <li>{@link #returnObject(Poolable)}对象归还。</li>
 * </ul>
 *
 * @param <T> 对象类型
 * @author looly
 */
public interface ObjectPool<T> extends Closeable, Serializable {

	/**
	 * 借出对象
	 *
	 * @return 对象
	 */
	Poolable<T> borrowObject();

	/**
	 * 归还对象
	 *
	 * @param obj 对象
	 * @return this
	 */
	ObjectPool<T> returnObject(final Poolable<T> obj);
}
