package cn.hutool.crypto.asymmetric;

import java.math.BigInteger;
import java.util.Random;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.CryptoException;

/**
 * SM2加密解密引擎，来自Bouncy Castle库的SM2Engine类改造<br>
 * SM2加密后的数据格式为（两种模式）：
 * 
 * <pre>
 * curve(C1) | data(C2) | digest(C3)
 * curve(C1) | digest(C3) | data(C2)
 * </pre>
 * 
 * @author looly, bouncycastle
 * @since 4.5.0
 */
public class SM2Engine {

	private final Digest digest;

	private boolean forEncryption;
	private ECKeyParameters ecKey;
	private ECDomainParameters ecParams;
	private int curveLength;
	private Random random;
	/** 加密解密模式 */
	private SM2Mode mode;

	/**
	 * 构造
	 */
	public SM2Engine() {
		this(new SM3Digest());
	}

	/**
	 * 构造
	 * 
	 * @param mode SM2密钥生成模式，可选C1C2C3和C1C3C2
	 */
	public SM2Engine(SM2Mode mode) {
		this(new SM3Digest(), mode);
	}

	/**
	 * 构造
	 * 
	 * @param digest 摘要算法啊
	 */
	public SM2Engine(Digest digest) {
		this(digest, null);
	}

	/**
	 * 构造
	 * 
	 * @param digest 摘要算法啊
	 * @param mode SM2密钥生成模式，可选C1C2C3和C1C3C2
	 */
	public SM2Engine(Digest digest, SM2Mode mode) {
		this.digest = digest;
		this.mode = ObjectUtil.defaultIfNull(mode, SM2Mode.C1C2C3);
	}

	/**
	 * 初始化引擎
	 * 
	 * @param forEncryption 是否为加密模式
	 * @param param {@link CipherParameters}，此处应为{@link ParametersWithRandom}（加密时）或{@link ECKeyParameters}（解密时）
	 */
	public void init(boolean forEncryption, CipherParameters param) {
		this.forEncryption = forEncryption;

		if (param instanceof ParametersWithRandom) {
			final ParametersWithRandom rParam = (ParametersWithRandom) param;
			this.ecKey = (ECKeyParameters) rParam.getParameters();
			this.random = rParam.getRandom();
		} else {
			this.ecKey = (ECKeyParameters) param;
		}
		this.ecParams = this.ecKey.getParameters();

		if (forEncryption) {
			// 检查曲线点
			final ECPoint ecPoint = ((ECPublicKeyParameters) ecKey).getQ().multiply(ecParams.getH());
			if (ecPoint.isInfinity()) {
				throw new IllegalArgumentException("invalid key: [h]Q at infinity");
			}

			// 检查随机参数
			if (null == this.random) {
				this.random = CryptoServicesRegistrar.getSecureRandom();
			}
		}

		// 曲线位长度
		this.curveLength = (this.ecParams.getCurve().getFieldSize() + 7) / 8;
	}

	/**
	 * 处理块，包括加密和解密
	 * 
	 * @param in 数据
	 * @param inOff 数据开始位置
	 * @param inLen 数据长度
	 * @return 结果
	 */
	public byte[] processBlock(byte[] in, int inOff, int inLen) {
		if (forEncryption) {
			return encrypt(in, inOff, inLen);
		} else {
			return decrypt(in, inOff, inLen);
		}
	}

	/**
	 * 设置加密类型
	 * 
	 * @param mode {@link SM2Mode}
	 * @return this
	 */
	public SM2Engine setMode(SM2Mode mode) {
		this.mode = mode;
		return this;
	}

	/**
	 * SM2算法模式<br>
	 * 在SM2算法中，C1C2C3为旧标准模式，C1C3C2为新标准模式
	 * 
	 * @author looly
	 *
	 */
	public static enum SM2Mode {
		C1C2C3, C1C3C2;
	}

	protected ECMultiplier createBasePointMultiplier() {
		return new FixedPointCombMultiplier();
	}

	// --------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 加密
	 * 
	 * @param in 数据
	 * @param inOff 位置
	 * @param inLen 长度
	 * @return 密文
	 */
	private byte[] encrypt(byte[] in, int inOff, int inLen) {
		// 加密数据
		byte[] c2 = new byte[inLen];
		System.arraycopy(in, inOff, c2, 0, c2.length);

		final ECMultiplier multiplier = createBasePointMultiplier();

		byte[] c1;
		ECPoint kPB;
		BigInteger k;
		do {
			k = nextK();
			//产生随机数计算出曲线点C1
			c1 = multiplier.multiply(ecParams.getG(), k).normalize().getEncoded(false);
			kPB = ((ECPublicKeyParameters) ecKey).getQ().multiply(k).normalize();
			kdf(digest, kPB, c2);
		} while (notEncrypted(c2, in, inOff));

		// 杂凑值，效验数据
		byte[] c3 = new byte[digest.getDigestSize()];

		addFieldElement(digest, kPB.getAffineXCoord());
		digest.update(in, inOff, inLen);
		addFieldElement(digest, kPB.getAffineYCoord());

		digest.doFinal(c3, 0);

		// 按照莫属输出结果
		switch (mode) {
		case C1C3C2:
			return Arrays.concatenate(c1, c3, c2);
		default:
			return Arrays.concatenate(c1, c2, c3);
		}
	}

