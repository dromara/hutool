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
 * @link 维基百科wikipedia-Unicode_symbols  https://en.wikipedia.org/wiki/List_of_Unicode_characters#Unicode_symbols
 * @link 维基百科wikipedia-Unicode字符列表   https://zh.wikipedia.org/wiki/Unicode%E5%AD%97%E7%AC%A6%E5%88%97%E8%A1%A8
 *
 * @since 5.6.2
 */
public class UnicodeSymbolUtil {
	public static final String UNICODE_START_CHAR = "\\u";
	private UnicodeSymbolUtil(){}

	/**
	 * 封闭式字母数字(带圆圈数字) ，从1-20,超过1-20报错
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
	 *     System.out.println(enclosedNumericsByInt(1));
	 *     System.out.println(enclosedNumericsByInt(2));
	 *     System.out.println(enclosedNumericsByInt(3));
	 *     System.out.println(enclosedNumericsByInt(4));
	 *     System.out.println(enclosedNumericsByInt(14));
	 *     System.out.println(enclosedNumericsByInt(18));
	 *     System.out.println(enclosedNumericsByInt(19));
	 *     System.out.println(enclosedNumericsByInt(20));
	 * </code>
	 *
	 * @param number 十进制数字，取值，从1-->20
	 * @return  ①②③④⑤⑥⑦⑧⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳
	 */
	public static String enclosedNumericsByInt(int number) {
		if (!(number >=1 && number <= 20)) {
			throw new IllegalArgumentException("封闭式数字的number取值范围是[1-20]的正整数,包含1和20");
		}
		int difference = number - 1;
		return unicodeAddDifference("①", difference);
	}

	/**
	 * 封闭式字母(获取带圈字母) ，从a-z or A-Z
	 * 根据字符 获取 Ⓐ Ⓑ Ⓒ Ⓓ Ⓔ Ⓕ Ⓖ Ⓗ Ⓘ Ⓙ Ⓚ Ⓛ Ⓜ Ⓝ Ⓞ Ⓟ Ⓠ Ⓡ Ⓢ Ⓣ Ⓤ Ⓥ Ⓦ Ⓧ Ⓨ Ⓩ ⓐ ⓑ ⓒ ⓓ ⓔ ⓕ ⓖ ⓗ ⓘ ⓙ ⓚ ⓛ ⓜ ⓝ ⓞ ⓟ ⓠ ⓡ ⓢ ⓣ ⓤ ⓥ ⓦ ⓧ ⓨ ⓩ
	 * <code>
	 *      System.out.println(encloseAlphabetByChar( 'A'));
	 * 		System.out.println(encloseAlphabetByChar( 'a'));
	 * 		System.out.println(encloseAlphabetByChar( 'z'));
	 * 		System.out.println(encloseAlphabetByChar( 'Z'));
	 * </code>
	 * @author dazer
	 * @param letter 字母，不区分大小写，'a'、'b'、'c'、'd'...'x'、'y'、'z'; 'A'、'B'...'Z'
	 * @date 2021/3/26 18:10
	 */
	public static String encloseAlphabetByChar(char letter) {
		letter = (letter + "").toLowerCase().charAt(0);
		if (!(letter >= 'a' && letter <= 'z')) {
			throw new IllegalArgumentException("封闭式字母number取值范围是[a-z]、[A-Z]的字符");
		}
		int difference = (letter - (int)'a');
		return unicodeAddDifference("Ⓐ", difference);
	}

