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
		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.of().setTransientSupport(false));
		Assertions.assertEquals("{\"id\":\"3243\",\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}

	@Test
	public void beanWithTransientTest(){
		final Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.of().setTransientSupport(true));
		Assertions.assertEquals("{\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}

	@Test
	public void beanWithoutTransientToBeanTest(){
		final Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		final JSONObject jsonObject = new JSONObject(detailBill,
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

		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.of().setTransientSupport(true));

		final Bill bill = jsonObject.toBean(Bill.class);
		Assertions.assertNull(bill.getId());
		Assertions.assertEquals("bizNo", bill.getBizNo());
	}
}
