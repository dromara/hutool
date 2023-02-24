package cn.hutool.core.bean.filler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 填充工具类
 * <p>
 * 多服务调用时候，聚合数据使用
 * 比如订单服务查询时，订单的用户相关参数需要填充，通过工具类可省去很多中间步骤
 *
 * @author pursue-wind
 */
public class FillerTest {

	Random random = new Random();

	/**
	 * 模拟feignClient 或者 dao 根据用户id列表查询UserName
	 *
	 * @param userIds 用户id列表
	 * @return 返回 Map{userId : userName}
	 */
	private Map<Integer, String> queryMapByIds(Collection<Integer> userIds) {
		return userIds.stream()
				.map(i -> User.builder().id(i).userName("用户" + i + "号").build())
				.collect(Collectors.toMap(User::getId, User::getUserName));
	}

	/**
	 * 填充前
	 * <p>
	 * Order [{id = 1, createUserId = x1, createUserName = null} ...]
	 * <p>
	 * 填充后
	 * <p>
	 * Order [{id = 1, createUserId = x1, createUserName = 用户x1号} ...]
	 */
	@Test
	public void fill() {
		List<Order> orders = IntStream.rangeClosed(1, 5)
				.mapToObj(i -> Order.builder().id(i).createUserId(random.nextInt(50)).build())
				.collect(Collectors.toList());
		System.out.println(orders);

		Filler.fill(
				() -> orders,                        // 数据列表
				userIds -> queryMapByIds(userIds),    // 根据 userIds 查询 userName 的函数
				Order::getCreateUserId, Order::setCreateUserName // 从查询出的 Map{userId : userName} 中 取出 Order.createUserId 对应的 userId 把 userName 赋值给 Order 对象的 createUserName
		);
		System.out.println(orders);

		Assert.assertNotNull(orders.get(orders.size() / 2).getCreateUserName());
	}


	/**
	 * 填充前
	 * <p>
	 * Order [{id = 1, createUserId = x1, createUserName = null, updateUserId = x2, updateUserName = null} ...]
	 * <p>
	 * 填充后
	 * <p>
	 * Order [{id = 1, createUserId = x1, createUserName = 用户x1号, updateUserId = x2, updateUserName = 用户x2号} ...]
	 */
	@Test
	public void fill2() {
		List<Order> orders = IntStream.rangeClosed(1, 5)
				.mapToObj(i -> Order.builder().id(i).createUserId(random.nextInt(50)).updateUserId(random.nextInt(50)).build())
				.collect(Collectors.toList());
		System.out.println(orders);

		Filler.fill(
				() -> orders,                            // 数据列表
				userIds -> queryMapByIds(userIds),    // 根据 userIds 查询 userName 的函数
				Order::getCreateUserId, Order::setCreateUserName, // 从查询出的 Map{userId : userName} 中 取出 Order.createUserId 对应的 userId 把 userName 赋值给 Order 对象的 createUserName
				Order::getUpdateUserId, Order::setUpdateUserName  // 同上
		);
		System.out.println(orders);

		Assert.assertNotNull(orders.get(orders.size() / 2).getCreateUserName());
		Assert.assertNotNull(orders.get(orders.size() / 2).getUpdateUserName());
	}

	/**
	 * 模拟 feignClient 或者 dao 根据 用户idcard列表 查询 IdCard
	 *
	 * @param idCardIds 用户idcard列表
	 * @return 返回 Map{idcardId : IdCard}
	 */
	private Map<Integer, IdCard> queryUserIdCardByIds(Collection<Integer> idCardIds) {
		return idCardIds.stream()
				.map(i -> IdCard.builder().id(i).username("名字" + i + "号").cardNo("身份证" + i + "号").build())
				.collect(Collectors.toMap(IdCard::getId, Function.identity()));
	}

	/**
	 * 填充前
	 * <p>
	 * User [{id = 1, createUserId = x, idCardName = null, idCardNo = null},{id = 2, idCardId = x, idCardName = null, idCardNo = null}...]
	 * <p>
	 * 填充后
	 * <p>
	 * User [{id = 1, idCardId = x, idCardName = 名字x号, idCardNo = 身份证x号},{id = 2, idCardId = x1, idCardName = 名字x1号, idCardNo = 身份证x1号}...]
	 */
	@Test
	public void fillMultiValue() {
		List<User> userList = IntStream.rangeClosed(1, 5)
				.mapToObj(i -> User.builder().id(i).idCardId(random.nextInt(50)).build())
				.collect(Collectors.toList());
		System.out.println(userList);

		/*
			Filler.<User, Integer, IdCard, String>fillMultiValue(
					() -> userList,                                          // 数据列表
					User::getIdCardId,                                       // 取出列表的每个idCardId
					userIds -> queryUserIdCardByIds(userIds),                // 取出的idCardList 去做查询
					ImmutableMap.of(
							IdCard::getUsername, User::setIdCardName,    	// 从查询出的IdCard中 取出 IdCard.username 赋值给 User 对象的 idCardName
							IdCard::getCardNo, User::setIdCardNo            // 从查询出的IdCard中 取出 IdCard.cardNo 赋值给 User 对象的 idCardNo
					)
			);
		*/
		Filler.<User, Integer, IdCard, String>fillMultiValue(
				() -> userList,                                          // 数据列表
				User::getIdCardId,                                       // 取出列表的每个idCardId
				userIds -> queryUserIdCardByIds(userIds),                // 取出的idCardList 去做查询
				new HashMap<Function<IdCard, String>, BiConsumer<User, String>>() {{
					put(IdCard::getUsername, User::setIdCardName);        // 从查询出的IdCard中 取出 IdCard.username 赋值给 User 对象的 idCardName
					put(IdCard::getCardNo, User::setIdCardNo);            // 从查询出的IdCard中 取出 IdCard.cardNo 赋值给 User 对象的 idCardNo
				}}
		);
		System.out.println(userList);
		Assert.assertNotNull(userList.get(userList.size() / 2).getIdCardNo());
		Assert.assertNotNull(userList.get(userList.size() / 2).getIdCardName());
	}

	@Data
	@AllArgsConstructor(staticName = "of")
	@Builder
	@NoArgsConstructor
	public static class User {
		private Integer id;
		private String userName;
		private Integer idCardId;
		private String idCardName;
		private String idCardNo;
	}


	@Data
	@AllArgsConstructor(staticName = "of")
	@Builder
	@NoArgsConstructor
	public static class Order {
		private Integer id;
		private Integer createUserId;
		private String createUserName;
		private Integer updateUserId;
		private String updateUserName;
	}


	@Data
	@AllArgsConstructor(staticName = "of")
	@Builder
	@NoArgsConstructor
	public static class IdCard {
		private Integer id;
		private String username;
		private String cardNo;
	}
}
