package cn.hutool.db.sql;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 多条件构建封装<br>
 * 可以将多个条件构建为SQL语句的一部分，并将参数值转换为占位符，并提取对应位置的参数值。<br>
 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
 *
 * @author looly
 * @since 5.4.3
 */
public class ConditionBuilder implements Builder<String> {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建构建器
	 *
	 * @param conditions 条件列表
	 * @return {@link ConditionBuilder}
	 */
	public static ConditionBuilder of(Condition... conditions) {
		return new ConditionBuilder(conditions);
	}

	/**
	 * 条件数组
	 */
	private final Condition[] conditions;
	/**
	 * 占位符对应的值列表
	 */
	private List<Object> paramValues;

	/**
	 * 构造
	 *
	 * @param conditions 条件列表
	 */
	public ConditionBuilder(Condition... conditions) {
		this.conditions = conditions;
	}

	/**
	 * 返回构建后的参数列表<br>
	 * 此方法调用前必须调用{@link #build()}
	 *
	 * @return 参数列表
	 */
	public List<Object> getParamValues() {
		return ListUtil.unmodifiable(this.paramValues);
	}

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @return 构建后的SQL语句条件部分
	 */
	@Override
	public String build() {
		if(null == this.paramValues){
			this.paramValues = new ArrayList<>();
		} else {
			this.paramValues.clear();
		}
		return build(this.paramValues);
	}

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @param paramValues 用于写出参数的List,构建时会将参数写入此List
	 * @return 构建后的SQL语句条件部分
	 */
	public String build(List<Object> paramValues) {
		if (ArrayUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder conditionStrBuilder = new StringBuilder();
		boolean isFirst = true;
		for (Condition condition : conditions) {
			// 添加逻辑运算符
			if (isFirst) {
				isFirst = false;
			} else {
				// " AND " 或者 " OR "
				conditionStrBuilder.append(CharUtil.SPACE).append(condition.getLinkOperator()).append(CharUtil.SPACE);
			}

			// 构建条件部分："name = ?"、"name IN (?,?,?)"、"name BETWEEN ？AND ？"、"name LIKE ?"
			conditionStrBuilder.append(condition.toString(paramValues));
		}
		return conditionStrBuilder.toString();
	}

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @return 构建后的SQL语句条件部分
	 */
	@Override
	public String toString() {
		return build();
	}
}
