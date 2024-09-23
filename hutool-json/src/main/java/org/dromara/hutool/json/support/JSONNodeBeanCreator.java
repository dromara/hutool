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

package org.dromara.hutool.json.support;

import org.dromara.hutool.core.bean.path.BeanPath;
import org.dromara.hutool.core.bean.path.NodeBeanCreator;
import org.dromara.hutool.core.bean.path.node.NameNode;
import org.dromara.hutool.core.bean.path.node.Node;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;

/**
 * JSON节点Bean创建器
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONNodeBeanCreator implements NodeBeanCreator {

	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param config JSON配置
	 */
	public JSONNodeBeanCreator(final JSONConfig config) {
		this.config = config;
	}

	@Override
	public Object create(final Object parent, final BeanPath beanPath) {
		final BeanPath next = beanPath.next();
		if (null != next) {
			final Node node = next.getNode();
			if (node instanceof NameNode) {
				final NameNode nameNode = (NameNode) node;
				if (nameNode.isNumber()) {
					return JSONUtil.ofArray(config);
				}
				return JSONUtil.ofObj(config);
			}
		}
		return JSONUtil.ofObj(config);
	}
}
