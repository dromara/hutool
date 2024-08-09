package cn.hutool.db.nosql;

import cn.hutool.db.nosql.mongo.MongoFactory;
import com.mongodb.client.MongoDatabase;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 */
public class MongoDBTest {

	@Test
	@Disabled
	public void mongoDSTest() {
		MongoDatabase db = MongoFactory.getDS("master").getDb("test");
		assertEquals("test", db.getName());
	}
}