	/**
	 * 解密，只支持私钥解密
	 * 
	 * @param in 密文
	 * @param inOff 位置
	 * @param inLen 长度
	 * @return 解密后的内容
	 */
	private byte[] decrypt(byte[] in, int inOff, int inLen) {
		// 获取曲线点
		final byte[] c1 = new byte[this.curveLength * 2 + 1];
		System.arraycopy(in, inOff, c1, 0, c1.length);

		ECPoint c1P = this.ecParams.getCurve().decodePoint(c1);
		if (c1P.multiply(this.ecParams.getH()).isInfinity()) {
			throw new CryptoException("[h]C1 at infinity");
		}
		c1P = c1P.multiply(((ECPrivateKeyParameters) ecKey).getD()).normalize();

		final int digestSize = this.digest.getDigestSize();

		// 解密C2数据
		final byte[] c2 = new byte[inLen - c1.length - digestSize];

		if (SM2Mode.C1C3C2 == this.mode) {
			// C2位于第三部分
			System.arraycopy(in, inOff + c1.length + digestSize, c2, 0, c2.length);
		} else {
			// C2位于第二部分
			System.arraycopy(in, inOff + c1.length, c2, 0, c2.length);
		}
		kdf(digest, c1P, c2);

		// 使用摘要验证C2数据
		final byte[] c3 = new byte[digestSize];

		addFieldElement(digest, c1P.getAffineXCoord());
		digest.update(c2, 0, c2.length);
		addFieldElement(digest, c1P.getAffineYCoord());
		digest.doFinal(c3, 0);

		int check = 0;
		for (int i = 0; i != c3.length; i++) {
			check |= c3[i] ^ in[inOff + c1.length + ((SM2Mode.C1C3C2 == this.mode) ? 0 : c2.length) + i];
		}

		Arrays.fill(c1, (byte) 0);
		Arrays.fill(c3, (byte) 0);

		if (check != 0) {
			Arrays.fill(c2, (byte) 0);
			throw new CryptoException("invalid cipher text");
		}

		return c2;
	}

	private boolean notEncrypted(byte[] encData, byte[] in, int inOff) {
		for (int i = 0; i != encData.length; i++) {
			if (encData[i] != in[inOff]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 解密数据
	 * 
	 * @param digest {@link Digest}
	 * @param c1 c1点
	 * @param encData 密文
	 */
	private void kdf(Digest digest, ECPoint c1, byte[] encData) {
		int digestSize = digest.getDigestSize();
		byte[] buf = new byte[Math.max(4, digestSize)];
		int off = 0;

		Memoable memo = null;
		Memoable copy = null;

		if (digest instanceof Memoable) {
			addFieldElement(digest, c1.getAffineXCoord());
			addFieldElement(digest, c1.getAffineYCoord());
			memo = (Memoable) digest;
			copy = memo.copy();
		}

		int ct = 0;

		while (off < encData.length) {
			if (memo != null) {
				memo.reset(copy);
			} else {
				addFieldElement(digest, c1.getAffineXCoord());
				addFieldElement(digest, c1.getAffineYCoord());
			}

			Pack.intToBigEndian(++ct, buf, 0);
			digest.update(buf, 0, 4);
			digest.doFinal(buf, 0);

			int xorLen = Math.min(digestSize, encData.length - off);
			xor(encData, buf, off, xorLen);
			off += xorLen;
		}
	}

	private void xor(byte[] data, byte[] kdfOut, int dOff, int dRemaining) {
		for (int i = 0; i != dRemaining; i++) {
			data[dOff + i] ^= kdfOut[i];
		}
	}

	private BigInteger nextK() {
		int qBitLength = ecParams.getN().bitLength();

		BigInteger k;
		do {
			k = new BigInteger(qBitLength, random);
		} while (k.equals(ECConstants.ZERO) || k.compareTo(ecParams.getN()) >= 0);

		return k;
	}

	private void addFieldElement(Digest digest, ECFieldElement v) {
		byte[] p = BigIntegers.asUnsignedByteArray(curveLength, v.toBigInteger());

		digest.update(p, 0, p.length);
	}
	// --------------------------------------------------------------------------------------------------- Private method start
}
