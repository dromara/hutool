package looly.github.hutool.db;

import java.util.HashMap;

import looly.github.hutool.Conver;
import looly.github.hutool.InjectUtil;
import looly.github.hutool.StrUtil;
import looly.github.hutool.exceptions.UtilException;

/**
 * 数据实体对象
 * @author loolly
 *
 */
public class Entity extends HashMap<String, Object>{
	private static final long serialVersionUID = -1951012511464327448L;
	
	/**
	 * 填充Value Object对象
	 * @param vo Value Object（或者POJO）
	 * @return vo
	 */
	public <T> T fillVo(T vo) {
		InjectUtil.injectFromMap(vo, this);
		return vo;
	}
	
	/**
	 * 填充Value Object对象
	 * @param vo Value Object（或者POJO）
	 * @return vo
	 */
	public <T> T toVo(Class<T> clazz) {
		if(clazz == null) {
			throw new NullPointerException("Provided Class is null!");
		}
		T vo;
		try {
			vo = clazz.newInstance();
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Instance Value Object [] error!", clazz.getName()));
		}
		InjectUtil.injectFromMap(vo, this);
		return vo;
	}
	
	//-------------------------------------------------------------------- 特定类型值
	/**
	 * @param key 键
	 * @return 获得字符串类型值
	 */
	public String getStr(String key) {
		return Conver.toStr(get(key), null);
	}
	
	/**
	 * @param key 键
	 * @return 获得字符串类型值
	 */
	public Integer getInt(String key) {
		return Conver.toInt(get(key), null);
	}
	
	/**
	 * @param key 键
	 * @return 获得字符串类型值
	 */
	public Long getLong(String key) {
		return Conver.toLong(get(key), null);
	}
}
