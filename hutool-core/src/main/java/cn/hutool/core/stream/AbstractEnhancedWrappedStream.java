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

package cn.hutool.core.stream;

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
