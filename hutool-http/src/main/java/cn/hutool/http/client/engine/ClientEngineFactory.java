package cn.hutool.http.client.engine;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.log.StaticLog;

/**
 * Http客户端引擎工厂类
 *
 * @author looly
 * @since 6.0.0
 */
public class ClientEngineFactory {

	/**
	 * 获得单例的ClientEngine
	 *
	 * @return 单例的ClientEngine
	 */
	public static ClientEngine get() {
		return Singleton.get(ClientEngine.class.getName(), ClientEngineFactory::of);
	}

	/**
	 * 根据用户引入的HTTP客户端引擎jar，自动创建对应的拼音引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code ClientEngine}
	 */
	public static ClientEngine of() {
		final ClientEngine engine = doCreate();
		StaticLog.debug("Use [{}] Http Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的拼音引擎jar，自动创建对应的拼音引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@code EngineFactory}
	 */
	private static ClientEngine doCreate() {
		final ClientEngine engine = ServiceLoaderUtil.loadFirstAvailable(ClientEngine.class);
		if (null != engine) {
			return engine;
		}

		throw new HttpException("No http jar found ! Please add one of it to your project !");
	}
}
