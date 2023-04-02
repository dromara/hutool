package org.dromara.hutool.core.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

	@Test
	public void checkIsBuiltTest(){
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			final TreeBuilder<Integer> of = TreeBuilder.of(0);
			of.build();
			of.append(new ArrayList<>());
		});
	}
}
