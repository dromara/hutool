package cn.hutool.core.comparator;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 按照中国人使用习惯，对给定的数字、字母、汉字字符串进行排序
 *
 * @author ZhenhengXie
 * @since 5.8.13
 */
public class ChineseComparator implements Comparator<String>, Serializable {

	private static final long serialVersionUID = 1L;

	// 数字类型
	private static final Integer TYPE_NUMBER = 0;

	// 字符类型（非数字）
	private static final Integer TYPE_CHARACTER = 1;

	/**
	 * 构造
	 */
	public ChineseComparator() {
	}

	@Override
	public int compare(String o1, String o2) {
		// 把字符串拆分成字符数组
		String[] o1Chars = o1.split("");
		String[] o2Chars = o2.split("");

		// 根据字符数组生成带分类的字符列表
		// List<Object>的第一位为该字符的类型(TYPE_NUMBER, TYPE_CHARACTER)
		// List<Object>的第二位为该字符的内容(一位数字, 一位非数字, 多位数字)
		List<List<Object>> o1CharList = getCharList(o1Chars);
		List<List<Object>> o2CharList = getCharList(o2Chars);

		// 统一CharList的长度
		int max = Math.max(o1CharList.size(), o2CharList.size());
		while (o1CharList.size() < max) {
			o1CharList.add(new ArrayList<>());
		}
		while (o2CharList.size() < max) {
			o2CharList.add(new ArrayList<>());
		}

		// 开始比较
		int compare = 0;
		for (int i = 0; i < max; i++) {
			List<Object> o1list = o1CharList.get(i);
			List<Object> o2list = o2CharList.get(i);

			// CharList短的，排在前面
			if (o1list.size() == 0) {
				compare = -1;
				break;
			}
			if (o2list.size() == 0) {
				compare = 1;
				break;
			}

			// 先比较类型
			Integer o1Type = (Integer) o1list.get(0);
			Integer o2Type = (Integer) o2list.get(0);
			int typeCompare = Integer.compare(o1Type, o2Type);
			if (typeCompare != 0) {
				// 类型不同，则数字在前，非数字在后
				compare = typeCompare;
				break;
			} else {
				// 类型相同，则比较内容
				if (TYPE_NUMBER.equals(o1Type)) {
					// 比较数字
					int o1Content = Integer.parseInt(o1list.get(1).toString());
					int o2Content = Integer.parseInt(o2list.get(1).toString());
					compare = Integer.compare(o1Content, o2Content);
				} else if (TYPE_CHARACTER.equals(o1Type)) {
					// 比较非数字
					String o1Content = (String) o1list.get(1);
					String o2Content = (String) o2list.get(1);
					compare = Collator.getInstance(Locale.CHINESE).compare(o1Content, o2Content);
				}
				// 如果不相等，则退出比较
				if (compare != 0) {
					break;
				}
			}
		}
		return compare;
	}

	/**
	 * 根据字符数组生成带分类的字符列表
	 *
	 * @param chars 字符数组
	 * @return 带分类的字符列表，List<Object>的第一位为该字符的类型(TYPE_NUMBER, TYPE_CHARACTER)，第二位为该字符的内容
	 */
	private List<List<Object>> getCharList(String[] chars) {
		List<List<Object>> charList = new ArrayList<>();
		List<Object> list;
		for (int i = 0; i < chars.length; i++) {
			char c = (chars[i].toCharArray())[0];
			list = new ArrayList<>();
			// 是否为数字
			if (((int) c >= '0' && (int) c <= '9')) {
				StringBuilder str = new StringBuilder();
				// 下一位是否为数字，如果为数字则组成多位数
				do {
					str.append(c);
					if (i + 1 < chars.length) {
						c = (chars[++i].toCharArray())[0];
					} else {
						break;
					}
				} while ((int) c >= '0' && (int) c <= '9');
				if (!(i + 1 == chars.length) || !(((int) c >= '0' && (int) c <= '9'))) {
					i--;
				}
				list.add(TYPE_NUMBER);
				list.add(str.toString());
			} else {
				list.add(TYPE_CHARACTER);
				list.add(String.valueOf(c));
			}
			charList.add(list);
		}
		return charList;
	}

}
