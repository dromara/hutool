package cn.hutool.log.dialect.console;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * @author hongda.li 2022-04-27 09:55
 */
public class ConsoleColorLogFactory extends LogFactory {

	public ConsoleColorLogFactory(){
		super("Hutool Console Color Logging");
	}

	@Override
	public Log createLog(String name) {
		return new ConsoleColorLog(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new ConsoleColorLog(clazz);
	}
}
