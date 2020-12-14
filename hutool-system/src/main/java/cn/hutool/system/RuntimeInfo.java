package cn.hutool.system;

import cn.hutool.core.io.FileUtil;

import java.io.Serializable;

/**
 * 运行时信息，包括内存总大小、已用大小、可用大小等
 *
 * @author looly
 */
public class RuntimeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Runtime currentRuntime = Runtime.getRuntime();

	/**
	 * 获得运行时对象
	 *
	 * @return {@link Runtime}
	 */
	public final Runtime getRuntime() {
		return currentRuntime;
	}

	/**
	 * 获得JVM最大内存
	 *
	 * @return 最大内存
	 */
	public final long getMaxMemory() {
		return currentRuntime.maxMemory();
	}

	/**
	 * 获得JVM已分配内存
	 *
	 * @return 已分配内存
	 */
	public final long getTotalMemory() {
		return currentRuntime.totalMemory();
	}

	/**
	 * 获得JVM已分配内存中的剩余空间
	 *
	 * @return 已分配内存中的剩余空间
	 */
	public final long getFreeMemory() {
		return currentRuntime.freeMemory();
	}

	/**
	 * 获得JVM最大可用内存
	 *
	 * @return 最大可用内存
	 */
	public final long getUsableMemory() {
		return currentRuntime.maxMemory() - currentRuntime.totalMemory() + currentRuntime.freeMemory();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "Max Memory:    ", FileUtil.readableFileSize(getMaxMemory()));
		SystemUtil.append(builder, "Total Memory:     ", FileUtil.readableFileSize(getTotalMemory()));
		SystemUtil.append(builder, "Free Memory:     ", FileUtil.readableFileSize(getFreeMemory()));
		SystemUtil.append(builder, "Usable Memory:     ", FileUtil.readableFileSize(getUsableMemory()));

		return builder.toString();
	}
}
