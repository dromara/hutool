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

import org.dromara.hutool.core.lang.wrapper.Wrapper;

/**
 * 池化对象
 *
 * @param <T> 对象类型
 * @author Looly
 */
public interface Poolable<T> extends Wrapper<T> {

	/**
	 * 获取最后借出时间
	 *
	 * @return 最后借出时间
	 */
	long getLastBorrow();

	/**
	 * 设置最后借出时间，在成功借出此对象时更新时间
	 *
	 * @param lastBorrow 最后借出时间
	 */
	void setLastBorrow(final long lastBorrow);
}
