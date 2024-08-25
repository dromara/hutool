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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.util.ObjUtil;

import java.util.*;

/**
 * Bean的Map接口实现<br>
 * 通过反射方式，将一个Bean的操作转化为Map操作
 *
 * @author Looly
 * @since 6.0.0
 */
public class BeanMap implements Map<String, Object> {

	/**
	 * 构建BeanMap
	 *
	 * @param bean Bean
	 * @return BeanMap
	 */
	public static BeanMap of(final Object bean) {
		return new BeanMap(bean);
	}

	private final Object bean;
	private final Map<String, PropDesc> propDescMap;

	/**
	 * 构造
	 *
	 * @param bean Bean
	 */
	public BeanMap(final Object bean) {
		this.bean = bean;
		this.propDescMap = BeanUtil.getBeanDesc(bean.getClass()).getPropMap(false);
	}

	@Override
	public int size() {
		return this.propDescMap.size();
	}

	@Override
	public boolean isEmpty() {
		return propDescMap.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.propDescMap.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		for (final PropDesc propDesc : this.propDescMap.values()) {
			if (ObjUtil.equals(propDesc.getValue(bean), value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object get(final Object key) {
		final PropDesc propDesc = this.propDescMap.get(key);
		if (null != propDesc) {
			return propDesc.getValue(bean);
		}
		return null;
	}

	@Override
	public Object put(final String key, final Object value) {
		final PropDesc propDesc = this.propDescMap.get(key);
		if (null != propDesc) {
			final Object oldValue = propDesc.getValue(bean);
			propDesc.setValue(bean, value);
			return oldValue;
		}
		return null;
	}

	@Override
	public Object remove(final Object key) {
		throw new UnsupportedOperationException("Can not remove field for Bean!");
	}

	@Override
	public void putAll(final Map<? extends String, ?> m) {
		m.forEach(this::put);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Can not clear fields for Bean!");
	}

	@Override
	public Set<String> keySet() {
		return this.propDescMap.keySet();
	}

	@Override
	public Collection<Object> values() {
		final List<Object> list = new ArrayList<>(size());
		for (final PropDesc propDesc : this.propDescMap.values()) {
			list.add(propDesc.getValue(bean));
		}
		return list;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		final HashSet<Entry<String, Object>> set = new HashSet<>(size(), 1);
		this.propDescMap.forEach((key, propDesc) -> set.add(new AbstractMap.SimpleEntry<>(key, propDesc.getValue(bean))));
		return set;
	}
}
