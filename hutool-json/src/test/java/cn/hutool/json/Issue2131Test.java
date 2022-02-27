package cn.hutool.json;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.junit.Assert;
import org.junit.Test;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Stream;

/**
 * https://github.com/dromara/hutool/issues/2131<br>
 * 字段定义成final，意味着setCollections无效，因此JSON转Bean的时候无法调用setCollections注入，所以是空的。
 */
public class Issue2131Test {

	@Test
	public void strToBean() {
		GoodsResponse goodsResponse = new GoodsResponse();
		GoodsItem apple = new GoodsItem().setGoodsId(1L).setGoodsName("apple").setChannel("wechat");
		GoodsItem pear = new GoodsItem().setGoodsId(2L).setGoodsName("pear").setChannel("jd");
		final List<GoodsItem> collections = goodsResponse.getCollections();
		Stream.of(apple, pear).forEach(collections::add);

		String jsonStr = JSONUtil.toJsonStr(goodsResponse);
		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

		GoodsResponse result = jsonObject.toBean(GoodsResponse.class);
		Assert.assertEquals(0, result.getCollections().size());
	}

	@Data
	static class BaseResponse {

		@SuppressWarnings("unused")
		@Transient
		public final boolean successful() {
			return code == 200 || code == 201;
		}

		private int code = 200;
		private String message;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	static class GoodsResponse extends BaseResponse {
		// 由于定义成了final形式，setXXX无效，导致无法注入。
		private final List<GoodsItem> collections = ListUtil.list(false);
	}

	@Data
	@Accessors(chain = true)
	static class GoodsItem{
		private long goodsId;
		private String goodsName;
		private String channel;
	}
}
