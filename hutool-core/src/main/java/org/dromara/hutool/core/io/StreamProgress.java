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

package org.dromara.hutool.core.io;

/**
 * Stream进度条<br>
 * 提供流拷贝进度监测，如开始、结束触发，以及进度回调。<br>
 * 注意进度回调的{@code total}参数为总大小，某些场景下无总大小的标记，则此值应为-1或者{@link Long#MAX_VALUE}，表示此参数无效。
 *
 * @author Looly
 */
public interface StreamProgress {

	/**
	 * 开始
	 */
	void start();

	/**
	 * 进行中
	 *
	 * @param total        总大小，如果未知为 -1或者{@link Long#MAX_VALUE}
	 * @param progressSize 已经进行的大小
	 */
	void progress(long total, long progressSize);

	/**
	 * 结束
	 */
	void finish();
}
