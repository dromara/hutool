package cn.hutool.db.dialect.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL方言
 * @author loolly
 *
 */
public class MysqlDialect extends AnsiSqlDialect{
	private static final long serialVersionUID = -3734718212043823636L;

	public MysqlDialect() {
		wrapper = new Wrapper('`');
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		return find.append(" LIMIT ").append(page.getStartPosition()).append(", ").append(page.getPageSize());
	}

	@Override
	public String dialectName() {
		return DialectName.MYSQL.toString();
	}

	/**
	 * 构建用于upsert的{@link PreparedStatement}<br>
	 * MySQL通过主键方式实现Upsert，故keys无效，生成SQL语法为：
	 * <pre>
	 *     INSERT INTO demo(a,b,c) values(?, ?, ?) ON DUPLICATE KEY UPDATE a=values(a), b=values(b), c=values(c);
	 * </pre>
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   此参数无效
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 * @since 5.7.20
	 */
	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.create(wrapper);

		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();
		final StringBuilder updateHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value)->{
			if (StrUtil.isNotBlank(field)) {
				if (fieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
					updateHolder.append(", ");
				}

				field = (null != wrapper) ? wrapper.wrap(field) : field;
				fieldsPart.append(field);
				updateHolder.append(field).append("=values(").append(field).append(")");
				placeHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.wrapper) {
			tableName = this.wrapper.wrap(tableName);
		}
		builder.append("INSERT INTO ").append(tableName)
				// 字段列表
				.append(" (").append(fieldsPart)
				// 更新值列表
				.append(") VALUES (").append(placeHolder)
				// 主键冲突后的更新操作
				.append(") ON DUPLICATE KEY UPDATE ").append(updateHolder);

		return StatementUtil.prepareStatement(conn, builder);
	}
}
