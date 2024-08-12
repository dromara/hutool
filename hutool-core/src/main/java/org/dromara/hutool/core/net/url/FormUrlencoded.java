/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.codec.PercentCodec;

/**
 * application/x-www-form-urlencoded，遵循W3C HTML Form content types规范，如空格须转+，+须被编码<br>
 * 规范见：<a href="https://url.spec.whatwg.org/#urlencoded-serializing">https://url.spec.whatwg.org/#urlencoded-serializing</a>
 *
 * @since 5.7.16
 */
public class FormUrlencoded {

	/**
	 * query中的value，默认除"-", "_", ".", "*"外都编码<br>
	 * 这个类似于JDK提供的{@link java.net.URLEncoder}
	 */
	public static final PercentCodec ALL = PercentCodec.Builder.of(RFC3986.UNRESERVED)
			.removeSafe('~').addSafe('*').setEncodeSpaceAsPlus(true).build();
}
