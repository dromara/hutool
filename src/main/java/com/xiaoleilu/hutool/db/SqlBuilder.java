package com.xiaoleilu.hutool.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.StrUtil;

/**
 * SQL构建器<br>
 * 首先拼接SQL语句，值使用 ? 占位<br>
 * 调用getParamValues()方法获得占位符对应的值
 * 
 * @author Looly
 *
 */
public class SqlBuilder {
	
	/**
	 * 逻辑运算符
	 * @author Looly
	 *
	 */
	public static enum LogicalOperator{
		AND,
		OR
	}
	
	/**
	 * 排序方式（升序或者降序）
	 * @author Looly
	 *
	 */
	public static enum Order{
		/** 升序 */
		ASC,
		/** 降序 */
		DESC
	}
	
	private StringBuilder sql = new StringBuilder();
	/** 占位符对应的值列表 */
	private List<Object> paramValues = new ArrayList<Object>();
	
	/**
	 * 插入
	 * @param entity 实体
	 */
	public void insert(Entity entity){
		sql.append("INSERT INTO ").append(entity.getTableName()).append(" (");

		final StringBuilder placeHolder = new StringBuilder(") VALUES (");

		for (Entry<String, Object> entry : entity.entrySet()) {
			//非第一个参数，追加逗号
			if (paramValues.size() > 0) {
				sql.append(", ");
				placeHolder.append(", ");
			}
			
			sql.append(entry.getKey());
			placeHolder.append("?");
			paramValues.add(entry.getValue());
		}
		sql.append(placeHolder.toString()).append(")");
	}
	
	/**
	 * 删除
	 * @param tableName 表名
	 */
	public void delete(String tableName){
		sql.append("DELETE FROM ").append(tableName);
	}
	
	/**
	 * 更新
	 * @param entity 要更新的实体
	 */
	public void update(Entity entity){
		sql.append("UPDATE ").append(entity.getTableName()).append(" SET ");
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (paramValues.size() > 0) {
				sql.append(", ");
			}
			sql.append(entry.getKey()).append(" = ? ");
			paramValues.add(entry.getValue());
		}
	}
	
	/**
	 * 查询
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields 查询的字段
	 */
	public void select(boolean isDistinct, String... fields){
		sql.append("SELECT ");
		if(isDistinct) {
			sql.append("DISTINCT ");
		}
		
		if (CollectionUtil.isEmpty(fields)) {
			sql.append("*");
		} else {
			sql.append(CollectionUtil.join(fields, StrUtil.COMMA));
		}
	}
	
	/**
	 * 添加 from语句
	 * @param tableName 表名列表（多个表名用于多表查询）
	 */
	public void from(String... tableNames){
		sql.append(" FROM ").append(CollectionUtil.join(tableNames, StrUtil.COMMA));
	}
	
	/**
	 * 添加Where语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件
	 */
	public void where(LogicalOperator logicalOperator, Condition... conditions){
		StringBuilder conditionStr = new StringBuilder();
		boolean isFirst = true;
		for (Condition condition : conditions) {
			//添加逻辑运算符
			if(isFirst){
				isFirst = false;
			}else {
				conditionStr.append(StrUtil.SPACE).append(logicalOperator).append(StrUtil.SPACE);
			}
			
			//添加条件表达式
			conditionStr.append(condition.getField()).append(StrUtil.SPACE).append(condition.getOperator()).append(" ?");
			paramValues.add(condition.getValue());
		}
	}
	
	/**
	 * 分组
	 * @param field 字段
	 */
	public void groupBy(String... field){
		sql.append(" GROUP BY ").append(CollectionUtil.join(field, StrUtil.COMMA));
	}
	
	/**
	 * 添加Having语句
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件
	 */
	public void having(LogicalOperator logicalOperator, Condition... conditions){
		sql.append(" HAVING ").append(CollectionUtil.join(conditions, StrUtil.SPACE + logicalOperator + StrUtil.SPACE));
	}
	
	/**
	 * 排序
	 * @param field 按照哪个字段排序
	 * @param order 排序方式（升序还是降序）
	 */
	public void orderBy(Order order, String... fields){
		sql.append(" ORDER BY ").append(CollectionUtil.join(fields, StrUtil.COMMA)).append(StrUtil.SPACE).append(order);
	}
	
	/**
	 * 获得占位符对应的值列表
	 * @return 占位符对应的值列表
	 */
	public List<Object> getParamValues() {
		return paramValues;
	}
	
	@Override
	public String toString() {
		return this.sql.toString();
	}
	
}
