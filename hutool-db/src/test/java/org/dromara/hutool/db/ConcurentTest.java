package org.dromara.hutool.db;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.db.handler.EntityListHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * SqlRunner线程安全测试
 *
 * @author looly
 *
 */
@Disabled
public class ConcurentTest {

	private Db db;

	@BeforeEach
	public void init() {
		db = Db.of("test");
	}

	@Test
	public void findTest() {
		for(int i = 0; i < 10000; i++) {
			ThreadUtil.execute(() -> {
				final List<Entity> find;
				find = db.find(ListUtil.of("name AS name2"), Entity.of("user"), EntityListHandler.of());
				Console.log(find);
			});
		}

		//主线程关闭会导致连接池销毁，sleep避免此情况引起的问题
		ThreadUtil.sleep(5000);
	}
}
