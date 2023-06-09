package cn.hutool.crypto.asymmetric;

import java.math.BigInteger;

/** 存放Paillier 公钥
 *
 * @author Revers.
 **/
public class PaillierpublicKey{
	private BigInteger n;
	private BigInteger g;

	public PaillierpublicKey(BigInteger n, BigInteger g) {
		if (n == null) {
			throw new NullPointerException("n is null");
		}
		if (g == null) {
			throw new NullPointerException("g is null");
		}
		this.n = n;
		this.g = g;
	}

	public BigInteger getN() {
		return n;
	}
	public BigInteger getG() {
		return g;
	}
}
