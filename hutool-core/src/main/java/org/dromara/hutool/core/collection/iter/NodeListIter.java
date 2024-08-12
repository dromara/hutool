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

package org.dromara.hutool.core.collection.iter;

import org.dromara.hutool.core.lang.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 包装 {@link NodeList} 的{@link Iterator}
 * <p>
 * 此 iterator 不支持 {@link #remove()} 方法。
 *
 * @author Apache-commons,looly
 * @see NodeList
 * @since 5.8.0
 */
public class NodeListIter implements ResettableIter<Node> {

	private final NodeList nodeList;
	/**
	 * 当前位置索引
	 */
	private int index = 0;

	/**
	 * 构造, 根据给定{@link NodeList} 创建{@code NodeListIterator}
	 *
	 * @param nodeList {@link NodeList}，非空
	 */
	public NodeListIter(final NodeList nodeList) {
		this.nodeList = Assert.notNull(nodeList, "NodeList must not be null.");
	}

	@Override
	public boolean hasNext() {
		return nodeList != null && index < nodeList.getLength();
	}

	@Override
	public Node next() {
		if (nodeList != null && index < nodeList.getLength()) {
			return nodeList.item(index++);
		}
		throw new NoSuchElementException("underlying nodeList has no more elements");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() method not supported for a NodeListIterator.");
	}

	@Override
	public void reset() {
		this.index = 0;
	}
}
