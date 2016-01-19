package com.xiaoleilu.hutool.log.dialect;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 *  Apache Commons Logging
 * @author Looly
 *
 */
public class ApacheCommonsLogFactory extends LogFactory{
	
	public ApacheCommonsLogFactory() {
		super("Apache Common Logging");
	}

	@Override
	public Log getLog(String name) {
		return new ApacheCommonsLog(name);
	}

	@Override
	public Log getLog(Class<?> clazz) {
		return new ApacheCommonsLog(clazz);
	}

}
