/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.text.StrUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows 资源管理器风格字符串比较器
 *
 * <p>此比较器模拟了 Windows 资源管理器的文件名排序方式，可得到与其相同的排序结果。</p>
 *
 * <p>假设有一个数组，包含若干个文件名 {@code {"abc2.doc", "abc1.doc", "abc12.doc"}}</p>
 * <p>在 Windows 资源管理器中以名称排序时，得到 {@code {"abc1.doc", "abc2.doc", "abc12.doc" }}</p>
 * <p>调用 {@code Arrays.sort(filenames);} 时，得到 {@code {"abc1.doc", "abc12.doc", "abc2.doc" }}</p>
 * <p>调用 {@code Arrays.sort(filenames, new WindowsExplorerStringComparator());} 时，得到 {@code {"abc1.doc", "abc2.doc",
 * "abc12.doc" }}，这与在资源管理器中看到的相同</p>
 *
 * @author YMNNs
 * @see
 * <a href="https://stackoverflow.com/questions/23205020/java-sort-strings-like-windows-explorer">Java - Sort Strings like Windows Explorer</a>
 */
public class WindowsExplorerStringComparator implements Comparator<CharSequence> {

	/**
	 * 单例
	 */
	public static final WindowsExplorerStringComparator INSTANCE = new WindowsExplorerStringComparator();

	private static final Pattern splitPattern = Pattern.compile("\\d+|\\.|\\s");

	@Override
	public int compare(final CharSequence str1, final CharSequence str2) {
		final Iterator<String> i1 = splitStringPreserveDelimiter(str1).iterator();
		final Iterator<String> i2 = splitStringPreserveDelimiter(str2).iterator();
		while (true) {
			//Til here all is equal.
			if (!i1.hasNext() && !i2.hasNext()) {
				return 0;
			}
			//first has no more parts -> comes first
			if (!i1.hasNext()) {
				return -1;
			}
			//first has more parts than i2 -> comes after
			if (!i2.hasNext()) {
				return 1;
			}

			final String data1 = i1.next();
			final String data2 = i2.next();
			int result;
			try {
				//If both data are numbers, then compare numbers
				result = Long.compare(Long.parseLong(data1), Long.parseLong(data2));
				//If numbers are equal than longer comes first
				if (result == 0) {
					result = -Integer.compare(data1.length(), data2.length());
				}
			} catch (final NumberFormatException ex) {
				//compare text case insensitive
				result = data1.compareToIgnoreCase(data2);
			}

			if (result != 0) {
				return result;
			}
		}
	}

	private List<String> splitStringPreserveDelimiter(final CharSequence str) {
		final Matcher matcher = splitPattern.matcher(str);
		final List<String> list = new ArrayList<>();
		int pos = 0;
		while (matcher.find()) {
			list.add(StrUtil.sub(str, pos, matcher.start()));
			list.add(matcher.group());
			pos = matcher.end();
		}
		list.add(StrUtil.subSuf(str, pos));
		return list;
	}
}
