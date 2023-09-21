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

import org.dromara.hutool.core.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

/**
 * https://gitee.com/dromara/hutool/issues/I49VZB
 */
public class IssueI49VZBTest {
	public enum NBCloudKeyType {
		/**
		 * 指纹
		 */
		fingerPrint,
		/**
		 * 密码
		 */
		password,
		/**
		 * 卡片
		 */
		card,
		/**
		 * 临时密码
		 */
		snapKey;

		public static NBCloudKeyType find(final String value) {
			return Stream.of(values()).filter(e -> e.getValue().equalsIgnoreCase(value)).findFirst()
					.orElse(null);
		}


		public static NBCloudKeyType downFind(final String keyType) {
			if (fingerPrint.name().equals(keyType.toLowerCase())) {
				return NBCloudKeyType.fingerPrint;
			} else {
				return find(keyType);
			}
		}

		public String getValue() {
			return super.toString().toLowerCase();
		}

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class UPOpendoor  {

		private String keyId;
		private NBCloudKeyType type;
		private String time;
		private int result;

	}

	@Test
	public void toBeanTest(){
		final String str = "{type: \"password\"}";
		final UPOpendoor upOpendoor = JSONUtil.toBean(str, UPOpendoor.class);
		Assertions.assertEquals(NBCloudKeyType.password, upOpendoor.getType());
	}

	@Test
	public void enumConvertTest(){
		final NBCloudKeyType type = Convert.toEnum(NBCloudKeyType.class, "snapKey");
		Assertions.assertEquals(NBCloudKeyType.snapKey, type);
	}
}
