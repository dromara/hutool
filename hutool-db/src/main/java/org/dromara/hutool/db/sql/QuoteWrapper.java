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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.db.Entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;

/**
 * 字段和表明包装器<br>
 * 主要用于字段名的包装（在字段名的前后加字符，例如反引号来避免与数据库的关键字冲突）
 *
 * @author Looly
 */
public class QuoteWrapper implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 无需包装和解包装的关键字
	 */
	private static final String[] IGNORE_WRAPPER_KEYS = {"*", "(", " ", " as "};

	/**
	 * 前置包装符号
	 */
	private Character preWrapQuote;
	/**
	 * 后置包装符号
	 */
	private Character sufWrapQuote;

	/**
	 * 构造
	 */
	public QuoteWrapper() {
	}

	/**
	 * 构造
	 *
	 * @param wrapQuote 单包装字符
	 */
	public QuoteWrapper(final Character wrapQuote) {
		this.preWrapQuote = wrapQuote;
		this.sufWrapQuote = wrapQuote;
	}

	/**
	 * 包装符号
	 *
	 * @param preWrapQuote 前置包装符号
	 * @param sufWrapQuote 后置包装符号
	 */
	public QuoteWrapper(final Character preWrapQuote, final Character sufWrapQuote) {
		this.preWrapQuote = preWrapQuote;
		this.sufWrapQuote = sufWrapQuote;
	}

	//--------------------------------------------------------------- Getters and Setters start

	/**
	 * @return 前置包装符号
	 */
	public char getPreWrapQuote() {
		return preWrapQuote;
	}

	/**
	 * 设置前置包装的符号
	 *
	 * @param preWrapQuote 前置包装符号
	 */
	public void setPreWrapQuote(final Character preWrapQuote) {
		this.preWrapQuote = preWrapQuote;
	}

	/**
	 * @return 后置包装符号
	 */
	public char getSufWrapQuote() {
		return sufWrapQuote;
	}

	/**
	 * 设置后置包装的符号
	 *
	 * @param sufWrapQuote 后置包装符号
	 */
	public void setSufWrapQuote(final Character sufWrapQuote) {
		this.sufWrapQuote = sufWrapQuote;
	}
	//--------------------------------------------------------------- Getters and Setters end

	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 *
	 * @param field 字段名
	 * @return 包装后的字段名
	 */
	public String wrap(final String field) {
		if (preWrapQuote == null || sufWrapQuote == null || StrUtil.isBlank(field)) {
			return field;
		}

		//如果已经包含包装的引号，返回原字符
		if (StrUtil.isWrap(field, preWrapQuote, sufWrapQuote)) {
			return field;
		}

		//如果字段中包含通配符或者括号（字段通配符或者函数），不做包装
		if (StrUtil.containsAnyIgnoreCase(field, IGNORE_WRAPPER_KEYS)) {
			return field;
		}

		//对于Oracle这类数据库，表名中包含用户名需要单独拆分包装
		if (field.contains(StrUtil.DOT)) {
			final Collection<String> target = CollUtil.edit(
				// 用户名和表名不能包含空格
				SplitUtil.split(field, StrUtil.DOT, 2, true, false),
				// 用户名和表名都加引号
				t -> StrUtil.wrap(t, preWrapQuote, sufWrapQuote));
			return CollUtil.join(target, StrUtil.DOT);
		}

		return StrUtil.wrap(field, preWrapQuote, sufWrapQuote);
	}

	/**
	 * 解包装字段名
	 *
	 * @param field 字段名
	 * @return 未包装的字段名
	 */
	public String unWrap(final String field) {
		if (preWrapQuote == null || sufWrapQuote == null || StrUtil.isBlank(field)) {
			return field;
		}

		//如果已经包含包装的引号，返回原字符
		if (!StrUtil.isWrap(field, preWrapQuote, sufWrapQuote)) {
			return field;
		}

		//如果字段中包含通配符或者括号（字段通配符或者函数），不做解包
		if (StrUtil.containsAnyIgnoreCase(field, IGNORE_WRAPPER_KEYS)) {
			return field;
		}

		//对于Oracle这类数据库，表名中包含用户名需要单独拆分包装
		if (field.contains(StrUtil.DOT)) {
			final Collection<String> target = CollUtil.edit(
				SplitUtil.split(field, StrUtil.DOT, 2, true, false),
				t -> StrUtil.unWrap(t, preWrapQuote, sufWrapQuote));
			return CollUtil.join(target, StrUtil.DOT);
		}

		return StrUtil.unWrap(field, preWrapQuote, sufWrapQuote);
	}

	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 *
	 * @param fields 字段名
	 * @return 包装后的字段名
	 */
	public String[] wrap(final String... fields) {
		if (ArrayUtil.isEmpty(fields)) {
			return fields;
		}

		final String[] wrappedFields = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			wrappedFields[i] = wrap(fields[i]);
		}

		return wrappedFields;
	}

	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 *
	 * @param fields 字段名
	 * @return 包装后的字段名
	 */
	public Collection<String> wrap(final Collection<String> fields) {
		if (CollUtil.isEmpty(fields)) {
			return fields;
		}

		return Arrays.asList(wrap(fields.toArray(new String[0])));
	}

	/**
	 * 包装表名和字段名，此方法返回一个新的Entity实体类<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 *
	 * @param entity 被包装的实体
	 * @return 新的实体
	 */
	public Entity wrap(final Entity entity) {
		if (null == entity) {
			return null;
		}

		final Entity wrapedEntity = new Entity();

		//wrap table name
		wrapedEntity.setTableName(wrap(entity.getTableName()));

		//wrap fields
		for (final Entry<String, Object> entry : entity.entrySet()) {
			wrapedEntity.set(wrap(entry.getKey()), entry.getValue());
		}

		return wrapedEntity;
	}

	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 *
	 * @param conditions 被包装的实体
	 * @return 包装后的字段名
	 */
	public Condition[] wrap(final Condition... conditions) {
		final Condition[] clonedConditions = new Condition[conditions.length];
		if (ArrayUtil.isNotEmpty(conditions)) {
			Condition clonedCondition;
			for (int i = 0; i < conditions.length; i++) {
				clonedCondition = conditions[i].clone();
				clonedCondition.setField(wrap(clonedCondition.getField()));
				clonedConditions[i] = clonedCondition;
			}
		}

		return clonedConditions;
	}
}
