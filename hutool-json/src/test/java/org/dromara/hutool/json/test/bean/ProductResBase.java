/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.test.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyan E-mail:wangyan@pospt.cn
 * @version 创建时间：2017年9月11日 上午9:33:01 类说明
 */
@Data
public class ProductResBase implements Serializable {
	private static final long serialVersionUID = -6708040074002451511L;
	/**
	 * 请求结果成功0
	 */
	public static final int REQUEST_RESULT_SUCCESS = 0;
	/**
	 * 请求结果失败 1
	 */
	public static final int REQUEST_RESULT_FIAL = 1;
	/**
	 * 成功code
	 */
	public static final String REQUEST_CODE_SUCCESS = "0000";

	/**
	 * 结果 成功0 失败1
	 */
	private int resResult = 0;
	private String resCode = "0000";
	private String resMsg = "success";

	/**
	 * 成本总计
	 */
	private Integer costTotal;
}
