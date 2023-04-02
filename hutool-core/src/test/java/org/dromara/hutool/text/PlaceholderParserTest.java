package org.dromara.hutool.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * test for {@link PlaceholderParser}
 *
 * @author huangchengxing
 */
public class PlaceholderParserTest {

	@Test
	public void testParse() {
		String text = "i {a}{m} a {jvav} programmer";
		PlaceholderParser parser = new PlaceholderParser(str -> str, "{", "}");
		Assertions.assertEquals(
			"i am a jvav programmer",
			parser.apply(text)
		);

		text = "i [a][m] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]");
		Assertions.assertEquals(
			"i am a jvav programmer",
			parser.apply(text)
		);

		text = "i \\[a][[m\\]] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]");
		Assertions.assertEquals(
			"i [a][m] a jvav programmer",
			parser.apply(text)
		);

		text = "i /[a][[m/]] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]", '/');
		Assertions.assertEquals(
			"i [a][m] a jvav programmer",
			parser.apply(text)
		);
	}

}
