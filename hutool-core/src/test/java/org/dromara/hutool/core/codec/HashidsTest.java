package org.dromara.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashidsTest {
	@Test
	public void hexEncodeDecode() {
		final Hashids hashids = Hashids.of("my awesome salt".toCharArray());
		final String encoded1 = hashids.encodeFromHex("507f1f77bcf86cd799439011");
		final String encoded2 = hashids.encodeFromHex("0x507f1f77bcf86cd799439011");
		final String encoded3 = hashids.encodeFromHex("0X507f1f77bcf86cd799439011");

		Assertions.assertEquals("R2qnd2vkOJTXm7XV7yq4", encoded1);
		Assertions.assertEquals(encoded1, encoded2);
		Assertions.assertEquals(encoded1, encoded3);
		final String decoded = hashids.decodeToHex(encoded1);
		Assertions.assertEquals("507f1f77bcf86cd799439011", decoded);
	}
}
