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

package org.dromara.hutool.json.test.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonNode implements Serializable {
	private static final long serialVersionUID = -2280206942803550272L;

	private Long id;
	private Integer parentId;
	private String name;

	public JsonNode() {
	}

	public JsonNode(final Long id, final Integer parentId, final String name) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}
}
