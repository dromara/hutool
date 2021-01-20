package cn.hutool.core.io.checksum;

import java.io.Serializable;
import java.util.zip.Checksum;

/**
 * CRC8 循环冗余校验码（Cyclic Redundancy Check）实现<br>
 * 代码来自：https://github.com/BBSc0der
 * 
 * @author Bolek,Looly
 * @since 4.4.1
 */
public class CRC8 implements Checksum, Serializable {
	private static final long serialVersionUID = 1L;

	private final short init;
	private final short[] crcTable = new short[256];
	private short value;

	/**
	 * 构造<br>
	 * 
	 * @param polynomial Polynomial, typically one of the POLYNOMIAL_* constants.
	 * @param init Initial value, typically either 0xff or zero.
	 */
	public CRC8(int polynomial, short init) {
		this.value = this.init = init;
		for (int dividend = 0; dividend < 256; dividend++) {
			int remainder = dividend;// << 8;
			for (int bit = 0; bit < 8; ++bit) {
				if ((remainder & 0x01) != 0) {
					remainder = (remainder >>> 1) ^ polynomial;
				} else {
					remainder >>>= 1;
				}
			}
			crcTable[dividend] = (short) remainder;
		}
	}

	@Override
	public void update(byte[] buffer, int offset, int len) {
		for (int i = 0; i < len; i++) {
			int data = buffer[offset + i] ^ value;
			value = (short) (crcTable[data & 0xff] ^ (value << 8));
		}
	}

	/**
	 * Updates the current checksum with the specified array of bytes. Equivalent to calling <code>update(buffer, 0, buffer.length)</code>.
	 * 
	 * @param buffer the byte array to update the checksum with
	 */
	public void update(byte[] buffer) {
		update(buffer, 0, buffer.length);
	}

	@Override
	public void update(int b) {
		update(new byte[] { (byte) b }, 0, 1);
	}

	@Override
	public long getValue() {
		return value & 0xff;
	}

	@Override
	public void reset() {
		value = init;
	}
}
