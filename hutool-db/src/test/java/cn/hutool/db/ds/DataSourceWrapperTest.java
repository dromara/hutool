package cn.hutool.db.ds;

import cn.hutool.db.ds.simple.SimpleDataSource;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DataSourceWrapperTest {

	@Test
	public void cloneTest(){
		final SimpleDataSource simpleDataSource = new SimpleDataSource("jdbc:sqlite:test.db", "", "");
		final DataSourceWrapper wrapper = new DataSourceWrapper(simpleDataSource, "test.driver");

		final DataSourceWrapper clone = wrapper.clone();
		assertEquals("test.driver", clone.getDriver());
		assertEquals(simpleDataSource, clone.getRaw());
	}
}
