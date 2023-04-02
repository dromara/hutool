package org.dromara.hutool.db.ds;

import org.dromara.hutool.db.ds.simple.SimpleDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataSourceWrapperTest {

	@Test
	public void cloneTest(){
		final SimpleDataSource simpleDataSource = new SimpleDataSource("jdbc:sqlite:test.db", "", "");
		final DSWrapper wrapper = new DSWrapper(simpleDataSource, "test.driver");

		final DSWrapper clone = wrapper.clone();
		Assertions.assertEquals("test.driver", clone.getDriver());
		Assertions.assertEquals(simpleDataSource, clone.getRaw());
	}
}
