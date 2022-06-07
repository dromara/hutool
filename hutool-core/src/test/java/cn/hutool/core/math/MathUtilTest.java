package cn.hutool.core.math;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class MathUtilTest {
	@Test
	public void factorialTest(){
		long factorial = MathUtil.factorial(0);
		Assert.assertEquals(1, factorial);

		Assert.assertEquals(1L, MathUtil.factorial(1));
		Assert.assertEquals(1307674368000L, MathUtil.factorial(15));
		Assert.assertEquals(2432902008176640000L, MathUtil.factorial(20));

		factorial = MathUtil.factorial(5, 0);
		Assert.assertEquals(120, factorial);
		factorial = MathUtil.factorial(5, 1);
		Assert.assertEquals(120, factorial);

		Assert.assertEquals(5, MathUtil.factorial(5, 4));
		Assert.assertEquals(2432902008176640000L, MathUtil.factorial(20, 0));
	}

	@Test
	public void factorialTest2(){
		long factorial = MathUtil.factorial(new BigInteger("0")).longValue();
		Assert.assertEquals(1, factorial);

		Assert.assertEquals(1L, MathUtil.factorial(new BigInteger("1")).longValue());
		Assert.assertEquals(1307674368000L, MathUtil.factorial(new BigInteger("15")).longValue());
		Assert.assertEquals(2432902008176640000L, MathUtil.factorial(20));

		factorial = MathUtil.factorial(new BigInteger("5"), new BigInteger("0")).longValue();
		Assert.assertEquals(120, factorial);
		factorial = MathUtil.factorial(new BigInteger("5"), BigInteger.ONE).longValue();
		Assert.assertEquals(120, factorial);

		Assert.assertEquals(5, MathUtil.factorial(new BigInteger("5"), new BigInteger("4")).longValue());
		Assert.assertEquals(2432902008176640000L, MathUtil.factorial(new BigInteger("20"), BigInteger.ZERO).longValue());
	}
}
