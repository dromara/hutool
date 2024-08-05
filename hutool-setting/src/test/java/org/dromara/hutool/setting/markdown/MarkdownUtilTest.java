package org.dromara.hutool.setting.markdown;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MarkdownUtilTest {

	@Test
	void testMarkdownToHtmlWithValidMarkdown() {
		// Given
		final String markdown = "This is *italic* and **bold** text.";

		// When
		final String result = MarkdownUtil.markdownToHtml(markdown);

		// Then
		Assertions.assertTrue(result.contains("<p>This is <em>italic</em> and <strong>bold</strong> text.</p>"),
			"The markdown should be correctly converted to HTML.");
	}

	@Test
	void testMarkdownToHtmlWithEmptyMarkdown() {
		// Given
		final String markdown = "";

		// When
		final String result = MarkdownUtil.markdownToHtml(markdown);

		// Then
		Assertions.assertEquals("", result, "Empty markdown should result in an empty HTML string.");
	}

	@Test
	void testMarkdownToHtmlWithHeadingAndLink() {
		// Given
		final String markdown = "# Heading\n\n[Link](https://example.com) to example site.";

		// When
		final String result = MarkdownUtil.markdownToHtml(markdown);

		// Then
		Assertions.assertTrue(result.contains("<h1>Heading</h1>"), "Heading should be converted correctly.");
		Assertions.assertTrue(result.contains("<a href=\"https://example.com\">Link</a>"), "Link should be converted correctly.");
	}

	@Test
	void testMarkdownToHtmlPreservesWhitespace() {
		// Given
		final String markdown = "Text    with    spaces";

		// When
		final String result = MarkdownUtil.markdownToHtml(markdown);

		// Then
		Assertions.assertEquals("<p>Text    with    spaces</p>\n", result, "Whitespace should be preserved as in the original markdown.");
	}
}