	/**
	 * 获取大写的罗马数字
	 * 维基百科-Number_Forms: https://en.wikipedia.org/wiki/List_of_Unicode_characters#Number_Forms
	 * 百度百科-罗马数字: https://baike.baidu.com/item/%E7%BD%97%E9%A9%AC%E6%95%B0%E5%AD%97
	 * <pre>
	 *          0	1	2	3	4	5	6	7	8	9	A	B	C	D	E	F
	 * U+215x	⅐	⅑	⅒	⅓	⅔	⅕	⅖	⅗	⅘	⅙	⅚	⅛	⅜	⅝	⅞	⅟
	 * U+216x	Ⅰ	Ⅱ	Ⅲ	Ⅳ	Ⅴ	Ⅵ	Ⅶ	Ⅷ	Ⅸ	Ⅹ	Ⅺ	Ⅻ	Ⅼ	Ⅽ	Ⅾ	Ⅿ
	 * U+217x	ⅰ	ⅱ	ⅲ	ⅳ	ⅴ	ⅵ	ⅶ	ⅷ	ⅸ	ⅹ	ⅺ	ⅻ	ⅼ	ⅽ	ⅾ	ⅿ
	 * </pre>
	 * <code>
	 *     System.out.println(romanNumeralsUppercaseByInt(1));
	 *     System.out.println(romanNumeralsUppercaseByInt(12));
	 * </code>
	 * @return 罗马数字-大写，	Ⅰ	Ⅱ	Ⅲ	Ⅳ	Ⅴ	Ⅵ	Ⅶ	Ⅷ	Ⅸ	Ⅹ	Ⅺ	Ⅻ	Ⅼ	Ⅽ	Ⅾ	Ⅿ
	 *         罗马数字-小写，	ⅰ	ⅱ	ⅲ	ⅳ	ⅴ	ⅵ	ⅶ	ⅷ	ⅸ	ⅹ	ⅺ	ⅻ	ⅼ	ⅽ	ⅾ	ⅿ
	 */
	public static String romanNumeralsUppercaseByInt(int number) {
		if (!(number >=1 && number <= 12)) {
			throw new IllegalArgumentException("大写罗马数字的number取值范围是[1-12]的正整数,包含1和12");
		}
		int difference = number - 1;
		return unicodeAddDifference("Ⅰ", difference);
	}

	/**
	 * 获取小写的罗马数字
	 * 维基百科-Number_Forms: https://en.wikipedia.org/wiki/List_of_Unicode_characters#Number_Forms
	 * 百度百科-罗马数字: https://baike.baidu.com/item/%E7%BD%97%E9%A9%AC%E6%95%B0%E5%AD%97
	 * <pre>
	 *          0	1	2	3	4	5	6	7	8	9	A	B	C	D	E	F
	 * U+215x	⅐	⅑	⅒	⅓	⅔	⅕	⅖	⅗	⅘	⅙	⅚	⅛	⅜	⅝	⅞	⅟
	 * U+216x	Ⅰ	Ⅱ	Ⅲ	Ⅳ	Ⅴ	Ⅵ	Ⅶ	Ⅷ	Ⅸ	Ⅹ	Ⅺ	Ⅻ	Ⅼ	Ⅽ	Ⅾ	Ⅿ
	 * U+217x	ⅰ	ⅱ	ⅲ	ⅳ	ⅴ	ⅵ	ⅶ	ⅷ	ⅸ	ⅹ	ⅺ	ⅻ	ⅼ	ⅽ	ⅾ	ⅿ
	 * </pre>
	 * <code>
	 *     System.out.println(romanNumeralsLowercaseByInt(1));
	 *     System.out.println(romanNumeralsLowercaseByInt(12));
	 * </code>
	 * @return 罗马数字-大写，	Ⅰ	Ⅱ	Ⅲ	Ⅳ	Ⅴ	Ⅵ	Ⅶ	Ⅷ	Ⅸ	Ⅹ	Ⅺ	Ⅻ	Ⅼ	Ⅽ	Ⅾ	Ⅿ
	 *         罗马数字-小写，	ⅰ	ⅱ	ⅲ	ⅳ	ⅴ	ⅵ	ⅶ	ⅷ	ⅸ	ⅹ	ⅺ	ⅻ	ⅼ	ⅽ	ⅾ	ⅿ
	 */
	public static String romanNumeralsLowercaseByInt(int number) {
		if (!(number >=1 && number <= 12)) {
			throw new IllegalArgumentException("大写罗马数字的number取值范围是[1-12]的正整数,包含1和12");
		}
		int difference = number - 1;
		return unicodeAddDifference("ⅰ", difference);
	}

