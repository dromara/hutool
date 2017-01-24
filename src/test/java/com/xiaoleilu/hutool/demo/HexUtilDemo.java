package com.xiaoleilu.hutool.demo;

import java.io.UnsupportedEncodingException;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.HexUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 
 * @author Looly
 *
 */
public class HexUtilDemo {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String result = HexUtil.encodeHexStr(StrUtil.bytes("地方地方的", CharsetUtil.CHARSET_UTF_8));
		Console.log(result);
		
		String str = HexUtil.decodeHexStr(result, CharsetUtil.CHARSET_UTF_8);
		Console.log(str);
	}
}
