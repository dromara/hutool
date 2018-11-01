package cn.hutool.core.thread;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Rule;

public class ThreadUtilTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void executeTest() {
		final boolean isValid = true;
		
		ThreadUtil.execute(new Runnable() {
			
			@Override
			public void run() {
				Assert.assertTrue(isValid);
			}
		});
		
	}

	@Test
	public void newExecutorByBlockingCoefficientInputPositiveOutputIllegalArgumentException() {

		// Arrange
		final float blockingCoefficient = 2.15625f;

		// Act
		thrown.expect(IllegalArgumentException.class);
		ThreadUtil.newExecutorByBlockingCoefficient(blockingCoefficient);

		// Method is not expected to return due to exception thrown
	}

	@Test
	public void newExecutorByBlockingCoefficientInputNegativeOutputIllegalArgumentException() {
  
	  // Arrange
	  final float blockingCoefficient = -1.3125f;
  
	  // Act
	  thrown.expect(IllegalArgumentException.class);
	  ThreadUtil.newExecutorByBlockingCoefficient(blockingCoefficient);
  
	  // Method is not expected to return due to exception thrown
	}

	@Test
	public void safeSleepInputZeroOutputTrue() {
  
	  // Arrange
	  final Number millis = 0;
  
	  // Act
	  final boolean retval = ThreadUtil.safeSleep(millis);
  
	  // Assert result
	  Assert.assertEquals(true, retval);
	}
}
