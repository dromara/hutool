package cn.hutool.swing.captcha;

import cn.hutool.swing.captcha.generator.MathGenerator;
import org.junit.jupiter.api.Test;

public class GeneratorTest {

	@Test
	public void mathGeneratorTest() {
		final MathGenerator mathGenerator = new MathGenerator();
		for (int i = 0; i < 1000; i++) {
			mathGenerator.verify(mathGenerator.generate(), "0");
		}
	}
}
