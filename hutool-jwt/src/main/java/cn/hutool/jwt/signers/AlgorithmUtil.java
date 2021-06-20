package cn.hutool.jwt.signers;

import cn.hutool.core.map.BiMap;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.HmacAlgorithm;

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

		map.put("RS256", SignAlgorithm.SHA256withRSA.getValue());
		map.put("RS384", SignAlgorithm.SHA384withRSA.getValue());
		map.put("RS512", SignAlgorithm.SHA512withRSA.getValue());

		map.put("ES256", SignAlgorithm.SHA256withECDSA.getValue());
		map.put("ES384", SignAlgorithm.SHA384withECDSA.getValue());
		map.put("ES512", SignAlgorithm.SHA512withECDSA.getValue());

		map.put("PS256", SignAlgorithm.SHA256withRSA_PSS.getValue());
		map.put("PS384", SignAlgorithm.SHA384withRSA_PSS.getValue());
		map.put("PS512", SignAlgorithm.SHA512withRSA_PSS.getValue());
	}

	/**
	 * 获取算法，用户传入算法ID返回算法名，传入算法名返回本身
	 * @param idOrAlgorithm 算法ID或算法名
	 * @return 算法名
	 */
	public static String getAlgorithm(String idOrAlgorithm){
		return ObjectUtil.defaultIfNull(getAlgorithmById(idOrAlgorithm), idOrAlgorithm);
	}

	/**
	 * 获取算法ID，用户传入算法名返回ID，传入算法ID返回本身
	 * @param idOrAlgorithm 算法ID或算法名
	 * @return 算法ID
	 */
	public static String getId(String idOrAlgorithm){
		return ObjectUtil.defaultIfNull(getIdByAlgorithm(idOrAlgorithm), idOrAlgorithm);
	}

	/**
	 * 根据JWT算法ID获取算法
	 *
	 * @param id JWT算法ID
	 * @return 算法
	 */
	private static String getAlgorithmById(String id) {
		return map.get(id.toUpperCase());
	}

	/**
	 * 根据算法获取JWT算法ID
	 *
	 * @param algorithm 算法
	 * @return JWT算法ID
	 */
	private static String getIdByAlgorithm(String algorithm) {
		return map.getInverse().get(algorithm);
	}
}
