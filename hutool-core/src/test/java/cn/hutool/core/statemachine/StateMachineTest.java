package cn.hutool.core.statemachine;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 状态机单元测试
 */
public class StateMachineTest {

	/**
	 * 普通情况测试
	 */
	@Test
	public void testNormal(){
		Order order1 = new Order();
		order1.paySuccess();
		order1.sendOut();
		order1.refund();
		Assert.assertEquals(MyStatus.CLOSE, order1.status);

		Order order2 = new Order();
		order2.paySuccess();
		order2.sendOut();
		order2.accept();
		Assert.assertEquals(MyStatus.BUY_SUCCESS, order2.status);
	}

	/**
	 * 自旋测试
	 */
	@Test
	public void testSpin(){
		Order order2 = new Order();
		order2.paySuccess();
		order2.sendOut();
		order2.sendOut();
		Assert.assertEquals(MyStatus.SEND_OUT, order2.status);
	}

	@Test
	public void testDefine(){
		StateMachine<MyStatus, MyEvent> stateMachine = new StateMachine<>();
		try {
			stateMachine.setStartStatus(MyStatus.WAIT_PAY)
					.when(MyStatus.WAIT_PAY).occur(MyEvent.PAY_SUCCESS).then(MyStatus.PAYED)
					.when(MyStatus.WAIT_PAY).occur(MyEvent.PAY_OUT_OF_TIME).then(MyStatus.CLOSE)
					.when(MyStatus.WAIT_PAY).occur(MyEvent.PAY_OUT_OF_TIME).then(MyStatus.CLOSE)
					.when(MyStatus.PAYED).occur(MyEvent.PAY_SUCCESS).then(MyStatus.PAYED)
					.when(MyStatus.PAYED).occur(MyEvent.SEND_OUT_SUCCESS).then(MyStatus.SEND_OUT)
					.when(MyStatus.SEND_OUT).occur(MyEvent.ACCEPT).then(MyStatus.BUY_SUCCESS)
					.when(MyStatus.SEND_OUT).occur(MyEvent.REFUND, MyEvent.REJECTED).then(MyStatus.CLOSE);
		}catch (RuntimeException e) {
			Assert.assertEquals(e.getMessage(),"WAIT_PAY定义了重复的事件PAY_OUTOF_TIME");
		}
	}

	public static class Order {
		private String orderId;
		private BigDecimal buyAmount;
		private MyStatus status;
		public Order() {
			this.status = MyStatus.start();
		}
		public void paySuccess() {
			this.status = this.status.on(MyEvent.PAY_SUCCESS);
		}
		public void sendOut() {
			this.status = this.status.on(MyEvent.SEND_OUT_SUCCESS);
		}
		public void refund() {
			this.status = this.status.on(MyEvent.REFUND);
		}
		public void accept(){
			this.status = this.status.on(MyEvent.ACCEPT);
		}
	}

	public enum MyStatus {
		WAIT_PAY, //待支付
		PAYED, //支付成功
		SEND_OUT, //已发货
		BUY_SUCCESS, //购买成功
		CLOSE;//订单关闭

		private static StateMachine<MyStatus, MyEvent> STATE_MACHINE = new StateMachine<>();
		static {
			STATE_MACHINE.setStartStatus(MyStatus.WAIT_PAY)
					.when(MyStatus.WAIT_PAY).occur(MyEvent.PAY_SUCCESS).then(MyStatus.PAYED)
					.when(MyStatus.WAIT_PAY).occur(MyEvent.PAY_OUT_OF_TIME).then(MyStatus.CLOSE)
					.when(MyStatus.PAYED).occur(MyEvent.PAY_SUCCESS).then(MyStatus.PAYED)
					.when(MyStatus.PAYED).occur(MyEvent.SEND_OUT_SUCCESS).then(MyStatus.SEND_OUT)
					.when(MyStatus.SEND_OUT).occur(MyEvent.ACCEPT).then(MyStatus.BUY_SUCCESS)
					.when(MyStatus.SEND_OUT).occur(MyEvent.REFUND,MyEvent.REJECTED).then(MyStatus.CLOSE);
			//另外一种定义形式
//				STATE_MACHINE.setStartStatus(MyStatus.WAIT_PAY)
//					.addRule(MyStatus.WAIT_PAY, MyStatus.PAYED, MyEvent.PAY_SUCCESS)
//					.addRule(MyStatus.WAIT_PAY, MyStatus.CLOSE, MyEvent.PAY_OUTOF_TIME)
//					.addRule(MyStatus.WAIT_PAY, MyStatus.CLOSE, MyEvent.PAY_OUTOF_TIME)
//					.addRule(MyStatus.PAYED, MyStatus.SEND_OUT, MyEvent.SEND_OUT_SUCCESS)
//					.addRule(MyStatus.SEND_OUT, MyStatus.BUY_SUCCESS, MyEvent.ACCEPT)
//					.addRule(MyStatus.SEND_OUT, MyStatus.CLOSE, MyEvent.REFUND,MyEvent.REJECTED);
		}

		public MyStatus on(MyEvent event) {
			return STATE_MACHINE.on(this,event);
		}
		public static MyStatus start(){
			return STATE_MACHINE.start();
		}
	}
	public enum MyEvent {
		PAY_OUT_OF_TIME, //支付超时
		PAY_SUCCESS,  //支付成功
		SEND_OUT_SUCCESS, //发货成功
		ACCEPT, //确认收货
		REFUND, //退货
		REJECTED//拒收
	}

}
