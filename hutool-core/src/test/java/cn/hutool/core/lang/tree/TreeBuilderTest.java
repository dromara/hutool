package cn.hutool.core.lang.tree;

import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void checkIsBuiltTest(){
		final TreeBuilder<Integer> of = TreeBuilder.of(0);
		of.build();
		of.append(new ArrayList<>());
	}

}
