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

package org.dromara.hutool.extra.expression.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.expression.ExpressionEngine;
import org.dromara.hutool.extra.expression.ExpressionException;
import org.dromara.hutool.log.StaticLog;

/**
 * 表达式语言引擎工厂类，用于根据用户引入的表达式jar，自动创建对应的引擎对象
 *
 * @since 5.5.0
 * @author looly
 */
public class ExpressionFactory {

	/**
	 * 获得单例的{@link ExpressionEngine}
	 *
	 * @return 单例的{@link ExpressionEngine}
	 */
	public static ExpressionEngine get(){
		return Singleton.get(ExpressionEngine.class.getName(), ExpressionFactory::of);
	}

	/**
	 * 根据用户引入的表达式引擎jar，自动创建对应的拼音引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@link ExpressionEngine}
	 */
	public static ExpressionEngine of() {
		final ExpressionEngine engine = doCreate();
		StaticLog.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的拼音引擎jar，自动创建对应的拼音引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@link ExpressionEngine}
	 */
	private static ExpressionEngine doCreate() {
		final ExpressionEngine engine = SpiUtil.loadFirstAvailable(ExpressionEngine.class);
		if(null != engine){
			return engine;
		}

		throw new ExpressionException("No expression jar found !Please add one of it to your project !");
	}
}
