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

package org.dromara.hutool.core.lang.test.bean;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author 质量过关
 *
 */
@Data
public class ExamInfoDict implements Serializable {
	private static final long serialVersionUID = 3640936499125004525L;

	// 主键
	private Integer id; // 可当作题号
	// 试题类型 客观题 0主观题 1
	private Integer examType;
	// 试题是否作答
	private Integer answerIs;

	public Integer getId(final Integer defaultValue) {
		return this.id == null ? defaultValue : this.id;
	}
}
