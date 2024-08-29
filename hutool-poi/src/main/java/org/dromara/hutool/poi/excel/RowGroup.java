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

package org.dromara.hutool.poi.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.dromara.hutool.core.collection.CollUtil;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 分组行<br>
 * 用于标识和写出复杂表头。
 * 分组概念灵感来自于EasyPOI的设计理念，见：https://blog.csdn.net/qq_45752401/article/details/121250993
 *
 * @author Looly
 * @since 6.0.0
 */
public class RowGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建分组
	 *
	 * @param name 分组名称
	 * @return RowGroup
	 */
	public static RowGroup of(final String name) {
		return new RowGroup(name);
	}

	private String name;
	private CellStyle style;
	private List<RowGroup> children;

	/**
	 * 构造
	 *
	 * @param name 分组名称
	 */
	public RowGroup(final String name) {
		this.name = name;
	}

	/**
	 * 获取分组名称
	 *
	 * @return 分组名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置分组名称
	 *
	 * @param name 分组名称
	 * @return this
	 */
	public RowGroup setName(final String name) {
		this.name = name;
		return this;
	}

	/**
	 * 获取样式
	 *
	 * @return 样式
	 */
	public CellStyle getStyle() {
		return style;
	}

	/**
	 * 设置样式
	 *
	 * @param style 样式
	 * @return this
	 */
	public RowGroup setStyle(final CellStyle style) {
		this.style = style;
		return this;
	}

	/**
	 * 获取子分组
	 *
	 * @return 子分组
	 */
	public List<RowGroup> getChildren() {
		return children;
	}

	/**
	 * 设置子分组
	 *
	 * @param children 子分组
	 * @return this
	 */
	public RowGroup setChildren(final List<RowGroup> children) {
		this.children = children;
		return this;
	}

	/**
	 * 添加指定名臣的子分组，最终分组
	 *
	 * @param name 子分组的名称
	 * @return this
	 */
	public RowGroup addChild(final String name) {
		return addChild(of(name));
	}

	/**
	 * 添加子分组
	 *
	 * @param child 子分组
	 * @return this
	 */
	public RowGroup addChild(final RowGroup child) {
		if (null == this.children) {
			// 无随机获取节点，节省空间
			this.children = new LinkedList<>();
		}
		this.children.add(child);
		return this;
	}

	/**
	 * 分组占用的最大列数，取决于子分组占用列数
	 *
	 * @return 列数
	 */
	public int maxColumnCount() {
		if (CollUtil.isEmpty(this.children)) {
			// 无子分组，1列
			return 1;
		}
		return children.stream().mapToInt(RowGroup::maxColumnCount).sum();
	}

	/**
	 * 获取最大行数，取决于子分组行数<br>
	 * 结果为：标题行占用行数 + 子分组占用行数
	 *
	 * @return 最大行数
	 */
	public int maxRowCount() {
		int maxRowCount = childrenMaxRowCount();
		if (null != this.name) {
			maxRowCount++;
		}

		if (0 == maxRowCount) {
			throw new IllegalArgumentException("Empty RowGroup!, please set the name or add children.");
		}

		return maxRowCount;
	}

	/**
	 * 获取子分组最大占用行数
	 * @return 子分组最大占用行数
	 */
	public int childrenMaxRowCount() {
		int maxRowCount = 0;
		if (null != this.children) {
			maxRowCount = this.children.stream().mapToInt(RowGroup::maxRowCount).max().orElse(0);
		}
		return maxRowCount;
	}
}
