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

package org.dromara.hutool.lang;

/**
 * 责任链接口
 * @author Looly
 *
 * @param <E> 元素类型
 * @param <T> 目标类类型，用于返回this对象
 */
public interface Chain<E, T> extends Iterable<E>{
	/**
	 * 加入责任链
	 * @param element 责任链新的环节元素
	 * @return this
	 */
	T addChain(E element);
}
