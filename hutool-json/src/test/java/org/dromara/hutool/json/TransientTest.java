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

package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransientTest {

	@Data
	static class Bill{
		private transient String id;
		private String bizNo;
	}

	@Test
	public void beanWithoutTransientTest(){
		final Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		//noinspection MismatchedQueryAndUpdateOfCollection
		final OldJSONObject jsonObject = new OldJSONObject(detailBill,
				JSONConfig.of().setTransientSupport(false));
		Assertions.assertEquals("{\"id\":\"3243\",\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}

	@Test
	public void beanWithTransientTest(){
		final Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		//noinspection MismatchedQueryAndUpdateOfCollection
		final OldJSONObject jsonObject = new OldJSONObject(detailBill,
				JSONConfig.of().setTransientSupport(true));
		Assertions.assertEquals("{\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}

	@Test
	public void beanWithoutTransientToBeanTest(){
		final Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		final OldJSONObject jsonObject = new OldJSONObject(detailBill,
				JSONConfig.of().setTransientSupport(false));

		final Bill bill = jsonObject.toBean(Bill.class);
		Assertions.assertEquals("3243", bill.getId());
		Assertions.assertEquals("bizNo", bill.getBizNo());
	}

	@Test
	public void beanWithTransientToBeanTest(){
		final Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		final OldJSONObject jsonObject = new OldJSONObject(detailBill,
				JSONConfig.of().setTransientSupport(true));

		final Bill bill = jsonObject.toBean(Bill.class);
		Assertions.assertNull(bill.getId());
		Assertions.assertEquals("bizNo", bill.getBizNo());
	}
}
