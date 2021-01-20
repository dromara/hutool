package cn.hutool.log.dialect.tinylog;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * <a href="http://www.tinylog.org/">TinyLog</a> log.<br>
 * 
 * @author Looly
 *
 */
public class TinyLogFactory extends LogFactory {
	
	/**
	 * 构造
	 */
	public TinyLogFactory() {
		super("TinyLog");
		checkLogExist(org.pmw.tinylog.Logger.class);
	}

	@Override
	public Log createLog(String name) {
		return new TinyLog(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new TinyLog(clazz);
	}

}
