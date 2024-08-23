/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.pool;

import java.io.Closeable;
import java.io.Serializable;

/**
 * 对象池接口，提供：
 * <ul>
 *     <li>{@link #borrowObject()}        对象借出。</li>
 *     <li>{@link #returnObject(Object)}对象归还。</li>
 *     <li>{@link #free(Object)}        对象销毁。</li>
 * </ul>
 * <p>
 * 对于对象池中对象维护，通过{@link PoolConfig#getMaxIdle()}控制，规则如下：
 * <ul>
 *     <li>如果借出量很多，则不断扩容，直到达到{@link PoolConfig#getMaxSize()}</li>
 *     <li>如果池对象闲置超出{@link PoolConfig#getMaxIdle()}，则销毁。</li>
 *     <li>实际使用中，池中对象可能少于{@link PoolConfig#getMinSize()}</li>
 * </ul>
 *
 * @param <T> 对象类型
 * @author Looly
 */
public interface ObjectPool<T> extends Closeable, Serializable {

	/**
	 * 借出对象，流程如下：
	 * <ol>
	 *     <li>从池中取出对象</li>
	 *     <li>检查对象可用性</li>
	 *     <li>如果无可用对象，扩容池并创建新对象</li>
	 *     <li>继续取对象</li>
	 * </ol>
	 *
	 * @return 对象
	 */
	T borrowObject();

	/**
	 * 归还对象，流程如下：
	 * <ol>
	 *     <li>检查对象可用性</li>
	 *     <li>不可用则销毁之</li>
	 *     <li>可用则入池</li>
	 * </ol>
	 *
	 * @param obj 对象
	 * @return this
	 */
	ObjectPool<T> returnObject(final T obj);

	/**
	 * 释放对象，即在使用中发现对象损坏或不可用，则直接销毁之
	 *
	 * @param obj 对象
	 * @return this
	 */
	ObjectPool<T> free(final T obj);

	/**
	 * 获取持有对象总数（包括空闲对象 + 正在使用对象数）
	 *
	 * @return 总数
	 */
	int getTotal();

	/**
	 * 获取空闲对象数，即在池中的对象数
	 *
	 * @return 空闲对象数，-1表示此信息不可用
	 */
	int getIdleCount();

	/**
	 * 获取已经借出的对象（正在使用的）对象数
	 *
	 * @return 正在使用的对象数，-1表示此对象不可用
	 */
	int getActiveCount();
}
