/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean.path;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.path.node.NameNode;
import org.dromara.hutool.core.bean.path.node.Node;
import org.dromara.hutool.core.bean.path.node.NodeFactory;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Bean路径表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
 * <ol>
 *   <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
 *   <li>[]表达式，可以获取集合等对象中对应index的值</li>
 * </ol>
 * <p>
 * 表达式栗子：
 *
 * <pre>
 * persion
 * persion.name
 * persons[3]
 * person.friends[5].name
 * ['person']['friends'][5]['name']
 * </pre>
 *
 * @author Looly
 * @since 6.0.0
 */
public class BeanPath implements Iterator<BeanPath> {

	/**
	 * 表达式边界符号数组
	 */
	private static final char[] EXP_CHARS = {CharUtil.DOT, CharUtil.BRACKET_START, CharUtil.BRACKET_END};

	/**
	 * 创建Bean路径
	 *
	 * @param expression 表达式
	 * @return BeanPath
	 */
	public static BeanPath of(final String expression) {
		return new BeanPath(expression);
	}

	private final Node node;
	private final String child;

	/**
	 * 构造
	 *
	 * @param expression 表达式
	 */
	public BeanPath(final String expression) {
		final int length = expression.length();
		final StringBuilder builder = new StringBuilder();

		char c;
		boolean isNumStart = false;// 下标标识符开始
		boolean isInWrap = false; //标识是否在引号内
		for (int i = 0; i < length; i++) {
			c = expression.charAt(i);
			if ('\'' == c) {
				// 结束
				isInWrap = (!isInWrap);
				continue;
			}

			if (!isInWrap && ArrayUtil.contains(EXP_CHARS, c)) {
				// 处理边界符号
				if (CharUtil.BRACKET_END == c) {
					// 中括号（数字下标）结束
					if (!isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, i));
					}
					isNumStart = false;
					// 中括号结束加入下标
				} else {
					if (isNumStart) {
						// 非结束中括号情况下发现起始中括号报错（中括号未关闭）
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, i));
					} else if (CharUtil.BRACKET_START == c) {
						// 数字下标开始
						isNumStart = true;
					}
					// 每一个边界符之前的表达式是一个完整的KEY，开始处理KEY
				}
				if (builder.length() > 0) {
					this.node = NodeFactory.createNode(builder.toString());
					// 如果以[结束，表示后续还有表达式，需保留'['，如name[0]
					this.child = StrUtil.nullIfEmpty(expression.substring(c == CharUtil.BRACKET_START ? i : i + 1));
					return;
				}
			} else {
				// 非边界符号，追加字符
				builder.append(c);
			}
		}

		// 最后的节点
		if (isNumStart) {
			throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, length - 1));
		} else {
			this.node = NodeFactory.createNode(builder.toString());
			this.child = null;
		}
	}

	/**
	 * 获取节点
	 *
	 * @return 节点
	 */
	public Node getNode() {
		return this.node;
	}

	/**
	 * 获取子表达式
	 *
	 * @return 子表达式
	 */
	public String getChild() {
		return this.child;
	}

	@Override
	public boolean hasNext() {
		return null != this.child;
	}

	@Override
	public BeanPath next() {
		return new BeanPath(this.child);
	}

	/**
	 * 获取路径对应的值
	 *
	 * @param bean Bean对象
	 * @return 路径对应的值
	 */
	public Object getValue(final Object bean) {
		final Object value = this.node.getValue(bean);
		if (!hasNext()) {
			return value;
		}
		return next().getValue(value);
	}

	/**
	 * 设置路径对应的值，如果路径节点为空，自动创建之
	 *
	 * @param bean  Bean对象
	 * @param value 设置的值
	 */
	public void setValue(Object bean, final Object value) {
		Object parentBean;
		BeanPath beanPath = this;
		while (beanPath.hasNext()) {
			parentBean = bean;
			bean = beanPath.node.getValue(bean);
			if (null == bean) {
				final BeanPath child = beanPath.next();
				bean = isListNode(child.node) ? new ArrayList<>() : new HashMap<>();
				beanPath.node.setValue(parentBean, bean);
				// 如果自定义put方法修改了value，此处二次get避免丢失
				bean = beanPath.node.getValue(parentBean);
			}
			beanPath = beanPath.next();
		}
		beanPath.node.setValue(bean, value);
	}

	@Override
	public String toString() {
		return "BeanPath{" +
			"node=" + node +
			", child='" + child + '\'' +
			'}';
	}

	/**
	 * 子节点值是否为列表
	 *
	 * @param node 节点
	 * @return 是否为列表
	 */
	private static boolean isListNode(final Node node) {
		if (node instanceof NameNode) {
			return ((NameNode) node).isNumber();
		}
		return false;
	}
}
