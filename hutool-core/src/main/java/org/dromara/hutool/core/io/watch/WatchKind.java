/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.io.watch;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * 监听事件类型枚举，包括：
 *
 * <pre>
 *      1. 事件丢失 OVERFLOW -》StandardWatchEventKinds.OVERFLOW
 *      2. 修改事件 MODIFY   -》StandardWatchEventKinds.ENTRY_MODIFY
 *      3. 创建事件 CREATE   -》StandardWatchEventKinds.ENTRY_CREATE
 *      4. 删除事件 DELETE   -》StandardWatchEventKinds.ENTRY_DELETE
 * </pre>
 *
 * @author loolly
 * @since 5.1.0
 */
public enum WatchKind {

	/**
	 * 事件丢失
	 */
	OVERFLOW(StandardWatchEventKinds.OVERFLOW),
	/**
	 * 修改事件
	 */
	MODIFY(StandardWatchEventKinds.ENTRY_MODIFY),
	/**
	 * 创建事件
	 */
	CREATE(StandardWatchEventKinds.ENTRY_CREATE),
	/**
	 * 删除事件
	 */
	DELETE(StandardWatchEventKinds.ENTRY_DELETE);

	/**
	 * 全部事件
	 */
	public static final WatchEvent.Kind<?>[] ALL = {//
			OVERFLOW.getValue(),      //事件丢失
			MODIFY.getValue(), //修改
			CREATE.getValue(),  //创建
			DELETE.getValue()   //删除
	};

	private final WatchEvent.Kind<?> value;

	/**
	 * 构造
	 *
	 * @param value 事件类型
	 */
	WatchKind(final WatchEvent.Kind<?> value) {
		this.value = value;
	}

	/**
	 * 获取枚举对应的事件类型
	 *
	 * @return 事件类型值
	 */
	public WatchEvent.Kind<?> getValue() {
		return this.value;
	}
}
