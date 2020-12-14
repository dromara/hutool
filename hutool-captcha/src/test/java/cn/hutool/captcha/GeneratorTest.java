package cn.hutool.captcha;

import cn.hutool.captcha.generator.MathGenerator;
import org.junit.Test;

public class GeneratorTest {
	@Test
	public void mathGeneratorTest(){
		final MathGenerator mathGenerator = new MathGenerator();
		for (int i = 0; i < 1000; i++) {
			mathGenerator.verify(mathGenerator.generate(), "0");
		}
	}
}
