package cn.hutool.db.ds;

import cn.hutool.db.ds.simple.SimpleDataSource;
import org.junit.Assert;
import org.junit.Test;

public class DataSourceWrapperTest {

	@Test
	public void cloneTest(){
		final SimpleDataSource simpleDataSource = new SimpleDataSource("jdbc:sqlite:test.db", "", "");
		final DSWrapper wrapper = new DSWrapper(simpleDataSource, "test.driver");

		final DSWrapper clone = wrapper.clone();
		Assert.assertEquals("test.driver", clone.getDriver());
		Assert.assertEquals(simpleDataSource, clone.getRaw());
	}
}
