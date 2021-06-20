package cn.hutool.core.net;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地端口生成器<br>
 * 用于生成本地可用（未被占用）的端口号<br>
 * 注意：多线程甚至单线程访问时可能会返回同一端口（例如获取了端口但是没有使用）
 *
 * @author looly
 * @since 4.0.3
 *
 */
public class LocalPortGenerater implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 备选的本地端口 */
	private final AtomicInteger alternativePort;

	/**
	 * 构造
	 *
	 * @param beginPort 起始端口号
	 */
	public LocalPortGenerater(int beginPort) {
		alternativePort = new AtomicInteger(beginPort);
	}

	/**
	 * 生成一个本地端口，用于远程端口映射
	 *
	 * @return 未被使用的本地端口
	 */
	public int generate() {
		int validPort = alternativePort.get();
		// 获取可用端口
		while (false == NetUtil.isUsableLocalPort(validPort)) {
			validPort = alternativePort.incrementAndGet();
		}
		return validPort;
	}
}
