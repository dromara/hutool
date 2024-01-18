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

package org.dromara.hutool.db;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.func.SerSupplier;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.db.sql.SqlUtil;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.RowId;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * 数据实体对象<br>
 * 数据实体类充当两个角色：<br>
 * 1. 数据的载体，一个Entity对应数据库中的一个row<br>
 * 2. SQL条件，Entity中的每一个字段对应一个条件，字段值对应条件的值
 *
 * @author loolly
 */
public class Entity extends Dict {
	private static final long serialVersionUID = -1951012511464327448L;

	// --------------------------------------------------------------- Static method start

	/**
	 * 创建Entity
	 *
	 * @return Entity
	 */
	public static Entity of() {
		return new Entity();
	}

	/**
	 * 创建Entity
	 *
	 * @param tableName 表名
	 * @return Entity
	 */
	public static Entity of(final String tableName) {
		return new Entity(tableName);
	}

	/**
	 * 将PO对象转为Entity
	 *
	 * @param <T>  Bean对象类型
	 * @param bean Bean对象
	 * @return Entity
	 */
	public static <T> Entity parse(final T bean) {
		return of(null).parseBean(bean);
	}

	/**
	 * 将PO对象转为Entity
	 *
	 * @param <T>               Bean对象类型
	 * @param bean              Bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue   是否忽略值为空的字段
	 * @return Entity
	 */
	public static <T> Entity parse(final T bean, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		return of(null).parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 将PO对象转为Entity,并采用下划线法转换字段
	 *
	 * @param <T>  Bean对象类型
	 * @param bean Bean对象
	 * @return Entity
	 */
	public static <T> Entity parseWithUnderlineCase(final T bean) {
		return of(null).parseBean(bean, true, true);
	}
	// --------------------------------------------------------------- Static method end

	/* 表名 */
	private String tableName;
	/* 字段名列表，用于限制加入的字段的值 */
	private Set<String> fieldNames;

	// region ----- Constructor
	/**
	 * 构造
	 */
	public Entity() {
	}

	/**
	 * 构造
	 *
	 * @param tableName 数据表名
	 */

	public Entity(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 构造
	 *
	 * @param tableName       数据表名
	 * @param caseInsensitive 是否大小写不敏感
	 * @since 4.5.16
	 */
	public Entity(final String tableName, final boolean caseInsensitive) {
		super(caseInsensitive);
		this.tableName = tableName;
	}
	// endregion

	// --------------------------------------------------------------- Getters and Setters start

	/**
	 * @return 获得表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名
	 *
	 * @param tableName 表名
	 * @return 本身
	 */
	public Entity setTableName(final String tableName) {
		this.tableName = tableName;
		return this;
	}

	/**
	 * @return 字段集合
	 */
	public Set<String> getFieldNames() {
		return this.fieldNames;
	}

	/**
	 * 设置字段列表，用于限制加入的字段的值
	 *
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(final Collection<String> fieldNames) {
		if (CollUtil.isNotEmpty(fieldNames)) {
			this.fieldNames = SetUtil.of(true, fieldNames);
		}
		return this;
	}

	/**
	 * 设置字段列表，用于限制加入的字段的值
	 *
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(final String... fieldNames) {
		if (ArrayUtil.isNotEmpty(fieldNames)) {
			this.fieldNames = SetUtil.ofLinked(fieldNames);
		}
		return this;
	}

	/**
	 * 通过lambda批量设置值
	 * @param fields lambda,不能为空
	 * @return this
	 */
	@Override
	public Entity setFields(final SerSupplier<?>... fields) {
		return (Entity) super.setFields(fields);
	}

	/**
	 * 添加字段列表
	 *
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity addFieldNames(final String... fieldNames) {
		if (ArrayUtil.isNotEmpty(fieldNames)) {
			if (null == this.fieldNames) {
				return setFieldNames(fieldNames);
			} else {
				Collections.addAll(this.fieldNames, fieldNames);
			}
		}
		return this;
	}

	// --------------------------------------------------------------- Getters and Setters end

	/**
	 * 将值对象转换为Entity<br>
	 * 类名会被当作表名，小写第一个字母
	 *
	 * @param <T>  Bean对象类型
	 * @param bean Bean对象
	 * @return 自己
	 */
	@Override
	public <T> Entity parseBean(final T bean) {
		if (StrUtil.isBlank(this.tableName)) {
			this.setTableName(StrUtil.lowerFirst(bean.getClass().getSimpleName()));
		}
		return (Entity) super.parseBean(bean);
	}

	/**
	 * 将值对象转换为Entity<br>
	 * 类名会被当作表名，小写第一个字母
	 *
	 * @param <T>               Bean对象类型
	 * @param bean              Bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue   是否忽略值为空的字段
	 * @return 自己
	 */
	@Override
	public <T> Entity parseBean(final T bean, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		if (StrUtil.isBlank(this.tableName)) {
			final String simpleName = bean.getClass().getSimpleName();
			this.setTableName(isToUnderlineCase ? StrUtil.toUnderlineCase(simpleName) : StrUtil.lowerFirst(simpleName));
		}
		return (Entity) super.parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 过滤Map保留指定键值对，如果键不存在跳过
	 *
	 * @param keys 键列表
	 * @return Dict 结果
	 * @since 4.0.10
	 */
	@Override
	public Entity filterNew(final String... keys) {
		final Entity result = new Entity(this.tableName);
		result.setFieldNames(this.fieldNames);

		for (final String key : keys) {
			if (this.containsKey(key)) {
				result.put(key, this.get(key));
			}
		}
		return result;
	}

	@Override
	public Entity removeNew(final String... keys) {
		return (Entity) super.removeNew(keys);
	}

	// -------------------------------------------------------------------- Put and Set start
	@Override
	public Entity set(final String field, final Object value) {
		return (Entity) super.set(field, value);
	}

	@Override
	public Entity setIgnoreNull(final String field, final Object value) {
		return (Entity) super.setIgnoreNull(field, value);
	}
	// -------------------------------------------------------------------- Put and Set end

	// -------------------------------------------------------------------- Get start

	/**
	 * 获得Clob类型结果，如果结果类型非Clob，不做转换，直接抛出异常
	 *
	 * @param field 参数
	 * @return Clob
	 */
	public Clob getClob(final String field) {
		return (Clob) get(field);
	}

	/**
	 * 获得Blob类型结果，如果结果类型非Blob，不做转换，直接抛出异常
	 *
	 * @param field 参数
	 * @return Blob
	 * @since 3.0.6
	 */
	public Blob getBlob(final String field) {
		return (Blob) get(field);
	}

	@Override
	public Time getSqlTime(final String field, final Time defaultValue) {
		final Object obj = get(field);
		Time result = null;
		if (null != obj) {
			try {
				result = (Time) obj;
			} catch (final Exception e) {
				// try oracle.sql.TIMESTAMP
				result = MethodUtil.invoke(obj, "timeValue");
			}
		}
		return ObjUtil.defaultIfNull(result, defaultValue);
	}

	@Override
	public Date getDate(final String field, final Date defaultValue) {
		final Object obj = get(field);
		Date result = null;
		if (null != obj) {
			try {
				result = (Date) obj;
			} catch (final Exception e) {
				// try oracle.sql.TIMESTAMP
				result = MethodUtil.invoke(obj, "dateValue");
			}
		}
		return ObjUtil.defaultIfNull(result, defaultValue);
	}

	@Override
	public Timestamp getSqlTimestamp(final String field, final Timestamp defaultValue) {
		final Object obj = get(field);
		Timestamp result = null;
		if (null != obj) {
			try {
				result = (Timestamp) obj;
			} catch (final Exception e) {
				// try oracle.sql.TIMESTAMP
				result = MethodUtil.invoke(obj, "timestampValue");
			}
		}
		return ObjUtil.defaultIfNull(result, defaultValue);
	}

	@Override
	public String getStr(final String field, final String defaultValue) {
		return getStr(field, CharsetUtil.UTF_8, defaultValue);
	}

	/**
	 * 获得字符串值<br>
	 * 支持Clob、Blob、RowId
	 *
	 * @param field        字段名
	 * @param charset      编码
	 * @param defaultValue 默认值
	 * @return 字段对应值
	 */
	public String getStr(final String field, final Charset charset, final String defaultValue) {
		final Object obj = getObj(field, defaultValue);
		if (obj instanceof Clob) {
			return SqlUtil.clobToStr((Clob) obj);
		} else if (obj instanceof Blob) {
			return SqlUtil.blobToStr((Blob) obj, charset);
		} else if (obj instanceof RowId) {
			final RowId rowId = (RowId) obj;
			return StrUtil.str(rowId.getBytes(), charset);
		}
		return super.getStr(field, defaultValue);
	}

	/**
	 * 获得rowid
	 *
	 * @return RowId
	 */
	public RowId getRowId() {
		return getRowId("ROWID");
	}

	/**
	 * 获得rowid
	 *
	 * @param field rowid属性名
	 * @return RowId
	 */
	public RowId getRowId(final String field) {
		final Object obj = this.get(field);
		if (null == obj) {
			return null;
		}
		if (obj instanceof RowId) {
			return (RowId) obj;
		}
		throw new DbException("Value of field [{}] is not a rowid!", field);
	}

	// -------------------------------------------------------------------- Get end

	// -------------------------------------------------------------------- 特殊方法 start
	@Override
	public Entity clone() {
		return (Entity) super.clone();
	}
	// -------------------------------------------------------------------- 特殊方法 end

	@Override
	public String toString() {
		return "Entity {tableName=" + tableName + ", fieldNames=" + fieldNames + ", fields=" + super.toString() + "}";
	}
}
