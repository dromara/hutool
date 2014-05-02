package looly.github.hutool.db;

import java.util.HashMap;

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
}
