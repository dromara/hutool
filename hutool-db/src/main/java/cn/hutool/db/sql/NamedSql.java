package cn.hutool.db.sql;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 使用命名占位符的SQL，例如：select * from table where field1=:name1<br>
 * 支持的占位符格式为：
 * <pre>
 * 1、:name
 * 2、@name
 * 3、?name
 * </pre>
 * 
 * @author looly
 * @since 4.0.10
 */
public class NamedSql {

	private static final char[] NAME_START_CHARS = {':', '@', '?'};

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
		if(MapUtil.isEmpty(paramMap)){
			this.sql = namedSql;
			return;
		}

		int len = namedSql.length();

		final StrBuilder name = StrUtil.strBuilder();
		final StrBuilder sqlBuilder = StrUtil.strBuilder();
		char c;
		Character nameStartChar = null;
		for (int i = 0; i < len; i++) {
			c = namedSql.charAt(i);
			if (ArrayUtil.contains(NAME_START_CHARS, c)) {
				nameStartChar = c;

				// 新的变量开始符出现，要处理之前的变量
				replaceVar(nameStartChar, name, sqlBuilder, paramMap);
			} else if (null != nameStartChar) {
				// 变量状态
				if (isGenerateChar(c)) {
					// 变量名
					name.append(c);
				} else {
					// 非标准字符也非变量开始的字符出现表示变量名结束，开始替换
					replaceVar(nameStartChar, name, sqlBuilder, paramMap);
					nameStartChar = null;
					sqlBuilder.append(c);
				}
			} else {
				// 变量以外的字符原样输出
				sqlBuilder.append(c);
			}
		}

		// 收尾，如果SQL末尾存在变量，处理之
		if (false == name.isEmpty()) {
			replaceVar(nameStartChar, name, sqlBuilder, paramMap);
		}

		this.sql = sqlBuilder.toString();
	}

	/**
	 * 替换变量，如果无变量，原样输出到SQL中去
	 *
	 * @param nameStartChar 变量开始字符
	 * @param name 变量名
	 * @param sqlBuilder 结果SQL缓存
	 * @param paramMap 变量map（非空）
	 */
	private void replaceVar(Character nameStartChar, StrBuilder name, StrBuilder sqlBuilder, Map<String, Object> paramMap){
		if(name.isEmpty()){
			// 无变量，按照普通字符处理
			return;
		}

		// 变量结束
		final String nameStr = name.toString();
		if(paramMap.containsKey(nameStr)) {
			// 有变量对应值（值可以为null），替换占位符为?，变量值放入相应index位置
			final Object paramValue = paramMap.get(nameStr);
			sqlBuilder.append('?');
			this.params.add(paramValue);
		} else {
			// 无变量对应值，原样输出
			sqlBuilder.append(nameStartChar).append(name);
		}

		//清空变量，表示此变量处理结束
		name.clear();
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
