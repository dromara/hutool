package cn.hutool.db;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.db.handler.EntityListHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * SqlRunner线程安全测试
 *
 * @author looly
 *
 */
@Ignore
public class ConcurentTest {

	private Db db;

	@Before
	public void init() {
		db = Db.of("test");
	}

	@Test
	public void findTest() {
		for(int i = 0; i < 10000; i++) {
			ThreadUtil.execute(() -> {
				final List<Entity> find;
				find = db.find(ListUtil.of("name AS name2"), Entity.of("user"), new EntityListHandler());
				Console.log(find);
			});
		}

		//主线程关闭会导致连接池销毁，sleep避免此情况引起的问题
		ThreadUtil.sleep(5000);
	}
}
