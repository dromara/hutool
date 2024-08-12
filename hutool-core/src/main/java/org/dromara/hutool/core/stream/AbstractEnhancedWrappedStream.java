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

package org.dromara.hutool.core.stream;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * {@link WrappedStream}接口的公共实现，用于包装并增强一个已有的流实例
 *
 * @param <T> 流中的元素类型
 * @param <S> {@link AbstractEnhancedWrappedStream}的实现类类型
 * @author huangchengxing VampireAchao
 * @see EasyStream
 * @see EntryStream
 * @since 6.0.0
 */
public abstract class AbstractEnhancedWrappedStream<T, S extends AbstractEnhancedWrappedStream<T, S>>
	implements TerminableWrappedStream<T, S>, TransformableWrappedStream<T, S> {

	/**
	 * 原始流实例
	 */
	protected Stream<T> stream;

	/**
	 * 获取被包装的元素流实例
	 */
	@Override
	public Stream<T> unwrap() {
		return stream;
	}

	/**
	 * 创建一个流包装器
	 *
	 * @param stream 包装的流对象
	 * @throws NullPointerException 当{@code unwrap}为{@code null}时抛出
	 */
	protected AbstractEnhancedWrappedStream(final Stream<T> stream) {
		this.stream = Objects.requireNonNull(stream, "unwrap must not null");
	}

	/**
	 * 获取当前被包装的实例的哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return stream.hashCode();
	}

	/**
	 * 比较被包装的实例是否相等
	 *
	 * @param obj 对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Stream && stream.equals(obj);
	}

	/**
	 * 将被包装的实例转为字符串
	 *
	 * @return 字符串
	 */
	@Override
	public String toString() {
		return stream.toString();
	}

	/**
	 * 触发流的执行，这是一个终端操作
	 */
	public void exec() {
		stream.forEach(t -> {});
	}

}
