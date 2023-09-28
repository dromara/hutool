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
