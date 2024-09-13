/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.writer;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.json.writer.impl.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ValueWriter管理器，用于管理ValueWriter，提供ValueWriter的注册和获取
 *
 * @author looly
 * @since 5.8.0
 */
public class ValueWriterManager {

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final ValueWriterManager INSTANCE;
		static {
			INSTANCE = new ValueWriterManager();
			registerDefault();
		}
	}

	/**
	 * 获得单例的 ValueWriterManager
	 *
	 * @return ValueWriterManager
	 */
	public static ValueWriterManager getInstance() {
		return ValueWriterManager.SingletonHolder.INSTANCE;
	}

	private final List<ValueWriter> valueWriterList;

	/**
	 * 构造
	 */
	public ValueWriterManager() {
		this.valueWriterList = Collections.synchronizedList(new ArrayList<>(6));
	}

	/**
	 * 加入自定义的对象值写出规则
	 *
	 * @param valueWriter 自定义对象写出实现
	 */
	public void register(final ValueWriter valueWriter) {
		valueWriterList.add(Assert.notNull(valueWriter));
	}

	/**
	 * 获取自定义对象值写出规则，后加入的优先
	 *
	 * @param value 值，{@code null}表示需要自定义null的输出
	 * @return 自定义的 {@link ValueWriter}
	 */
	public ValueWriter get(final Object value) {
		if (value instanceof ValueWriter) {
			return (ValueWriter) value;
		}

		final List<ValueWriter> valueWriterList = this.valueWriterList;
		ValueWriter valueWriter;
		for (int i = valueWriterList.size() - 1 ; i >= 0 ; i--) {
			valueWriter = valueWriterList.get(i);
			if (valueWriter.test(value)) {
				return valueWriter;
			}
		}
		return null;
	}

	/**
	 * 注册默认的ValueWriter
	 */
	private static void registerDefault() {
		final ValueWriterManager manager = SingletonHolder.INSTANCE;
		// JDK对象最后判断
		manager.register(JdkValueWriter.INSTANCE);
		manager.register(NumberValueWriter.INSTANCE);
		manager.register(DateValueWriter.INSTANCE);
		manager.register(BooleanValueWriter.INSTANCE);
	}
}
