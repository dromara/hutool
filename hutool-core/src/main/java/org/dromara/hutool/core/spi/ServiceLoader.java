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

package org.dromara.hutool.core.spi;

/**
 * SPI服务加载接口<br>
 * 用户实现此接口用于制定不同的服务加载方式
 *
 * @param <T> 服务对象类型
 * @author looly
 */
public interface ServiceLoader<T> {

	/**
	 * 加载服务
	 */
	void load();

	/**
	 * 服务总数
	 *
	 * @return 总数
	 */
	int size();
}
