package cn.hutool.core.text;

import java.math.BigInteger;

/**
 * 符号工具类
 * @author dazer & neusoft
 * @date 2021/3/26 12:21
 * 别名：Symbol or special signal or Special symbols
 * 说明：获取常见的特殊符号，如：带圈数字、
 *
 * {@link UnicodeUtil}
 * @link 百度百科 https://baike.baidu.com/item/%E7%89%B9%E6%AE%8A%E5%AD%97%E7%AC%A6/112715?fr=aladdin
 * @link 360百科 https://baike.so.com/doc/5408938-5646935.html
 * @link 百科 https://www.baike.com/wikiid/3469869303298461399?prd=home_search&search_id=5bm572esa2k000&view_id=1takcxx7kjc000
 * @link coolsymbol https://coolsymbol.com/
 * @link 维基百科wikipedia  https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols
 *
 * @since 5.6.2
 */
public class SpecialSymbolUtil {
	public static final String UNICODE_START_CHAR = "\\u";
	private SpecialSymbolUtil(){}

	/**
	 * 获取带圈数字 /封闭式字母数字 ，从1-20,超过1-20报错
	 *
	 *          0	1	2	3	4	5	6	7	8	9	A	B	C	D	E	F
	 * 	U+246x	①	②	③	④	⑤	⑥	⑦	⑧	⑨	⑩	⑪	⑫	⑬	⑭	⑮	⑯
	 * 	U+247x	⑰	⑱	⑲	⑳	⑴	⑵	⑶	⑷	⑸	⑹	⑺	⑻	⑼	⑽	⑾	⑿
	 * 	U+248x	⒀	⒁	⒂	⒃	⒄	⒅	⒆	⒇	⒈	⒉	⒊	⒋	⒌	⒍	⒎	⒏
	 * 	U+249x	⒐	⒑	⒒	⒓	⒔	⒕	⒖	⒗	⒘	⒙	⒚	⒛	⒜	⒝	⒞	⒟
	 * 	U+24Ax	⒠	⒡	⒢	⒣	⒤	⒥	⒦	⒧	⒨	⒩	⒪	⒫	⒬	⒭	⒮	⒯
	 * 	U+24Bx	⒰	⒱	⒲	⒳	⒴	⒵	Ⓐ	Ⓑ	Ⓒ	Ⓓ	Ⓔ	Ⓕ	Ⓖ	Ⓗ	Ⓘ	Ⓙ
	 * 	U+24Cx	Ⓚ	Ⓛ	Ⓜ	Ⓝ	Ⓞ	Ⓟ	Ⓠ	Ⓡ	Ⓢ	Ⓣ	Ⓤ	Ⓥ	Ⓦ	Ⓧ	Ⓨ	Ⓩ
	 * 	U+24Dx	ⓐ	ⓑ	ⓒ	ⓓ	ⓔ	ⓕ	ⓖ	ⓗ	ⓘ	ⓙ	ⓚ	ⓛ	ⓜ	ⓝ	ⓞ	ⓟ
	 * 	U+24Ex	ⓠ	ⓡ	ⓢ	ⓣ	ⓤ	ⓥ	ⓦ	ⓧ	ⓨ	ⓩ	⓪	⓫	⓬	⓭	⓮	⓯
	 * 	U+24Fx	⓰	⓱	⓲	⓳	⓴	⓵	⓶	⓷	⓸	⓹	⓺	⓻	⓼	⓽	⓾	⓿
	 * @link Unicode_symbols https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols
	 * @link Enclosed Alphanumerics https://en.wikipedia.org/wiki/Enclosed_Alphanumerics
	 *
	 * <code>
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(1));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(2));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(3));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(4));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(14));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(18));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(19));
	 *     System.out.println(enclosedAlphanumericsStyle1ByInt(20));
	 * </code>
	 *
	 * @param number 十进制数字，从1、-->10、11--->20
	 * @return  ①②③④⑤⑥⑦⑧⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳
	 */
	public static String enclosedNumericsByInt(int number) {
		if (number <= 0 || number > 20) {
			throw new IllegalArgumentException("number取值范围是[1-20]的正整数,包含1和20");
		}
		String start = "①";
		String unicodeStart = UnicodeUtil.toUnicode(start).substring(UNICODE_START_CHAR.length(),
				UnicodeUtil.toUnicode(start).length() - 1);
		// begin: U+246x的24
		String beginHex = unicodeStart.substring(0, 2);
		//  U+246x的6
		String middleHex = number >= 17 ? "7" : "6";
		// A
		String endHex = Integer.toHexString(number >= 17 ? number - 17 : number - 1);
		// U + 24 + 60
		String unicodeStr = UNICODE_START_CHAR + beginHex + middleHex + endHex;
		return UnicodeUtil.toString(unicodeStr);
	}

	/**
	 * 获取带圈字母 /封闭式字母 ，从a-z or A-Z
	 * 根据字符 获取 Ⓐ Ⓑ Ⓒ Ⓓ Ⓔ Ⓕ Ⓖ Ⓗ Ⓘ Ⓙ Ⓚ Ⓛ Ⓜ Ⓝ Ⓞ Ⓟ Ⓠ Ⓡ Ⓢ Ⓣ Ⓤ Ⓥ Ⓦ Ⓧ Ⓨ Ⓩ ⓐ ⓑ ⓒ ⓓ ⓔ ⓕ ⓖ ⓗ ⓘ ⓙ ⓚ ⓛ ⓜ ⓝ ⓞ ⓟ ⓠ ⓡ ⓢ ⓣ ⓤ ⓥ ⓦ ⓧ ⓨ ⓩ
	 * <code>
	 *      System.out.println(encloseAlphabetByChar( 'A'));
	 * 		System.out.println(encloseAlphabetByChar( 'a'));
	 * 		System.out.println(encloseAlphabetByChar( 'z'));
	 * 		System.out.println(encloseAlphabetByChar( 'Z'))
	 * </code>
	 * @author dazer
	 * @param letter 字母，不区分大小写，'a'、'b'、'c'、'd'...'x'、'y'、'z'; 'A'、'B'...'Z'
	 * @date 2021/3/26 18:10
	 */
	public static String encloseAlphabetByChar(char letter) {
		if (!(letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z')) {
			throw new IllegalArgumentException("number取值范围是[a-z]、[A-Z]的字符");
		}
		// \u24b6
		String start = "Ⓐ";
		String hexStr = UnicodeUtil.toUnicode(start).substring(UNICODE_START_CHAR.length());
		int difference = letter >= 'a' && letter <= 'z' ? (letter - (int)'a') : (letter - (int)'A');
		String hex = new BigInteger(hexStr, 16).add(new BigInteger(String.valueOf(difference), 10)).toString(16);
		//
		String unicodeStr = UNICODE_START_CHAR + hex;
		return UnicodeUtil.toString(unicodeStr);
	}
}
