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


