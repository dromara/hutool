package cn.hutool.db;

import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.map.MapUtil;

import java.util.Collection;

/**
 * 动态实体类<br>
 * 提供了针对自身实体的增删改方法
 *
 * @author Looly
 *
 */
public class ActiveEntity extends Entity {
	private static final long serialVersionUID = 6112321379601134750L;

	private final Db db;

	// --------------------------------------------------------------- Static method start
	/**
	 * 创建ActiveEntity
	 *
	 * @return ActiveEntity
	 */
	public static ActiveEntity create() {
		return new ActiveEntity();
	}

	/**
	 * 创建ActiveEntity
	 *
	 * @param tableName 表名
	 * @return ActiveEntity
	 */
	public static ActiveEntity create(final String tableName) {
		return new ActiveEntity(tableName);
	}

	/**
	 * 将PO对象转为Entity
	 *
	 * @param <T> Bean对象类型
	 * @param bean Bean对象
	 * @return ActiveEntity
	 */
	public static <T> ActiveEntity parse(final T bean) {
		return create(null).parseBean(bean);
	}

	/**
	 * 将PO对象转为ActiveEntity
	 *
	 * @param <T> Bean对象类型
	 * @param bean Bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @return ActiveEntity
	 */
	public static <T> ActiveEntity parse(final T bean, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		return create(null).parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 将PO对象转为ActiveEntity,并采用下划线法转换字段
	 *
	 * @param <T> Bean对象类型
	 * @param bean Bean对象
	 * @return ActiveEntity
	 */
	public static <T> ActiveEntity parseWithUnderlineCase(final T bean) {
		return create(null).parseBean(bean, true, true);
	}
	// --------------------------------------------------------------- Static method end

	// -------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public ActiveEntity() {
		this(Db.of(), (String) null);
	}

	/**
	 * 构造
	 *
	 * @param tableName 表名
	 */
	public ActiveEntity(final String tableName) {
		this(Db.of(), tableName);
	}

	/**
	 * 构造
	 *
	 * @param entity 非动态实体
	 */
	public ActiveEntity(final Entity entity) {
		this(Db.of(), entity);
	}

	/**
	 * 构造
	 *
	 * @param db {@link Db}
	 * @param tableName 表名
	 */
	public ActiveEntity(final Db db, final String tableName) {
		super(tableName);
		this.db = db;
	}

	/**
	 * 构造
	 *
	 * @param db {@link Db}
	 * @param entity 非动态实体
	 */
	public ActiveEntity(final Db db, final Entity entity) {
		super(entity.getTableName());
		this.putAll(entity);
		this.db = db;
	}
	// -------------------------------------------------------------------------- Constructor end

	@Override
	public ActiveEntity setTableName(final String tableName) {
		return (ActiveEntity) super.setTableName(tableName);
	}

	@Override
	public ActiveEntity setFieldNames(final Collection<String> fieldNames) {
		return (ActiveEntity) super.setFieldNames(fieldNames);
	}

	@Override
	public ActiveEntity setFieldNames(final String... fieldNames) {
		return (ActiveEntity) super.setFieldNames(fieldNames);
	}

	/**
	 * 通过lambda批量设置值
	 * @param fields lambda,不能为空
	 * @return this
	 */
	@Override
	public ActiveEntity setFields(final Func0<?>... fields) {
		return (ActiveEntity) super.setFields(fields);
	}

	@Override
	public ActiveEntity addFieldNames(final String... fieldNames) {
		return (ActiveEntity) super.addFieldNames(fieldNames);
	}

	@Override
	public <T> ActiveEntity parseBean(final T bean) {
		return (ActiveEntity) super.parseBean(bean);
	}

	@Override
	public <T> ActiveEntity parseBean(final T bean, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		return (ActiveEntity) super.parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}

	@Override
	public ActiveEntity set(final String field, final Object value) {
		return (ActiveEntity) super.set(field, value);
	}

	@Override
	public ActiveEntity setIgnoreNull(final String field, final Object value) {
		return (ActiveEntity) super.setIgnoreNull(field, value);
	}

	@Override
	public ActiveEntity clone() {
		return (ActiveEntity) super.clone();
	}

	// -------------------------------------------------------------------------- CRUD start
	/**
	 * 根据Entity中现有字段条件从数据库中增加一条数据
	 *
	 * @return this
	 */
	public ActiveEntity add() {
		db.insert(this);
		return this;
	}

	/**
	 * 根据Entity中现有字段条件从数据库中加载一个Entity对象
	 *
	 * @return this
	 */
	public ActiveEntity load() {
		final Entity result = db.get(this);
		if(MapUtil.isNotEmpty(result)) {
			this.putAll(result);
		}
		return this;
	}

	/**
	 * 根据现有Entity中的条件删除与之匹配的数据库记录
	 *
	 * @return this
	 */
	public ActiveEntity del() {
		db.del(this);
		return this;
	}

	/**
	 * 根据现有Entity中的条件删除与之匹配的数据库记录
	 *
	 * @param primaryKey 主键名
	 * @return this
	 */
	public ActiveEntity update(final String primaryKey) {
		db.update(this, Entity.create().set(primaryKey, this.get(primaryKey)));
		return this;
	}
	// -------------------------------------------------------------------------- CRUD end
}
