package cn.hutool.core.io.checksum;

import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.util.CharUtil;


/**
 * 纵向冗余校验（Longitudinal Redundancy Check，简称：LRC）<br>
 * 参照：http://www.ip33.com/lrc.html
 *
 * @author sunday123
 */
public class LRCUtil {

    /**
     * ascii字符串LRC校验
     *
     * <pre>
     *   LRCUtil.asciiLRCHexStr(" ") = e0
     *   LRCUtil.asciiLRCHexStr("1") = cf
     *   LRCUtil.asciiLRCHexStr("~") = 82
     *   LRCUtil.asciiLRCHexStr("@@@@") = 100
     * </pre>
     *
     * @param asciiStr ascii字符串
     * @return LRC校验的16进制字符串
     * @throws IllegalArgumentException 输入的参数不是ascii字符串
     */
    public static String asciiLRCHexStr(String asciiStr) {
        int sum = 0;
        for (char c : asciiStr.toCharArray()) {
            if (!CharUtil.isAscii(c)) {
                throw new IllegalArgumentException(asciiStr + " is not ascii string!");
            }
            sum += c;
        }
        return HexUtil.toHex(256 - (sum % 256));
    }

    /**
     * 16进制字符串LRC校验
     *
     * <pre>
     *   LRCUtil.hexLRCHexStr("0")  = 100
     *   LRCUtil.hexLRCHexStr("80")  = 80
     *   LRCUtil.hexLRCHexStr("8000")  = 80
     *   LRCUtil.hexLRCHexStr("1122FFEE")  = e0
     * </pre>
     *
     * @param hex 16进制字符串
     * @return LRC校验的16进制字符串
     * @throws IllegalArgumentException 输入的参数不是16进制字符串，比如输入T
     */
    public static String hexLRCHexStr(String hex) {
        if (!HexUtil.isHexNumber("0X" + hex)) {
            throw new IllegalArgumentException(hex + " is not hex string!");
        }
        byte[] bytes = HexUtil.decodeHex(hex);
        int sum = 0;
        for (byte b : bytes) {
            sum += (b & 0xFF);
        }
        return HexUtil.toHex(256 - (sum % 256));
    }
}
