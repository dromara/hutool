package cn.hutool.db;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlUtil;

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
	public static Entity create() {
		return new Entity();
	}

	/**
	 * 创建Entity
	 *
	 * @param tableName 表名
	 * @return Entity
	 */
	public static Entity create(String tableName) {
		return new Entity(tableName);
	}

	/**
	 * 将PO对象转为Entity
	 *
	 * @param <T>  Bean对象类型
	 * @param bean Bean对象
	 * @return Entity
	 */
	public static <T> Entity parse(T bean) {
		return create(null).parseBean(bean);
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
	public static <T> Entity parse(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		return create(null).parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 将PO对象转为Entity,并采用下划线法转换字段
	 *
	 * @param <T>  Bean对象类型
	 * @param bean Bean对象
	 * @return Entity
	 */
	public static <T> Entity parseWithUnderlineCase(T bean) {
		return create(null).parseBean(bean, true, true);
	}
	// --------------------------------------------------------------- Static method end

	/* 表名 */
	private String tableName;
	/* 字段名列表，用于限制加入的字段的值 */
	private Set<String> fieldNames;

	// --------------------------------------------------------------- Constructor start
	public Entity() {
	}

	/**
	 * 构造
	 *
	 * @param tableName 数据表名
	 */

	public Entity(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 构造
	 *
	 * @param tableName       数据表名
	 * @param caseInsensitive 是否大小写不敏感
	 * @since 4.5.16
	 */
	public Entity(String tableName, boolean caseInsensitive) {
		super(caseInsensitive);
		this.tableName = tableName;
	}
	// --------------------------------------------------------------- Constructor end

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
	public Entity setTableName(String tableName) {
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
	public Entity setFieldNames(Collection<String> fieldNames) {
		if (CollectionUtil.isNotEmpty(fieldNames)) {
			this.fieldNames = CollectionUtil.newHashSet(true, fieldNames);
		}
		return this;
	}

	/**
	 * 设置字段列表，用于限制加入的字段的值
	 *
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(String... fieldNames) {
		if (ArrayUtil.isNotEmpty(fieldNames)) {
			this.fieldNames = CollectionUtil.newLinkedHashSet(fieldNames);
		}
		return this;
	}

	/**
	 * 添加字段列表
	 *
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity addFieldNames(String... fieldNames) {
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
	public <T> Entity parseBean(T bean) {
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
	public <T> Entity parseBean(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		if (StrUtil.isBlank(this.tableName)) {
			String simpleName = bean.getClass().getSimpleName();
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
	public Entity filter(String... keys) {
		final Entity result = new Entity(this.tableName);
		result.setFieldNames(this.fieldNames);

		for (String key : keys) {
			if (this.containsKey(key)) {
				result.put(key, this.get(key));
			}
		}
		return result;
	}

	// -------------------------------------------------------------------- Put and Set start
	@Override
	public Entity set(String field, Object value) {
		return (Entity) super.set(field, value);
	}

	@Override
	public Entity setIgnoreNull(String field, Object value) {
		return (Entity) super.setIgnoreNull(field, value);
	}
	// -------------------------------------------------------------------- Put and Set end

	// -------------------------------------------------------------------- Get start

	/**
	 * 获得Clob类型结果
	 *
	 * @param field 参数
	 * @return Clob
	 */
	public Clob getClob(String field) {
		return get(field, null);
	}

	/**
	 * 获得Blob类型结果
	 *
	 * @param field 参数
	 * @return Blob
	 * @since 3.0.6
	 */
	public Blob getBlob(String field) {
		return get(field, null);
	}

	@Override
	public Time getTime(String field) {
		Object obj = get(field);
		Time result = null;
		if (null != obj) {
			try {
				result = (Time) obj;
			} catch (Exception e) {
				// try oracle.sql.TIMESTAMP
				result = ReflectUtil.invoke(obj, "timeValue");
			}
		}
		return result;
	}

	@Override
	public Date getDate(String field) {
		Object obj = get(field);
		Date result = null;
		if (null != obj) {
			try {
				result = (Date) obj;
			} catch (Exception e) {
				// try oracle.sql.TIMESTAMP
				result = ReflectUtil.invoke(obj, "dateValue");
			}
		}
		return result;
	}

	@Override
	public Timestamp getTimestamp(String field) {
		Object obj = get(field);
		Timestamp result = null;
		if (null != obj) {
			try {
				result = (Timestamp) obj;
			} catch (Exception e) {
				// try oracle.sql.TIMESTAMP
				result = ReflectUtil.invoke(obj, "timestampValue");
			}
		}
		return result;
	}

	@Override
	public String getStr(String field) {
		return getStr(field, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得字符串值<br>
	 * 支持Clob、Blob、RowId
	 *
	 * @param field   字段名
	 * @param charset 编码
	 * @return 字段对应值
	 * @since 3.0.6
	 */
	public String getStr(String field, Charset charset) {
		final Object obj = get(field);
		if (obj instanceof Clob) {
			return SqlUtil.clobToStr((Clob) obj);
		} else if (obj instanceof Blob) {
			return SqlUtil.blobToStr((Blob) obj, charset);
		} else if (obj instanceof RowId) {
			final RowId rowId = (RowId) obj;
			return StrUtil.str(rowId.getBytes(), charset);
		}
		return super.getStr(field);
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
	public RowId getRowId(String field) {
		Object obj = this.get(field);
		if (null == obj) {
			return null;
		}
		if (obj instanceof RowId) {
			return (RowId) obj;
		}
		throw new DbRuntimeException("Value of field [{}] is not a rowid!", field);
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