	/**
	 * 黑桃♠扑克牌
	 * 黑桃（spade）.对应宝剑，象征正义、战争与灾难。spade是铲子，也是武器。象征战争。
	 * -------
	 * 黑桃♠ 🂡 spade
	 * 红桃♥ 🂱 Heart
	 * 方块♦ 🃁 Diamond
	 * 梅花♣ 🃑 Club
	 * -------
	 * https://en.wikipedia.org/wiki/Playing_cards_in_Unicode
	 * <code>
	 *      System.out.println(playingCardsSpadeByInt(1));
	 * 		System.out.println(playingCardsSpadeByInt(2));
	 * 		System.out.println(playingCardsSpadeByInt(3));
	 * 		System.out.println(playingCardsSpadeByInt(13));
	 * </code>
	 * @param number 1-13
	 * @return 黑桃扑克牌，🂡---->🂮
	 *         🂡🂢🂣🂤🂥🂦🂧🂨🂩🂪🂫🂬🂭🂮
	 *         扑克牌unicode特殊是两位
	 */
	public static String playingCardsSpadeByInt(int number) {
		if (!(number >=1 && number <= 13)) {
			throw new IllegalArgumentException("扑克牌♠number取值范围是[1-13]的正整数,包含1和13");
		}
		int difference = number - 1;
		return unicodeAddDifference("🂡", difference);
	}

	/**
	 * 红桃♥扑克牌
	 */
	public static String playingCardsHeartByInt(int number) {
		if (!(number >=1 && number <= 13)) {
			throw new IllegalArgumentException("扑克牌♥number取值范围是[1-13]的正整数,包含1和13");
		}
		int difference = number - 1;
		return unicodeAddDifference("🂱", difference);
	}

	/**
	 * 方块♦扑克牌
	 */
	public static String playingCardsDiamondByInt(int number) {
		if (!(number >=1 && number <= 13)) {
			throw new IllegalArgumentException("扑克牌方块♦number取值范围是[1-13]的正整数,包含1和13");
		}
		int difference = number - 1;
		return unicodeAddDifference("🃑", difference);
	}

	/**
	 * 梅花♣扑克牌
	 */
	public static String playingCardsClubByInt(int number) {
		if (!(number >=1 && number <= 13)) {
			throw new IllegalArgumentException("扑克牌梅花♣number取值范围是[1-13]的正整数,包含1和13");
		}
		int difference = number - 1;
		return unicodeAddDifference("🂱", difference);
	}

	/**
	 * 一位unicode从开始特殊字符，便宜num个数字
	 * @param startSymbol 开始的特殊字符，如：Ⓐ、①
	 * @return startSymbol + difference 之后的特殊字符，如：Ⓐ + 2 = Ⓒ
	 */
	private static String unicodeAddDifference(String startSymbol, int difference) {
		// unicode有两种形式
		// k = U+1F0AE = 🂮 = "\uD83C\uDCAE"
		// Ⓐ \u24b6
		int unicodeLen = 6;
		String unicode = UnicodeUtil.toUnicode(startSymbol);
		if (unicode.length() <= unicodeLen) {
			// 8位实现 Ⓐ \u24b6
			String hexStr = unicode.substring(UNICODE_START_CHAR.length());
			String hex = new BigInteger(hexStr, 16).add(new BigInteger(String.valueOf(difference), 10)).toString(16);
			//
			String unicodeStr = UNICODE_START_CHAR + hex;
			return UnicodeUtil.toString(unicodeStr);
		} else {
			// 16实现 🂮 = "\uD83C\uDCAE"
			String unicode1 = unicode.substring(0, unicodeLen);
			String unicode2 = unicode.substring(unicodeLen);
			String hexStr2 = unicode2.substring(UNICODE_START_CHAR.length());
			String hex2 = new BigInteger(hexStr2, 16).add(new BigInteger(String.valueOf(difference), 10)).toString(16);
			//
			String unicodeStr2 = UNICODE_START_CHAR + hex2;
			return UnicodeUtil.toString(unicode1 + unicodeStr2);
		}
	}
}
