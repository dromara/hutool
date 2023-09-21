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

package org.dromara.hutool.core.spi;

import java.util.List;

/**
 * SPI服务加载接口<br>
 * 用户实现此接口用于制定不同的服务加载方式
 *
 * @param <S> 服务对象类型
 * @author looly
 */
public interface ServiceLoader<S> extends Iterable<S> {

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

	/**
	 * 获取服务名称列表
	 *
	 * @return 服务名称列表
	 */
	List<String> getServiceNames();

	/**
	 * 获取指定服务的实现类
	 *
	 * @param serviceName 服务名称
	 * @return 服务名称对应的实现类
	 */
	Class<S> getServiceClass(String serviceName);

	/**
	 * 获取指定名称对应的服务
	 *
	 * @param serviceName 服务名称
	 * @return 服务对象
	 */
	S getService(String serviceName);
}
