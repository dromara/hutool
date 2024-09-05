/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.symmetric;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * https://gitee.com/dromara/hutool/issues/I4EMST
 */
public class Sm4StreamTest {

	private static final SM4 sm4 = new SM4();

	private static final boolean IS_CLOSE = false;

	@Test
	@Disabled
	public void sm4Test(){
		final String source = "d:/test/sm4_1.txt";
		final String target = "d:/test/sm4_2.data";
		final String target2 = "d:/test/sm4_3.txt";
		encrypt(source, target);
		decrypt(target, target2);
	}

	public static void encrypt(final String source, final String target) {
		try (final InputStream input = Files.newInputStream(Paths.get(source));
			 final OutputStream out = Files.newOutputStream(Paths.get(target))) {
			sm4.encrypt(input, out, IS_CLOSE);
			System.out.println("============encrypt end");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void decrypt(final String source, final String target) {
		try (final InputStream input = Files.newInputStream(Paths.get(source));
			 final OutputStream out = Files.newOutputStream(Paths.get(target))) {
			sm4.decrypt(input, out, IS_CLOSE);
			System.out.println("============decrypt end");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
