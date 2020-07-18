package cn.hutool.core.io.checksum.crc16;

import java.io.Serializable;
import java.util.zip.Checksum;

/**
 * CRC16 Checksum，用于提供多种CRC16算法的通用实现<br>
 * 通过继承此类，重写update和reset完成相应算法。
 *
 * @author looly
 * @since 5.3.10
 */
public abstract class CRC16Checksum implements Checksum, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * CRC16 Checksum 结果值
	 */
	protected int wCRCin = 0x0000;

	@Override
	public long getValue() {
		return wCRCin;
	}

	@Override
	public void reset() {
		wCRCin = 0x0000;
	}

	/**
	 * 计算全部字节
	 * @param b 字节
	 */
	public void update(byte[] b){
		update(b, 0, b.length);
	}

	@Override
	public void update(byte[] b, int off, int len) {
		for (int i = off; i < off + len; i++)
			update(b[i]);
	}

}
