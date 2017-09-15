package com.xiaoleilu.hutool.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;

/**
 * 系统运行时工具类，用于执行系统命令的工具
 * 
 * @author Looly
 * @since 3.1.1
 */
public class RuntimeUtil {

	/**
	 * 执行系统命令，使用系统默认编码
	 * 
	 * @param cmds 命令列表，每个元素代表一条命令
	 * @return 执行结果
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> exec(String... cmds) throws IORuntimeException {
		return exec(CharsetUtil.defaultCharset(), cmds);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 * 
	 * @param charset 编码
	 * @param cmds 命令列表，每个元素代表一条命令
	 * @return 执行结果
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static List<String> exec(Charset charset, String... cmds) throws IORuntimeException {
		Process process;
		try {
			// process = Runtime.getRuntime().exec(cmds);
			process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return getResult(process, charset);
	}

	/**
	 * 获取命令执行结果，使用系统默认编码
	 * 
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 */
	public static List<String> getResult(Process process) {
		return getResult(process, CharsetUtil.defaultCharset());
	}

	/**
	 * 获取命令执行结果，使用系统默认编码
	 * 
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static List<String> getResult(Process process, Charset charset) {
		InputStream in = null;
		try {
			in = process.getInputStream();
			return IoUtil.readLines(in, CharsetUtil.defaultCharset(), new ArrayList<String>());
		} finally {
			IoUtil.close(in);
		}
	}
}
