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

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Issues1881Test {

	@Accessors(chain = true)
	@Data
	static class ThingsHolderContactVO implements Serializable {

		private static final long serialVersionUID = -8727337936070932370L;
		private Long id;
		private Integer type;
		private String memberId;
		private String name;
		private String phone;
		private String avatar;
		private Long createTime;
	}

	@Test
	public void parseTest(){
		final List<ThingsHolderContactVO> holderContactVOList = new ArrayList<>();
		holderContactVOList.add(new ThingsHolderContactVO().setId(1L).setName("1"));
		holderContactVOList.add(new ThingsHolderContactVO().setId(2L).setName("2"));

		Assertions.assertEquals("[{\"id\":1,\"name\":\"1\"},{\"id\":2,\"name\":\"2\"}]", JSONUtil.parseArray(holderContactVOList).toString());
	}
}
