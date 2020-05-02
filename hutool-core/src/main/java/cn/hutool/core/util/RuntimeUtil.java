package cn.hutool.core.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
	public static String execForStr(String... cmds) throws IORuntimeException {
		return execForStr(CharsetUtil.systemCharset(), cmds);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param charset 编码
	 * @param cmds    命令列表，每个元素代表一条命令
	 * @return 执行结果
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static String execForStr(Charset charset, String... cmds) throws IORuntimeException {
		return getResult(exec(cmds), charset);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param cmds 命令列表，每个元素代表一条命令
	 * @return 执行结果，按行区分
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> execForLines(String... cmds) throws IORuntimeException {
		return execForLines(CharsetUtil.systemCharset(), cmds);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param charset 编码
	 * @param cmds    命令列表，每个元素代表一条命令
	 * @return 执行结果，按行区分
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static List<String> execForLines(Charset charset, String... cmds) throws IORuntimeException {
		return getResultLines(exec(cmds), charset);
	}

	/**
	 * 执行命令<br>
	 * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
	 *
	 * @param cmds 命令
	 * @return {@link Process}
	 */
	public static Process exec(String... cmds) {
		if (ArrayUtil.isEmpty(cmds)) {
			throw new NullPointerException("Command is empty !");
		}

		// 单条命令的情况
		if (1 == cmds.length) {
			final String cmd = cmds[0];
			if (StrUtil.isBlank(cmd)) {
				throw new NullPointerException("Command is empty !");
			}
			cmds = StrUtil.splitToArray(cmd, StrUtil.C_SPACE);
		}

		Process process;
		try {
			process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return process;
	}

	/**
	 * 执行命令<br>
	 * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
	 *
	 * @param envp 环境变量参数，传入形式为key=value，null表示继承系统环境变量
	 * @param cmds 命令
	 * @return {@link Process}
	 * @since 4.1.6
	 */
	public static Process exec(String[] envp, String... cmds) {
		return exec(envp, null, cmds);
	}

	/**
	 * 执行命令<br>
	 * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
	 *
	 * @param envp 环境变量参数，传入形式为key=value，null表示继承系统环境变量
	 * @param dir  执行命令所在目录（用于相对路径命令执行），null表示使用当前进程执行的目录
	 * @param cmds 命令
	 * @return {@link Process}
	 * @since 4.1.6
	 */
	public static Process exec(String[] envp, File dir, String... cmds) {
		if (ArrayUtil.isEmpty(cmds)) {
			throw new NullPointerException("Command is empty !");
		}

		// 单条命令的情况
		if (1 == cmds.length) {
			final String cmd = cmds[0];
			if (StrUtil.isBlank(cmd)) {
				throw new NullPointerException("Command is empty !");
			}
			cmds = StrUtil.splitToArray(cmd, StrUtil.C_SPACE);
		}
		try {
			return Runtime.getRuntime().exec(cmds, envp, dir);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------------------------------- result

	/**
	 * 获取命令执行结果，使用系统默认编码，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 */
	public static List<String> getResultLines(Process process) {
		return getResultLines(process, CharsetUtil.systemCharset());
	}

	/**
	 * 获取命令执行结果，使用系统默认编码，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static List<String> getResultLines(Process process, Charset charset) {
		InputStream in = null;
		try {
			in = process.getInputStream();
			return IoUtil.readLines(in, charset, new ArrayList<>());
		} finally {
			IoUtil.close(in);
			destroy(process);
		}
	}

	/**
	 * 获取命令执行结果，使用系统默认编码，，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static String getResult(Process process) {
		return getResult(process, CharsetUtil.systemCharset());
	}

	/**
	 * 获取命令执行结果，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static String getResult(Process process, Charset charset) {
		InputStream in = null;
		try {
			in = process.getInputStream();
			return IoUtil.read(in, charset);
		} finally {
			IoUtil.close(in);
			destroy(process);
		}
	}

	/**
	 * 获取命令执行异常结果，使用系统默认编码，，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 * @since 4.1.21
	 */
	public static String getErrorResult(Process process) {
		return getErrorResult(process, CharsetUtil.systemCharset());
	}

	/**
	 * 获取命令执行异常结果，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 4.1.21
	 */
	public static String getErrorResult(Process process, Charset charset) {
		InputStream in = null;
		try {
			in = process.getErrorStream();
			return IoUtil.read(in, charset);
		} finally {
			IoUtil.close(in);
			destroy(process);
		}
	}

	/**
	 * 销毁进程
	 *
	 * @param process 进程
	 * @since 3.1.2
	 */
	public static void destroy(Process process) {
		if (null != process) {
			process.destroy();
		}
	}

	/**
	 * 增加一个JVM关闭后的钩子，用于在JVM关闭时执行某些操作
	 *
	 * @param hook 钩子
	 * @since 4.0.5
	 */
	public static void addShutdownHook(Runnable hook) {
		Runtime.getRuntime().addShutdownHook((hook instanceof Thread) ? (Thread) hook : new Thread(hook));
	}

	/**
	 * 获得JVM可用的处理器数量（一般为CPU核心数）
	 *
	 * @return 可用的处理器数量
	 * @since 5.3.0
	 */
	public static int getProcessorCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * 获得JVM中剩余的内存数，单位byte
	 *
	 * @return JVM中剩余的内存数，单位byte
	 * @since 5.3.0
	 */
	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 获得JVM已经从系统中获取到的总共的内存数，单位byte
	 *
	 * @return JVM中剩余的内存数，单位byte
	 * @since 5.3.0
	 */
	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * 获得JVM中可以从系统中获取的最大的内存数，单位byte，以-Xmx参数为准
	 *
	 * @return JVM中剩余的内存数，单位byte
	 * @since 5.3.0
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * 获得JVM最大可用内存，计算方法为：<br>
	 * 最大内存-总内存+剩余内存
	 *
	 * @return 最大可用内存
	 */
	public final long getUsableMemory() {
		return getMaxMemory() - getTotalMemory() + getFreeMemory();
	}
}
