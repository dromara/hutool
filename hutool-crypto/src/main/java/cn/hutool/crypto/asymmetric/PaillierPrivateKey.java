package cn.hutool.crypto.asymmetric;

import java.math.BigInteger;

/** 存放Paillier 私钥
 *
 * @author Revers.
 **/
public class PaillierPrivateKey{
	private final BigInteger n;
	private final BigInteger lambda;
	private final BigInteger u;

	public PaillierPrivateKey(BigInteger n, BigInteger lambda, BigInteger u) {
		if (n == null) {
			throw new NullPointerException("n is null");
		}
		if (lambda == null) {
			throw new NullPointerException("lambda is null");
		}
		if (u == null) {
			throw new NullPointerException("u is null");
		}
		this.n = n;
		this.lambda = lambda;
		this.u = u;
	}

	public BigInteger getN() {
		return n;
	}
	public BigInteger getLambda() {
		return lambda;
	}
	public BigInteger getu() {
		return u;
	}

}
