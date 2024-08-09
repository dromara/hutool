package cn.hutool.http;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HTMLFilterTest {
	@Test
	public void issue3433Test() {
		String p1 = "<p>a</p>";
		String p2 = "<p onclick=\"bbbb\">a</p>";

		final HTMLFilter htmlFilter = new HTMLFilter();
		String filter = htmlFilter.filter(p1);
		assertEquals("<p>a</p>", filter);

		filter = htmlFilter.filter(p2);
		assertEquals("<p>a</p>", filter);
	}
}
