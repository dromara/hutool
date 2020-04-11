package cn.hutool.db.sql;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 使用命名占位符的SQL，例如：select * from table where field1=:name1<br>
 * 支持的占位符格式为：
 * <pre>
 * 1、:name
 * 2、?name
 * 3、@name
 * </pre>
 * 
 * @author looly
 * @since 4.0.10
 */
public class NamedSql {

	private String sql;
	private final List<Object> params;

	/**
	 * 构造
	 * 
	 * @param namedSql 命名占位符的SQL
	 * @param paramMap 名和参数的对应Map
	 */
	public NamedSql(String namedSql, Map<String, Object> paramMap) {
		this.params = new LinkedList<>();
		parse(namedSql, paramMap);
	}

	/**
	 * 获取SQL
	 * 
	 * @return SQL
	 */
	public String getSql() {
		return this.sql;
	}
	
	/**
	 * 获取参数列表，按照占位符顺序
	 * 
	 * @return 参数数组
	 */
	public Object[] getParams() {
		return this.params.toArray(new Object[0]);
	}

	/**
	 * 获取参数列表，按照占位符顺序
	 * 
	 * @return 参数列表
	 */
	public List<Object> getParamList() {
		return this.params;
	}

	/**
	 * 解析命名占位符的SQL
	 * 
	 * @param namedSql 命名占位符的SQL
	 * @param paramMap 名和参数的对应Map
	 */
	private void parse(String namedSql, Map<String, Object> paramMap) {
		int len = namedSql.length();

		final StrBuilder name = StrUtil.strBuilder();
		final StrBuilder sqlBuilder = StrUtil.strBuilder();
		char c;
		Character nameStartChar = null;
		for (int i = 0; i < len; i++) {
			c = namedSql.charAt(i);
			if (c == ':' || c == '@' || c == '?') {
				nameStartChar = c;
			} else if (null != nameStartChar) {
				// 变量状态
				if (isGenerateChar(c)) {
					// 变量名
					name.append(c);
				} else {
					// 变量结束
					String nameStr = name.toString();
					if(paramMap.containsKey(nameStr)) {
						// 有变量对应值（值可以为null），替换占位符
						final Object paramValue = paramMap.get(nameStr);
						sqlBuilder.append('?');
						this.params.add(paramValue);
					} else {
						// 无变量对应值，原样输出
						sqlBuilder.append(nameStartChar).append(name);
					}
					nameStartChar = null;
					name.clear();
					sqlBuilder.append(c);
				}
			} else {
				sqlBuilder.append(c);
			}
		}

		if (false == name.isEmpty()) {
			// SQL结束依旧有变量名存在，说明变量位于末尾
			final Object paramValue = paramMap.get(name.toString());
			if (null != paramValue) {
				// 有变量对应值，替换占位符
				sqlBuilder.append('?');
				this.params.add(paramValue);
			} else {
				// 无变量对应值，原样输出
				sqlBuilder.append(nameStartChar).append(name);
			}
			name.clear();
		}

		this.sql = sqlBuilder.toString();
	}

	/**
	 * 是否为标准的字符，包括大小写字母、下划线和数字
	 * 
	 * @param c 字符
	 * @return 是否标准字符
	 */
	private static boolean isGenerateChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || (c >= '0' && c <= '9');
	}
}
