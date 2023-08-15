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

package org.dromara.hutool.core.collection.iter;

import java.util.Iterator;

/**
 * 支持重置的{@link Iterator} 接口<br>
 * 通过实现{@link #reset()}，重置此{@link Iterator}后可实现复用重新遍历
 *
 * @param <E> 元素类型
 * @since 5.8.0
 */
public interface ResettableIter<E> extends Iterator<E> {

	/**
	 * 重置，重置后可重新遍历
	 */
	void reset();
}
