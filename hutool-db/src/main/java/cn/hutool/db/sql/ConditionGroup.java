package cn.hutool.db.sql;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;

/**
 * 条件组<br>
 * 用于构建复杂where条件
 *
 * @author tjh
 * @since 5.7.21
 */
public class ConditionGroup extends Condition {
	/**
	 * 条件列表
	 */
	private Condition[] conditions;

	/**
	 * 追加条件
	 *
	 * @param conditions 条件列表
	 */
	public void addConditions(Condition... conditions) {
		if (null == this.conditions) {
			this.conditions = conditions;
		} else {
			this.conditions = ArrayUtil.addAll(this.conditions, conditions);
		}
	}

	/**
	 * 将条件组转换为条件字符串，使用括号包裹，并回填占位符对应的参数值
	 *
	 * @param paramValues 参数列表，用于回填占位符对应参数值
	 * @return 条件字符串
	 */
	@Override
	public String toString(List<Object> paramValues) {
		if (ArrayUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder conditionStrBuilder = StrUtil.builder();
		conditionStrBuilder.append("(");
		// 将组内的条件构造为SQL，因为toString，会进行递归，处理所有的条件组
		conditionStrBuilder.append(ConditionBuilder.of(this.conditions).build(paramValues));
		conditionStrBuilder.append(")");

		return conditionStrBuilder.toString();
	}
}
