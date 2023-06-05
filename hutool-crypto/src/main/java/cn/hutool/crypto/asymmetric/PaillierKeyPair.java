package cn.hutool.crypto.asymmetric;

/**
 *  存放Paillier 公钥私钥对
 *
 * @author Revers.
 **/
public class PaillierKeyPair {
	private PaillierPrivateKey privateKey;
	private PaillierpublicKey publicKey;

	public PaillierKeyPair(PaillierpublicKey PaillierpublicKey,PaillierPrivateKey PaillierPrivateKey) {
		this.privateKey = PaillierPrivateKey;
		this.publicKey = PaillierpublicKey;
	}

	public PaillierPrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PaillierPrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PaillierpublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PaillierpublicKey publicKey) {
		this.publicKey = publicKey;
	}
}
