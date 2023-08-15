/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.log.engine.tinylog;

import org.dromara.hutool.log.AbsLogEngine;
import org.dromara.hutool.log.Log;

/**
 * <a href="http://www.tinylog.org/">TinyLog2</a> log.<br>
 *
 * @author Looly
 *
 */
public class TinyLog2Engine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public TinyLog2Engine() {
		super("TinyLog");
		checkLogExist(org.tinylog.Logger.class);
	}

	@Override
	public Log createLog(final String name) {
		return new TinyLog2(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new TinyLog2(clazz);
	}

}
