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

package org.dromara.hutool.json.serializer;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.set.ConcurrentHashSet;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.serializer.impl.KotlinDeserializer;
import org.dromara.hutool.json.serializer.impl.TemporalAccessorSerializer;
import org.dromara.hutool.json.serializer.impl.TimeZoneSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * JSON序列化和反序列化管理器，用于管理JSON序列化器，注册和注销自定义序列化器和反序列化器。<br>
 * 此管理器管理着两种类型的序列化器和反序列化器：
 * <ul>
 *     <li>类型精准匹配方式。通过Java对象类型匹配，只会匹配查找的类型，而不匹配子类。可以调用{@link #register(Type, JSONSerializer)} 和 {@link #register(Type, JSONDeserializer)}注册。</li>
 *     <li>匹配器（Matcher）方式。通过判断序列化和反序列化器中match方法，找到自定义的序列化和反序列化器，可以调用{@link #register(MatcherJSONSerializer)} 和 {@link #register(MatcherJSONDeserializer)}注册。</li>
 * </ul>
 *
 * 管理器的使用分为三种方式：
 * <ul>
 *     <li>全局模式：  使用{@link SerializerManager#getInstance()}调用单例，全局可用。</li>
 *     <li>实例模式：  使用{@link SerializerManager#of()}创建实例，局部可用。</li>
 *     <li>自定义模式：使用{@code new SerializerManager()}创建实例，不加载默认的转换器。</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class SerializerManager {
	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final SerializerManager INSTANCE = new SerializerManager();
		static {
			registerDefault(INSTANCE);
		}
	}

	/**
	 * 获得单例的 SerializerManager
	 *
	 * @return SerializerManager
	 */
	public static SerializerManager getInstance() {
		return SerializerManager.SingletonHolder.INSTANCE;
	}

	/**
	 * 创建SerializerManager，附带默认的序列化器和反序列化器
	 *
	 * @return SerializerManager
	 */
	public static SerializerManager of() {
		final SerializerManager serializerManager = new SerializerManager();
		registerDefault(serializerManager);
		return serializerManager;
	}

	/**
	 * 用户自定义序列化器，存储自定义匹配规则的一类对象的转换器
	 */
	private volatile Set<MatcherJSONSerializer<?>> serializerSet;
	/**
	 * 用户自定义精确类型转换器<br>
	 * 主要存储类型明确（无子类）的转换器
	 */
	private volatile Map<Type, JSONSerializer<?>> serializerMap;
	/**
	 * 用户自定义类型转换器，存储自定义匹配规则的一类对象的转换器
	 */
	private volatile Set<MatcherJSONDeserializer<?>> deserializerSet;
	/**
	 * 用户自定义精确类型转换器<br>
	 * 主要存储类型明确（无子类）的转换器
	 */
	private volatile Map<Type, JSONDeserializer<?>> deserializerMap;

	/**
	 * 构造
	 */
	public SerializerManager() {
	}

	// region ----- register

	/**
	 * 注册自定义序列化器，用于自定义对象序列化<br>
	 * 当按照匹配规则匹配时，使用对应的序列化器进行序列化
	 *
	 * @param serializer 自定义序列化器
	 * @return this
	 */
	public SerializerManager register(final MatcherJSONSerializer<?> serializer) {
		if (null != serializer) {
			getSerializerSet().add(serializer);
		}
		return this;
	}

	/**
	 * 注册自定义序列化器，用于自定义对象序列化<br>
	 * 当类型精准匹配时，使用对应的序列化器进行序列化
	 *
	 * @param type       类型
	 * @param serializer 自定义序列化器，{@code null}表示移除
	 * @return this
	 */
	public SerializerManager register(final Type type, final JSONSerializer<?> serializer) {
		Assert.notNull(type);
		if (null == serializer) {
			getSerializerMap().remove(type);
		} else {
			getSerializerMap().put(type, serializer);
		}
		return this;
	}

	/**
	 * 注册自定义反序列化器，用于自定义对象反序列化<br>
	 * 当按照匹配规则匹配时，使用对应的反序列化器进行反序列化
	 *
	 * @param deserializer 自定义反序列化器
	 * @return this
	 */
	public SerializerManager register(final MatcherJSONDeserializer<?> deserializer) {
		if (null != deserializer) {
			getDeserializerSet().add(deserializer);
		}
		return this;
	}

	/**
	 * 注册自定义反序列化器，用于自定义对象反序列化<br>
	 * 当类型精准匹配时，使用对应的反序列化器进行反序列化
	 *
	 * @param type         类型，{@code null}表示
	 * @param deserializer 自定义反序列化器，{@code null}表示移除
	 * @return this
	 */
	public SerializerManager register(final Type type, final JSONDeserializer<?> deserializer) {
		Assert.notNull(type);
		if (null == deserializer) {
			getDeserializerMap().remove(type);
		} else {
			getDeserializerMap().put(type, deserializer);
		}
		return this;
	}
	// endregion

	// region ----- getSerializer or Deserializer

	/**
	 * 获取匹配器对应的序列化器
	 *
	 * @param bean 对象
	 * @return JSONSerializer
	 */
	@SuppressWarnings({"unchecked"})
	public MatcherJSONSerializer<Object> getSerializer(final Object bean) {
		if(CollUtil.isNotEmpty(this.serializerSet)){
			for (final MatcherJSONSerializer<?> serializer : this.serializerSet) {
				if (serializer.match(bean, null)) {
					return (MatcherJSONSerializer<Object>) serializer;
				}
			}
		}
		return null;
	}

	/**
	 * 获取匹配器对应的序列化器
	 *
	 * @param type 类型
	 * @return JSONSerializer
	 */
	@SuppressWarnings("unchecked")
	public JSONSerializer<Object> getSerializer(final Type type) {
		if(null == type || CollUtil.isEmpty(this.serializerMap)){
			return null;
		}
		return (JSONSerializer<Object>) this.serializerMap.get(type);
	}

	/**
	 * 获取匹配器对应的反序列化器
	 *
	 * @param json JSON
	 * @param type 类型
	 * @return JSONDeserializer
	 */
	@SuppressWarnings("unchecked")
	public JSONDeserializer<Object> getDeserializer(final JSON json, final Type type) {
		final Class<?> rawType = TypeUtil.getClass(type);
		if(null == rawType){
			return null;
		}
		if (JSONDeserializer.class.isAssignableFrom(rawType)) {
			return (JSONDeserializer<Object>) ConstructorUtil.newInstanceIfPossible(rawType);
		}

		if(CollUtil.isNotEmpty(this.deserializerMap)){
			final JSONDeserializer<?> jsonDeserializer = this.deserializerMap.get(type);
			if(null != jsonDeserializer){
				return (JSONDeserializer<Object>) jsonDeserializer;
			}
		}

		// Matcher
		if(CollUtil.isNotEmpty(this.deserializerSet)){
			for (final MatcherJSONDeserializer<?> deserializer : this.deserializerSet) {
				if (deserializer.match(json, type)) {
					return (JSONDeserializer<Object>) deserializer;
				}
			}
		}

		return null;
	}
	// endregion

	// region ----- getSet or Map
	private Set<MatcherJSONSerializer<?>> getSerializerSet() {
		if (null == this.serializerSet) {
			synchronized (this) {
				if (null == this.serializerSet) {
					this.serializerSet = new ConcurrentHashSet<>();
				}
			}
		}
		return this.serializerSet;
	}

	private Map<Type, JSONSerializer<?>> getSerializerMap() {
		if (null == this.serializerMap) {
			synchronized (this) {
				if (null == this.serializerMap) {
					this.serializerMap = new SafeConcurrentHashMap<>();
				}
			}
		}
		return this.serializerMap;
	}

	private Set<MatcherJSONDeserializer<?>> getDeserializerSet() {
		if (null == this.deserializerSet) {
			synchronized (this) {
				if (null == this.deserializerSet) {
					this.deserializerSet = new ConcurrentHashSet<>();
				}
			}
		}
		return this.deserializerSet;
	}

	private Map<Type, JSONDeserializer<?>> getDeserializerMap() {
		if (null == this.deserializerMap) {
			synchronized (this) {
				if (null == this.deserializerMap) {
					this.deserializerMap = new SafeConcurrentHashMap<>();
				}
			}
		}
		return this.deserializerMap;
	}

	/**
	 * 注册默认的序列化器和反序列化器
	 * @param manager {@code SerializerManager}
	 */
	private static void registerDefault(final SerializerManager manager) {
		manager.register(LocalDate.class, (JSONSerializer<?>) new TemporalAccessorSerializer(LocalDate.class));
		manager.register(LocalDate.class, (JSONDeserializer<?>) new TemporalAccessorSerializer(LocalDate.class));

		manager.register(LocalTime.class, (JSONSerializer<?>) new TemporalAccessorSerializer(LocalTime.class));
		manager.register(LocalTime.class, (JSONDeserializer<?>) new TemporalAccessorSerializer(LocalTime.class));

		manager.register(LocalDateTime.class, (JSONSerializer<?>) new TemporalAccessorSerializer(LocalDateTime.class));
		manager.register(LocalDateTime.class, (JSONDeserializer<?>) new TemporalAccessorSerializer(LocalDateTime.class));

		manager.register((MatcherJSONSerializer<TimeZone>) TimeZoneSerializer.INSTANCE);
		manager.register((MatcherJSONDeserializer<TimeZone>) TimeZoneSerializer.INSTANCE);

		// issue#I5WDP0 对于Kotlin对象，由于参数可能非空限制，导致无法创建一个默认的对象再赋值
		manager.register(KotlinDeserializer.INSTANCE);
	}
	// endregion
}
