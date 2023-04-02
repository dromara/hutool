package org.dromara.hutool.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class MathUtilTest {
	@Test
	public void factorialTest(){
		long factorial = MathUtil.factorial(0);
		Assertions.assertEquals(1, factorial);

		Assertions.assertEquals(1L, MathUtil.factorial(1));
		Assertions.assertEquals(1307674368000L, MathUtil.factorial(15));
		Assertions.assertEquals(2432902008176640000L, MathUtil.factorial(20));

		factorial = MathUtil.factorial(5, 0);
		Assertions.assertEquals(120, factorial);
		factorial = MathUtil.factorial(5, 1);
		Assertions.assertEquals(120, factorial);

		Assertions.assertEquals(5, MathUtil.factorial(5, 4));
		Assertions.assertEquals(2432902008176640000L, MathUtil.factorial(20, 0));
	}

	@Test
	public void factorialTest2(){
		long factorial = MathUtil.factorial(new BigInteger("0")).longValue();
		Assertions.assertEquals(1, factorial);

		Assertions.assertEquals(1L, MathUtil.factorial(new BigInteger("1")).longValue());
		Assertions.assertEquals(1307674368000L, MathUtil.factorial(new BigInteger("15")).longValue());
		Assertions.assertEquals(2432902008176640000L, MathUtil.factorial(20));

		factorial = MathUtil.factorial(new BigInteger("5"), new BigInteger("0")).longValue();
		Assertions.assertEquals(120, factorial);
		factorial = MathUtil.factorial(new BigInteger("5"), BigInteger.ONE).longValue();
		Assertions.assertEquals(120, factorial);

		Assertions.assertEquals(5, MathUtil.factorial(new BigInteger("5"), new BigInteger("4")).longValue());
		Assertions.assertEquals(2432902008176640000L, MathUtil.factorial(new BigInteger("20"), BigInteger.ZERO).longValue());
	}
}
