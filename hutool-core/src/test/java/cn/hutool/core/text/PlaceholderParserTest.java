package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals(
			"i am a jvav programmer",
			parser.apply(text)
		);

		text = "i [a][m] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]");
		Assert.assertEquals(
			"i am a jvav programmer",
			parser.apply(text)
		);

		text = "i \\[a][[m\\]] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]");
		Assert.assertEquals(
			"i [a][m] a jvav programmer",
			parser.apply(text)
		);

		text = "i /[a][[m/]] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]", '/');
		Assert.assertEquals(
			"i [a][m] a jvav programmer",
			parser.apply(text)
		);
	}

}
