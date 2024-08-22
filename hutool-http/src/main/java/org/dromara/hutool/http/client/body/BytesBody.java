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

package org.dromara.hutool.http.client.body;

import org.dromara.hutool.core.io.resource.BytesResource;
import org.dromara.hutool.core.io.resource.HttpResource;

/**
 * bytes类型的Http request body，主要发送编码后的表单数据或rest body（如JSON或XML）
 *
 * @since 5.7.17
 * @author looly
 */
public class BytesBody extends ResourceBody {

	/**
	 * 创建 Http request body
	 * @param content body内容，编码后
	 * @return BytesBody
	 */
	public static BytesBody of(final byte[] content){
		return new BytesBody(content);
	}

	/**
	 * 构造
	 *
	 * @param content Body内容，编码后
	 */
	public BytesBody(final byte[] content) {
		super(new HttpResource(new BytesResource(content), null));
	}
}
