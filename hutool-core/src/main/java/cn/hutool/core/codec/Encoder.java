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

package cn.hutool.core.codec;

/**
 * 编码接口
 *
 * @param <T> 被编码的数据类型
 * @param <R> 编码后的数据类型
 * @author looly
 * @since 5.7.22
 */
public interface Encoder<T, R> {

	/**
	 * 执行编码
	 *
	 * @param data 被编码的数据
	 * @return 编码后的数据
	 */
	R encode(T data);
}
