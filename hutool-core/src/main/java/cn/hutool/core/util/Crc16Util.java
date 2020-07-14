package cn.hutool.core.util;

/**
 * crc16校验码工具类
 *
 * @author wangjian
 */
public class Crc16Util {

    /**
     * 生成crc 十六进制整数 4位固定长度 返回大写
     * 
     * @param str 要生成crc的字符串
     * @return
     */
    public static String crc16(String str) {
        int len = 4;
        String getHexStr = "";
        byte[] buf = str.getBytes();
        int size = 8;
        int r = 0xffff;

        for (int j = 0; j < buf.length; j++) {
            int hi = r >> size;
            hi ^= buf[j];
            r = hi;

            for (int i = 0; i < size; i++) {
                int flag = r & 0x0001;
                r = r >> 1;
                if (flag == 1) {
                    r ^= 0xa001;
                }
            }
        }

        getHexStr = Integer.toHexString(r);
        if (getHexStr.length() < len) {
            int loop = len - getHexStr.length();
            for (int i = 0; i < loop; i++) {
                getHexStr += "0";
            }
        }
        if (r == 0) {
            getHexStr = "0000";
        }
        
        return getHexStr.toUpperCase();
    }
}
