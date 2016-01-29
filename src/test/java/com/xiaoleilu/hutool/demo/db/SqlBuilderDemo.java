package com.xiaoleilu.hutool.demo.db;

import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.sql.Condition;
import com.xiaoleilu.hutool.db.sql.Order;
import com.xiaoleilu.hutool.db.sql.SqlBuilder;
import com.xiaoleilu.hutool.db.sql.SqlBuilder.Direction;
import com.xiaoleilu.hutool.db.sql.SqlBuilder.Join;
import com.xiaoleilu.hutool.db.sql.SqlBuilder.LogicalOperator;
import com.xiaoleilu.hutool.db.sql.Wrapper;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * SQL构建器样例
 * @author Looly
 *
 */
public class SqlBuilderDemo {
	private final static Log log = StaticLog.get();
	
	public static void main(String[] args) {
		//主要用于字段名的包装（在字段名的前后加字符，例如反引号来避免与数据库的关键字冲突）
		//例如原字段：name，包装后就是 `name`
		Wrapper wrapper = new Wrapper('`');
		
		
		Entity user = Entity.create("User");
		user
			.set("name", "Joe")
			.set("age", 22)
			.set("gender", "男");
		
		//有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
		
		Condition condition1 = new Condition("`age`", ">", 12);
		Condition condition2 = new Condition("gender", "=", "男");
		
		//join on的条件，不使用条件表达式占位符
		Condition condition3 = new Condition("User.name", "=", "User2.name");
		condition3.setPlaceHolder(false);
		
		//Having语句的条件
		Condition havingCondition = new Condition("sum(age)", ">", 100);
		
		//build insert
		SqlBuilder insert = SqlBuilder.create(wrapper).insert(user);
		log.debug("Insert SQL: {}, paramValues: {}", insert.build(), insert.getParamValues());
		
		//build delete
		SqlBuilder delete = SqlBuilder.create(wrapper)
				.delete("User")
				.where(LogicalOperator.AND, condition1);
		log.debug("Delete SQL: {}, paramValues: {}", delete.build(), delete.getParamValues());
		
		//build update
		SqlBuilder update = SqlBuilder.create(wrapper)
				.update(user)
				.where(LogicalOperator.AND, condition2);
		log.debug("Update SQL: {}, paramValues: {}", update.build(), update.getParamValues());
		
		
		//build select，条件查询
		SqlBuilder select = SqlBuilder.create(wrapper)
				.select("name", "age", "gender")
				.from("User")
				.where(LogicalOperator.AND, condition1, condition2);
		log.debug("Select SQL: {}, paramValues: {}", select.build(), select.getParamValues());
		
		//build select，查询全部字段
		SqlBuilder select2 = SqlBuilder.create(wrapper)
				.select()
				.from("User");
		log.debug("Select SQL: {}, paramValues: {}", select2.build(), select2.getParamValues());
		
		//build select，查询全部字段并排序
		SqlBuilder select3 = SqlBuilder.create(wrapper)
				.select()
				.from("User")
				.orderBy(new Order("name", Direction.DESC));
		log.debug("Select SQL: {}, paramValues: {}", select3.build(), select3.getParamValues());
		
		//build select，查询多表
		SqlBuilder select4 = SqlBuilder.create(wrapper)
				.select("name", "age")
				.from("User")
				.join("User2", Join.INNER)
				.on(LogicalOperator.AND, condition3);
		log.debug("Select SQL: {}, paramValues: {}", select4.build(), select4.getParamValues());
	
		//build select，查询多表
		SqlBuilder select5 = SqlBuilder.create(wrapper)
				.select("name", "sum(age)")
				.from("User")
				.groupBy("gender")
				.having(LogicalOperator.AND, havingCondition);
		log.debug("Select SQL: {}, paramValues: {}", select5.build(), select5.getParamValues());
	}
}
