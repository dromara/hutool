package cn.hutool.web;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.web.anno.Domain;
import cn.hutool.web.anno.VO;
import cn.hutool.web.gen.VoGenerator;
import lombok.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class VoGeneratorTest {

	@Data
	@AllArgsConstructor
	private static class UserEntity {
		@VO(pageIds = {UserDm.PageId.LIST, UserDm.PageId.DETAIL})
		private String username;

		@VO(pageIds = {UserDm.PageId.LIST, UserDm.PageId.DETAIL})
		private String realName;

		@VO(pageIds = {UserDm.PageId.LIST, UserDm.PageId.DETAIL})
		private String nickName;
	}

	@Data
	@Domain
	@AllArgsConstructor
	private static class Account {
		@VO(pageIds = {UserDm.PageId.LIST})
		private String phone;

		@VO(pageIds = {UserDm.PageId.LIST})
		private String email;
	}

	@Data
	@Domain
	@AllArgsConstructor
	private static class Photo {
		@VO(pageIds = {UserDm.PageId.DETAIL})
		private String url;

		@VO(pageIds = {UserDm.PageId.DETAIL})
		private String size;
	}

	@Getter
	@Setter
	private static class UserDm extends UserEntity {
		public UserDm(UserEntity userEntity) {
			super(userEntity.getUsername(), userEntity.getRealName(), userEntity.getNickName());
		}

		public static class PageId {
			public final static String LIST = "UserDm.PageId.LIST";
			public final static String DETAIL = "UserDm.PageId.DETAIL";
		}

		private String userLevel;
		private String address;

		@VO(pageIds = {UserDm.PageId.LIST})
		private Account account;

		@VO(pageIds = {PageId.DETAIL})
		private List<Photo> photos;
	}

	@Test
	public void voBuilderTest() {

		/* 声明：为了更直观的查看效果，不使用断言，而是采用控制台直接打印结果的方式 */

		UserEntity userEntity = new UserEntity("zhangsan", "张三", "爱你的张三");
		Account account = new Account("13611112222", "email@foxmail.com");
		List<Photo> photos = Arrays.asList(
				new Photo("https://www.baidu.com", "1920*1080"),
				new Photo("https://www.baidu.com", "1920*1080"),
				new Photo("https://www.baidu.com", "1920*1080")
		);

		UserDm dm = new UserDm(userEntity);
		dm.setAccount(account);
		dm.setPhotos(photos);
		dm.setUserLevel("一级");
		dm.setAddress("广东省 汕头市");

		System.out.println("===================  分 隔 线 : 只有标注 @VO 注解并且指定的 PageIds 包含 \"UserDm.PageId.LIST\" 才构建到 VO 中 ==================================\n");
		final JSONObject listVo = new VoGenerator<UserDm>(dm)
				.withPageIds(UserDm.PageId.LIST)
				.put("你是谁？", "我是张三。")
				.genVoObj();
		System.out.println(listVo.toStringPretty());

		System.out.println("\n===================  分 隔 线 : 只有标注 @VO 注解并且指定的 PageIds 包含 \"UserDm.PageId.DETAIL\" 才构建到 VO 中 ==================================\n");

		final JSONObject detailVo = new VoGenerator<UserDm>(dm)
				.withPageIds(UserDm.PageId.DETAIL)
				.put("你到底是谁！", "我是张三！")
				.genVoObj();
		System.out.println(detailVo.toStringPretty());

		System.out.println("\n===================  分 隔 线 : 只有 userLevel、username 等字段才会被构建到 VO 中  ==================================\n");

		final JSONObject upuVo = new VoGenerator<>(dm)
				.withAttrs(UserDm::getUserLevel, UserEntity::getUsername)
				.put("你就是哪个张三吗？", "是的，我是。")
				.genVoObj();
		System.out.println(upuVo.toStringPretty());

		System.out.println("\n===================  分 隔 线 : 数组  ==================================\n");

		final JSONArray arr = new VoGenerator<>(photos)
				.put("你就是哪个张三吗？", "是的，没错，我就是哪个不按照套路出牌的张三")
				.withPageIds(UserDm.PageId.DETAIL)
				.genVoArr();
		System.out.println(arr.toStringPretty());
	}
}
