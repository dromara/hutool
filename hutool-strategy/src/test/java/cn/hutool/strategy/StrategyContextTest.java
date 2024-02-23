package cn.hutool.strategy;


import org.junit.Assert;
import org.junit.Test;

public class StrategyContextTest {

	@Test
	public void strategyExecute(){
		StrategyContext<Integer, Integer, String> strategyContext = new StrategyContext<>("add");

		Strategy<Integer, Integer, String> strategyA = new Strategy<Integer, Integer, String>() {

			@Override
			public Integer execute(Integer request) {
				return request + 1976;
			}
			@Override
			public String getType() {
				return "strategyA";
			}
		};
		Strategy<Integer, Integer, String> strategyB = new Strategy<Integer, Integer, String>() {

			@Override
			public Integer execute(Integer request) {
				return request + 1978;
			}
			@Override
			public String getType() {
				return "strategyB";
			}
		};
		strategyContext.register(strategyA);
		strategyContext.register(strategyB);
		Integer aResult = strategyContext.execute("strategyA", 2);
		Integer bResult = strategyContext.execute("strategyB", 2);

		Assert.assertEquals(1976 + 2, aResult.intValue());
		Assert.assertEquals(1978 + 2, bResult.intValue());
	}


	@Test
	public void lockStrategyExecute(){
		LockStrategyContext<Integer, Integer, String> strategyContext = new LockStrategyContext<>("add");

		Strategy<Integer, Integer, String> strategyA = new Strategy<Integer, Integer, String>() {

			@Override
			public Integer execute(Integer request) {
				return request + 1976;
			}
			@Override
			public String getType() {
				return "strategyA";
			}
		};
		Strategy<Integer, Integer, String> strategyB = new Strategy<Integer, Integer, String>() {

			@Override
			public Integer execute(Integer request) {
				return request + 1978;
			}
			@Override
			public String getType() {
				return "strategyB";
			}
		};
		strategyContext.register(strategyA);
		strategyContext.register(strategyB);
		Integer aResult = strategyContext.execute("strategyA", 2);
		Integer bResult = strategyContext.execute("strategyB", 2);

		strategyContext.unregister(strategyA.getType());

		Assert.assertThrows(IllegalArgumentException.class, () -> strategyContext.execute("strategyA", 2));
		Assert.assertEquals(1976 + 2, aResult.intValue());
		Assert.assertEquals(1978 + 2, bResult.intValue());
	}


}
