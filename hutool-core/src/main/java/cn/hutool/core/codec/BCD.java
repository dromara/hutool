package cn.hutool.core.codec;

import cn.hutool.core.lang.Assert;

/**
 * BCD码（Binary-Coded Decimal‎）亦称二进码十进数或二-十进制代码<br>
 * BCD码这种编码形式利用了四个位元来储存一个十进制的数码，使二进制和十进制之间的转换得以快捷的进行<br>
 * see http://cuisuqiang.iteye.com/blog/1429956
 * @author Looly
 *
 */
public class BCD {

	/**
	 * 字符串转BCD码
	 * @param asc ASCII字符串
	 * @return BCD
	 */
	public static byte[] strToBcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte[] abt;
		if (len >= 2) {
			len >>= 1;
		}
		byte[] bbt;
		bbt = new byte[len];
		abt = asc.getBytes();
		int j;
		int k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/**
	 * ASCII转BCD
	 * @param ascii ASCII byte数组
	 * @return BCD
	 */
	public static byte[] ascToBcd(byte[] ascii) {
		Assert.notNull(ascii, "Ascii must be not null!");
		return ascToBcd(ascii, ascii.length);
	}

	/**
	 * ASCII转BCD
	 * @param ascii ASCII byte数组
	 * @param ascLength 长度
	 * @return BCD
	 */
	public static byte[] ascToBcd(byte[] ascii, int ascLength) {
		Assert.notNull(ascii, "Ascii must be not null!");
		byte[] bcd = new byte[ascLength / 2];
		int j = 0;
		for (int i = 0; i < (ascLength + 1) / 2; i++) {
			bcd[i] = ascToBcd(ascii[j++]);
			bcd[i] = (byte) (((j >= ascLength) ? 0x00 : ascToBcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	/**
	 * BCD转ASCII字符串
	 * @param bytes BCD byte数组
	 * @return ASCII字符串
	 */
	public static String bcdToStr(byte[] bytes) {
		Assert.notNull(bytes, "Bcd bytes must be not null!");
		char[] temp = new char[bytes.length * 2];
		char val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}


	//----------------------------------------------------------------- Private method start
	/**
	 * 转换单个byte为BCD
	 * @param asc ACSII
	 * @return BCD
	 */
	private static byte ascToBcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9')) {
			bcd = (byte) (asc - '0');
		}else if ((asc >= 'A') && (asc <= 'F')) {
			bcd = (byte) (asc - 'A' + 10);
		}else if ((asc >= 'a') && (asc <= 'f')) {
			bcd = (byte) (asc - 'a' + 10);
		}else {
			bcd = (byte) (asc - 48);
		}
		return bcd;
	}
	//----------------------------------------------------------------- Private method end
}
