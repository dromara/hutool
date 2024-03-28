package org.dromara.hutool.db;

import org.dromara.hutool.db.sql.QuoteWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author bwcx_jzy
 * @since 24/3/28 028
 */
public class QuoteWrapperTest {

	@Test
	@Disabled
	public void test() {
		final QuoteWrapper wrapper = new QuoteWrapper('`');
		final String originalName = "name";
		final String wrapName = wrapper.wrap(originalName);
		final String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testDotWrap() {
		final QuoteWrapper wrapper = new QuoteWrapper('`');
		final String originalName = "name.age";
		final String wrapName = wrapper.wrap(originalName);
		final String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testError() {
		final QuoteWrapper wrapper = new QuoteWrapper('`');
		final String originalName = "name.age*";
		final String wrapName = wrapper.wrap(originalName);
		final String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}
}
