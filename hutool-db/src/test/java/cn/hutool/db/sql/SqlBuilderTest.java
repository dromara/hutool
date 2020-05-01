package cn.hutool.db.sql;

import org.junit.Assert;
import org.junit.Test;

public class SqlBuilderTest {
	
	@Test
	public void queryNullTest() {
		SqlBuilder builder = SqlBuilder.create().select().from("user").where(new Condition("name", "= null"));
		Assert.assertEquals("SELECT * FROM user WHERE name IS NULL", builder.build());
		
		SqlBuilder builder2 = SqlBuilder.create().select().from("user").where(new Condition("name", "is null"));
		Assert.assertEquals("SELECT * FROM user WHERE name IS NULL", builder2.build());
		
		SqlBuilder builder3 = SqlBuilder.create().select().from("user").where(LogicalOperator.AND, new Condition("name", "!= null"));
		Assert.assertEquals("SELECT * FROM user WHERE name IS NOT NULL", builder3.build());
		
		SqlBuilder builder4 = SqlBuilder.create().select().from("user").where(LogicalOperator.AND, new Condition("name", "is not null"));
		Assert.assertEquals("SELECT * FROM user WHERE name IS NOT NULL", builder4.build());
	}

	@Test
	public void orderByTest(){
		SqlBuilder builder = SqlBuilder.create().select("id", "username").from("user")
				.join("role", SqlBuilder.Join.INNER)
				.on("user.id = role.user_id")
				.where(new Condition("age", ">=", 18),
						new Condition("username", "abc", Condition.LikeType.Contains)
				).orderBy(new Order("id"));

		Assert.assertEquals("SELECT id,username FROM user INNER JOIN role ON user.id = role.user_id WHERE age >= ? AND username LIKE ? ORDER BY id", builder.build());
	}
}
