package org.dromara.hutool.db;

import org.dromara.hutool.db.sql.QuoteWrapper;
import org.junit.jupiter.api.Assertions;
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
		QuoteWrapper wrapper = new QuoteWrapper('`');
		String originalName = "name";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testDotWrap() {
		QuoteWrapper wrapper = new QuoteWrapper('`');
		String originalName = "name.age";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testError() {
		QuoteWrapper wrapper = new QuoteWrapper('`');
		String originalName = "name.age*";
		String wrapName = wrapper.wrap(originalName);
		String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}
}
