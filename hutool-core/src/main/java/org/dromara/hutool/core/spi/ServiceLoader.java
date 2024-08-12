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
