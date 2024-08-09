package cn.hutool.core.lang.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TreeBuilderTest {

	@Test
	public void checkIsBuiltTest(){
		assertThrows(IllegalArgumentException.class, () -> {
			final TreeBuilder<Integer> of = TreeBuilder.of(0);
			of.build();
			of.append(new ArrayList<>());
		});
	}
}
