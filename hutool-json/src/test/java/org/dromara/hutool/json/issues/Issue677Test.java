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

package org.dromara.hutool.json.issues;

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
