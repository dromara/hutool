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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.log.Log;
import org.dromara.hutool.log.level.Level;

/**
 * SQL在日志中打印配置
 *
 * @author looly
 * @since 4.1.0
 */
public class SqlLog {

	private final static Log log = Log.get();

	/**
	 * 是否debugSQL
	 */
	private boolean showSql;
	/**
	 * 是否格式化SQL
	 */
	private boolean formatSql;
	/**
	 * 是否显示参数
	 */
	private boolean showParams;
	/**
	 * 默认日志级别
	 */
	private Level level = Level.DEBUG;

	/**
	 * 设置全局配置：是否通过debug日志显示SQL
	 *
	 * @param isShowSql    是否显示SQL，{@code null}表示保持默认
	 * @param isFormatSql  是否格式化显示的SQL，{@code null}表示保持默认
	 * @param isShowParams 是否打印参数，{@code null}表示保持默认
	 * @param level        日志级别，{@code null}表示保持默认
	 */
	public void init(final Boolean isShowSql, final Boolean isFormatSql, final Boolean isShowParams, final Level level) {
		if (null != isShowSql) {
			this.showSql = isShowSql;
		}
		if (null != isFormatSql) {
			this.formatSql = isFormatSql;
		}
		if (null != isShowParams) {
			this.showParams = isShowParams;
		}
		if (null != level) {
			this.level = level;
		}
		log.debug("Show sql: [{}], format sql: [{}], show params: [{}], level: [{}]",
			this.showSql, this.formatSql, this.showParams, this.level);
	}

	/**
	 * 打印SQL日志
	 *
	 * @param sql SQL语句
	 * @since 4.6.7
	 */
	public void log(final String sql) {
		log(sql, null);
	}

	/**
	 * 打印批量 SQL日志
	 *
	 * @param sql SQL语句
	 * @since 4.6.7
	 */
	public void logForBatch(final String sql) {
		if (this.showSql) {
			log.log(this.level, "\n[Batch SQL] -> {}", this.formatSql ? SqlFormatter.format(sql) : sql);
		}
	}

	/**
	 * 打印SQL日志
	 *
	 * @param sql         SQL语句
	 * @param paramValues 参数，可为null
	 */
	public void log(final String sql, final Object paramValues) {
		if (this.showSql) {
			if (null != paramValues && this.showParams) {
				log.log(this.level, "\n[SQL] -> {}\nParams -> {}", this.formatSql ? SqlFormatter.format(sql) : sql, paramValues);
			} else {
				log.log(this.level, "\n[SQL] -> {}", this.formatSql ? SqlFormatter.format(sql) : sql);
			}
		}
	}
}
