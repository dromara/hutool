package cn.hutool.db;

import cn.hutool.db.sql.Wrapper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author bwcx_jzy
 * @since 24/3/28 028
 */
public class WrapperTest {

	@Test
	@Disabled
	public void test() {
		Wrapper wrapper = new Wrapper('`');
		String originalName = "name";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testDotWrap() {
		Wrapper wrapper = new Wrapper('`');
		String originalName = "name.age";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testError() {
		Wrapper wrapper = new Wrapper('`');
		String originalName = "name.age*";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		assertEquals(unWrapName, originalName);
	}
}
