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

package org.dromara.hutool.core.codec.hash;

import org.dromara.hutool.core.codec.Encoder;

/**
 * Hash计算接口
 *
 * @param <T> 被计算hash的对象类型
 * @author looly
 * @since 5.2.5
 */
@FunctionalInterface
public interface Hash64<T> extends Encoder<T, Number> {
	/**
	 * 计算Hash值
	 *
	 * @param t 对象
	 * @return hash
	 */
	long hash64(T t);

	@Override
	default Number encode(final T t){
		return hash64(t);
	}
}
