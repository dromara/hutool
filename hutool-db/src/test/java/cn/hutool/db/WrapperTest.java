package cn.hutool.db;

import cn.hutool.db.sql.Wrapper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author bwcx_jzy
 * @since 24/3/28 028
 */
public class WrapperTest {

	@Test
	@Ignore
	public void test() {
		Wrapper wrapper = new Wrapper('`');
		String originalName = "name";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		Assert.assertEquals(unWrapName, originalName);
	}

	@Test
	@Ignore
	public void testDotWrap() {
		Wrapper wrapper = new Wrapper('`');
		String originalName = "name.age";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		Assert.assertEquals(unWrapName, originalName);
	}

	@Test
	@Ignore
	public void testError() {
		Wrapper wrapper = new Wrapper('`');
		String originalName = "name.age*";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		Assert.assertEquals(unWrapName, originalName);
	}
}
