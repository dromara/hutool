package cn.hutool.db;

import cn.hutool.db.sql.SqlLog;
import cn.hutool.log.level.Level;

/**
 * DB全局配置配置项
 *
 * @author looly
 * @since 5.3.10
 */
public class GlobalDbConfig {
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected static boolean caseInsensitive = true;
	/**
	 * 是否INSERT语句中默认返回主键（默认返回主键）
	 */
	protected static boolean returnGeneratedKey = true;

	/**
	 * 设置全局是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param isCaseInsensitive 否在结果中忽略大小写
	 */
	public static void setCaseInsensitive(boolean isCaseInsensitive) {
		caseInsensitive = isCaseInsensitive;
	}

	/**
	 * 设置全局是否INSERT语句中默认返回主键（默认返回主键）<br>
	 * 如果false，则在Insert操作后，返回影响行数
	 * 主要用于某些数据库不支持返回主键的情况
	 *
	 * @param isReturnGeneratedKey 是否INSERT语句中默认返回主键
	 */
	public static void setReturnGeneratedKey(boolean isReturnGeneratedKey) {
		returnGeneratedKey = isReturnGeneratedKey;
	}

	/**
	 * 设置全局配置：是否通过debug日志显示SQL
	 *
	 * @param isShowSql    是否显示SQL
	 * @param isFormatSql  是否格式化显示的SQL
	 * @param isShowParams 是否打印参数
	 * @param level        SQL打印到的日志等级
	 */
	public static void setShowSql(boolean isShowSql, boolean isFormatSql, boolean isShowParams, Level level) {
		SqlLog.INSTANCE.init(isShowSql, isFormatSql, isShowParams, level);
	}
}
