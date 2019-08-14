package cn.hutool.crypto.digest.mac;

import java.io.InputStream;

import cn.hutool.core.io.IoUtil;

/**
 * MAC（Message Authentication Code）算法引擎
 * 
 * @author Looly
 * @since 4.5.13
 */
public interface MacEngine {
	
	/**
	 * 生成摘要
	 * 
	 * @param data {@link InputStream} 数据流
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要bytes
	 */
	byte[] digest(InputStream data, int bufferLength);
}
