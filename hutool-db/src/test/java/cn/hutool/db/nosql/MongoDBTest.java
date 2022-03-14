package cn.hutool.db.nosql;

import cn.hutool.db.nosql.mongo.MongoFactory;
import com.mongodb.client.MongoDatabase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author VampireAchao
 */
public class MongoDBTest {

	@Test
	@Ignore
	public void mongoDSTest() {
		MongoDatabase db = MongoFactory.getDS("master").getDb("test");
		Assert.assertEquals("test", db.getName());
	}
}
