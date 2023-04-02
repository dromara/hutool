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

package org.dromara.hutool.jwt.signers;

import org.dromara.hutool.map.BiMap;
import org.dromara.hutool.util.ObjUtil;
import org.dromara.hutool.asymmetric.SignAlgorithm;
import org.dromara.hutool.digest.HmacAlgorithm;

import java.util.HashMap;

/**
 * 算法工具类，算法和JWT算法ID对应表
 *
 * @author looly
 * @since 5.7.0
 */
public class AlgorithmUtil {

	private static final BiMap<String, String> map;

	static {
		map = new BiMap<>(new HashMap<>());
		map.put("HS256", HmacAlgorithm.HmacSHA256.getValue());
		map.put("HS384", HmacAlgorithm.HmacSHA384.getValue());
		map.put("HS512", HmacAlgorithm.HmacSHA512.getValue());

		map.put("HMD5", HmacAlgorithm.HmacMD5.getValue());
		map.put("HSHA1", HmacAlgorithm.HmacSHA1.getValue());
		map.put("SM4CMAC", HmacAlgorithm.SM4CMAC.getValue());

		map.put("RS256", SignAlgorithm.SHA256withRSA.getValue());
		map.put("RS384", SignAlgorithm.SHA384withRSA.getValue());
		map.put("RS512", SignAlgorithm.SHA512withRSA.getValue());

		map.put("ES256", SignAlgorithm.SHA256withECDSA.getValue());
		map.put("ES384", SignAlgorithm.SHA384withECDSA.getValue());
		map.put("ES512", SignAlgorithm.SHA512withECDSA.getValue());

		map.put("PS256", SignAlgorithm.SHA256withRSA_PSS.getValue());
		map.put("PS384", SignAlgorithm.SHA384withRSA_PSS.getValue());
		map.put("PS512", SignAlgorithm.SHA512withRSA_PSS.getValue());

		map.put("RMD2", SignAlgorithm.MD2withRSA.getValue());
		map.put("RMD5", SignAlgorithm.MD5withRSA.getValue());
		map.put("RSHA1", SignAlgorithm.SHA1withRSA.getValue());
		map.put("DNONE", SignAlgorithm.NONEwithDSA.getValue());
		map.put("DSHA1", SignAlgorithm.SHA1withDSA.getValue());
		map.put("ENONE", SignAlgorithm.NONEwithECDSA.getValue());
		map.put("ESHA1", SignAlgorithm.SHA1withECDSA.getValue());
	}

	/**
	 * 获取算法，用户传入算法ID返回算法名，传入算法名返回本身
	 * @param idOrAlgorithm 算法ID或算法名
	 * @return 算法名
	 */
	public static String getAlgorithm(final String idOrAlgorithm){
		return ObjUtil.defaultIfNull(getAlgorithmById(idOrAlgorithm), idOrAlgorithm);
	}

	/**
	 * 获取算法ID，用户传入算法名返回ID，传入算法ID返回本身
	 * @param idOrAlgorithm 算法ID或算法名
	 * @return 算法ID
	 */
	public static String getId(final String idOrAlgorithm){
		return ObjUtil.defaultIfNull(getIdByAlgorithm(idOrAlgorithm), idOrAlgorithm);
	}

	/**
	 * 根据JWT算法ID获取算法
	 *
	 * @param id JWT算法ID
	 * @return 算法
	 */
	private static String getAlgorithmById(final String id) {
		return map.get(id.toUpperCase());
	}

	/**
	 * 根据算法获取JWT算法ID
	 *
	 * @param algorithm 算法
	 * @return JWT算法ID
	 */
	private static String getIdByAlgorithm(final String algorithm) {
		return map.getKey(algorithm);
	}
}
