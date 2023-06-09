/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.crypto.asymmetric.paillier;

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
