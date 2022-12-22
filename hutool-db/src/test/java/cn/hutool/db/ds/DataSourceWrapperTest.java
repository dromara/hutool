package cn.hutool.db.ds;

import cn.hutool.db.ds.simple.SimpleDataSource;
import org.junit.Assert;
import org.junit.Test;

public class DataSourceWrapperTest {

	@Test
	public void cloneTest(){
		final SimpleDataSource simpleDataSource = new SimpleDataSource("jdbc:sqlite:test.db", "", "");
		final DataSourceWrapper wrapper = new DataSourceWrapper(simpleDataSource, "test.driver");

		final DataSourceWrapper clone = wrapper.clone();
		Assert.assertEquals("test.driver", clone.getDriver());
		Assert.assertEquals(simpleDataSource, clone.getRaw());
	}
}
