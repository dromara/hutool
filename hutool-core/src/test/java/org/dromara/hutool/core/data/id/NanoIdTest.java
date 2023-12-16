/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.data.id;

import org.dromara.hutool.core.data.id.NanoId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Tests for NanoId.
 *
 * @author David Klebanoff, Looly
 * @see NanoId
 */
public class NanoIdTest {

	@Test
	public void nanoIdVerify100KRandomNanoIdsAreUniqueVerifiedTest() {

		//It's not much, but it's a good sanity check I guess.
		final int idCount = 100000;
		final Set<String> ids = new HashSet<>(idCount);

		for (int i = 0; i < idCount; i++) {
			final String id = NanoId.randomNanoId();
			if (ids.contains(id) == false) {
				ids.add(id);
			} else {
				Assertions.fail("Non-unique ID generated: " + id);
			}
		}

	}

	@Test
	public void nanoIdSeededRandomSuccessTest() {

		//With a seed provided, we can know which IDs to expect, and subsequently verify that the
		// provided random number generator is being used as expected.
		final Random random = new Random(12345);

		final char[] alphabet =
				("_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

		final int size = 21;

		final String[] expectedIds = new String[]{"kutqLNv1wDmIS56EcT3j7", "U497UttnWzKWWRPMHpLD7",
				"7nj2dWW1gjKLtgfzeI8eC", "I6BXYvyjszq6xV7L9k2A9", "uIolcQEyyQIcn3iM6Odoa"};

		for (final String expectedId : expectedIds) {
			final String generatedId = NanoId.randomNanoId(random, alphabet, size);
			Assertions.assertEquals(expectedId, generatedId);
		}

	}

	@Test
	public void nanoIdVariousAlphabetsSuccessTest() {

		//Test ID generation with various alphabets consisting of 1 to 255 unique symbols.
		for (int symbols = 1; symbols <= 255; symbols++) {

			final char[] alphabet = new char[symbols];
			for (int i = 0; i < symbols; i++) {
				alphabet[i] = (char) i;
			}

			final String id = NanoId
					.randomNanoId(null, alphabet, NanoId.DEFAULT_SIZE);

			//Create a regex pattern that only matches to the characters in the alphabet
			final StringBuilder patternBuilder = new StringBuilder();
			patternBuilder.append("^[");
			for (final char character : alphabet) {
				patternBuilder.append(Pattern.quote(String.valueOf(character)));
			}
			patternBuilder.append("]+$");

			Assertions.assertTrue(id.matches(patternBuilder.toString()));
		}

	}

	@Test
	public void nanoIdVariousSizesSuccessTest() {

		//Test ID generation with all sizes between 1 and 1,000.
		for (int size = 1; size <= 1000; size++) {

			final String id = NanoId.randomNanoId(size);

			Assertions.assertEquals(size, id.length());
		}

	}

	@Test
	public void nanoIdWellDistributedSuccess() {

		//Test if symbols in the generated IDs are well distributed.

		final int idCount = 100000;
		final int idSize = 20;
		final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		final Map<String, Long> charCounts = new HashMap<>();

		for (int i = 0; i < idCount; i++) {

			final String id = NanoId
					.randomNanoId(null, alphabet, idSize);

			for (int j = 0; j < id.length(); j++) {
				final String value = String.valueOf(id.charAt(j));

				final Long charCount = charCounts.get(value);
				if (charCount == null) {
					charCounts.put(value, 1L);
				} else {
					charCounts.put(value, charCount + 1);
				}
			}
		}

		//Verify the distribution of characters is pretty even
		for (final Long charCount : charCounts.values()) {
			final double distribution = (charCount * alphabet.length / (double) (idCount * idSize));
			Assertions.assertTrue(distribution >= 0.95 && distribution <= 1.05);
		}

	}

	@Test
	public void randomNanoIdEmptyAlphabetExceptionThrownTest() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			NanoId.randomNanoId(new SecureRandom(), new char[]{}, 10);
		});
	}

	@Test
	public void randomNanoId256AlphabetExceptionThrownTest() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			//The alphabet is composed of 256 unique characters
			final char[] largeAlphabet = new char[256];
			for (int i = 0; i < 256; i++) {
				largeAlphabet[i] = (char) i;
			}
			NanoId.randomNanoId(new SecureRandom(), largeAlphabet, 20);
		});


	}

	@Test
	public void randomNanoIdNegativeSizeExceptionThrown() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			NanoId.randomNanoId(new SecureRandom(), new char[]{'a', 'b', 'c'}, -10);
		});
	}

	@Test
	public void randomNanoIdZeroSizeExceptionThrown() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			NanoId.randomNanoId(new SecureRandom(), new char[]{'a', 'b', 'c'}, 0);
		});
	}
}
