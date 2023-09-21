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

package org.dromara.hutool.json;

import org.dromara.hutool.core.date.DateUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * 用于测试1970年前的日期（负数）还有int类型的数字转日期可能导致的转换失败问题。
 */
public class Issue677Test {

	@Test
	public void toBeanTest(){
		final AuditResultDto dto = new AuditResultDto();
		dto.setDate(DateUtil.date(-1497600000));

		final String jsonStr = JSONUtil.toJsonStr(dto);
		final AuditResultDto auditResultDto = JSONUtil.toBean(jsonStr, AuditResultDto.class);
		Assertions.assertEquals("Mon Dec 15 00:00:00 CST 1969", auditResultDto.getDate().toString().replace("GMT+08:00", "CST"));
	}

	@Data
	public static class AuditResultDto{
		private Date date;
	}
}
