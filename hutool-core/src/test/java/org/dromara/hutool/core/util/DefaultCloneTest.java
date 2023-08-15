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

package org.dromara.hutool.core.util;


import org.dromara.hutool.core.exception.CloneException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCloneTest {

	@Test
	public void clone0() {
		final Car oldCar = new Car();
		oldCar.setId(1);
		oldCar.setWheelList(Stream.of(new Wheel("h")).collect(Collectors.toList()));

		final Car newCar = oldCar.clone();
		Assertions.assertEquals(oldCar.getId(), newCar.getId());
		Assertions.assertEquals(oldCar.getWheelList(), newCar.getWheelList());

		newCar.setId(2);
		Assertions.assertNotEquals(oldCar.getId(), newCar.getId());
		newCar.getWheelList().add(new Wheel("s"));

		Assertions.assertNotSame(oldCar, newCar);

	}

	@Data
	static class Car implements Cloneable {
		private Integer id;
		private List<Wheel> wheelList;

		@Override
		public Car clone() {
			try {
				return (Car) super.clone();
			} catch (final CloneNotSupportedException e) {
				throw new CloneException(e);
			}
		}
	}

	@Data
	@AllArgsConstructor
	static class Wheel {
		private String direction;
	}

}


