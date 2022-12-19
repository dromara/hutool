package cn.hutool.core.io.checksum;

import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.util.CharUtil;

/**
 * BCC(Block Check Character/信息组校验码)<br>
 * 参照：http://www.ip33.com/bcc.html
 *
 * @author sunday123
 */
public class BCCUtil {

    /**
     * 16进制字符串BCC校验
     *
     * <pre>
     *   BCCUtil.hexBCCHexStr("A")  = a
     *   BCCUtil.hexBCCHexStr("01A0")  = a1
     *   BCCUtil.hexBCCHexStr("AA")  = aa
     *   BCCUtil.hexBCCHexStr("01A07CFF02")  = 20
     * </pre>
     *
     * @param hexStr 16进制字符串
     * @return BCC校验的16进制字符串
     * @throws IllegalArgumentException 输入的参数不是16进制字符串，比如输入T
     */
	public static String hexBCCHexStr(String hexStr) {
		if (!HexUtil.isHexNumber("0X" + hexStr)) {
			throw new IllegalArgumentException(hexStr + " is not hex string!");
        }

        int a = 0;
        for (byte b : HexUtil.decodeHex(hexStr)) {
            a ^= b & 0xFF;
        }
        return HexUtil.toHex(a);
    }

    /**
     * ascii字符串BCC校验
     *
     * <pre>
     *   BCCUtil.asciiBCCHexStr(" ") = 20
     *   BCCUtil.asciiBCCHexStr("  ") = 0
     *   BCCUtil.asciiBCCHexStr("A") = 41
     *   BCCUtil.asciiBCCHexStr("AA") = 0
     *   BCCUtil.asciiBCCHexStr("1234") = 4
     * </pre>
     *
     * @param asciiStr ascii字符串
     * @return BCC校验的16进制字符串
     * @throws IllegalArgumentException 输入的参数不是ascii字符串
     */
    public static String asciiBCCHexStr(String asciiStr) {
        int a = 0;
        for (char c : asciiStr.toCharArray()) {
            if (!CharUtil.isAscii(c)) {
                throw new IllegalArgumentException(asciiStr + " is not ascii string!");
            }
            a ^= c;
        }
        return HexUtil.toHex(a);
    }


}
