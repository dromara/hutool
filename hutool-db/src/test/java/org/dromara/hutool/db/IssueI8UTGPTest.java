package org.dromara.hutool.db;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.config.SettingConfigParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class IssueI8UTGPTest {

	@Test
	void customSqlLogTest() {
		final AtomicBoolean isFilterValid = new AtomicBoolean(false);

		final DbConfig dbConfig = DbConfig.of()
			.setUrl("jdbc:sqlite:test.db")
			.addSqlFilter((conn, boundSql, returnGeneratedKey) -> {
				isFilterValid.set(true);
				Console.log("Custom log: {}", boundSql.getSql());
			});

		final Db db = Db.of(dbConfig);

		final List<Entity> find = db.query("select * from user where age = ?", 18);
		Assertions.assertEquals("王五", find.get(0).get("name"));

		Assertions.assertTrue(isFilterValid.get());
	}

	@Test
	void customSqlLogTest2() {
		final AtomicBoolean isFilterValid = new AtomicBoolean(false);

		final DbConfig dbConfig = SettingConfigParser.of().parse("test");
		dbConfig.addSqlFilter((conn, boundSql, returnGeneratedKey) -> {
				isFilterValid.set(true);
				Console.log("Custom log: {}", boundSql.getSql());
			});

		final Db db = Db.of(dbConfig);

		final List<Entity> find = db.query("select * from user where age = ?", 18);
		Assertions.assertEquals("王五", find.get(0).get("name"));

		Assertions.assertTrue(isFilterValid.get());
	}
}
