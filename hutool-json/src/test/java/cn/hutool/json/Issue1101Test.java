package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * 测试转换为TreeSet是否成功。 TreeSet必须有默认的比较器
 */
public class Issue1101Test {

	@Test
	public void treeMapConvertTest(){
		String json = "[{\"nodeName\":\"admin\",\"treeNodeId\":\"00010001_52c95b83-2083-4138-99fb-e6e21f0c1277\",\"sort\":0,\"type\":10,\"parentId\":\"00010001\",\"children\":[],\"id\":\"52c95b83-2083-4138-99fb-e6e21f0c1277\",\"status\":true},{\"nodeName\":\"test\",\"treeNodeId\":\"00010001_97054a82-f8ff-46a1-b76c-cbacf6d18045\",\"sort\":0,\"type\":10,\"parentId\":\"00010001\",\"children\":[],\"id\":\"97054a82-f8ff-46a1-b76c-cbacf6d18045\",\"status\":true}]";
		final JSONArray objects = JSONUtil.parseArray(json);
		final TreeSet<TreeNodeDto> convert = Convert.convert(new TypeReference<TreeSet<TreeNodeDto>>() {
		}, objects);
		Assert.assertEquals(2, convert.size());
	}

	@Test
	public void test(){
		String json = "{\n" +
				"\t\"children\": [{\n" +
				"\t\t\"children\": [],\n" +
				"\t\t\"id\": \"52c95b83-2083-4138-99fb-e6e21f0c1277\",\n" +
				"\t\t\"nodeName\": \"admin\",\n" +
				"\t\t\"parentId\": \"00010001\",\n" +
				"\t\t\"sort\": 0,\n" +
				"\t\t\"status\": true,\n" +
				"\t\t\"treeNodeId\": \"00010001_52c95b83-2083-4138-99fb-e6e21f0c1277\",\n" +
				"\t\t\"type\": 10\n" +
				"\t}, {\n" +
				"\t\t\"children\": [],\n" +
				"\t\t\"id\": \"97054a82-f8ff-46a1-b76c-cbacf6d18045\",\n" +
				"\t\t\"nodeName\": \"test\",\n" +
				"\t\t\"parentId\": \"00010001\",\n" +
				"\t\t\"sort\": 0,\n" +
				"\t\t\"status\": true,\n" +
				"\t\t\"treeNodeId\": \"00010001_97054a82-f8ff-46a1-b76c-cbacf6d18045\",\n" +
				"\t\t\"type\": 10\n" +
				"\t}],\n" +
				"\t\"id\": \"00010001\",\n" +
				"\t\"nodeName\": \"测试\",\n" +
				"\t\"parentId\": \"0001\",\n" +
				"\t\"sort\": 0,\n" +
				"\t\"status\": true,\n" +
				"\t\"treeNodeId\": \"00010001\",\n" +
				"\t\"type\": 0\n" +
				"}";

		final JSONObject jsonObject = JSONUtil.parseObj(json);

		final TreeNode treeNode = JSONUtil.toBean(jsonObject, TreeNode.class);
		Assert.assertEquals(2, treeNode.getChildren().size());

		TreeNodeDto dto = new TreeNodeDto();
		BeanUtil.copyProperties(treeNode, dto, true);
		Assert.assertEquals(2, dto.getChildren().size());
	}

	@Data
	public static class TreeNodeDto {
		private String id;
		private String parentId;
		private int sort;
		private String nodeName;
		private int type;
		private Boolean status;
		private String treeNodeId;
		private TreeSet<TreeNodeDto> children = new TreeSet<>(Comparator.comparing(o -> o.id));
	}

	@Data
	public static class TreeNode implements Comparable<TreeNode> {
		private String id;
		private String parentId;
		private int sort;
		private String nodeName;
		private int type;
		private Boolean status;
		private String treeNodeId;
		private TreeSet<TreeNode> children = new TreeSet<>();

		@Override
		public int compareTo(TreeNode o) {
			return id.compareTo(o.getId());
		}
	}
}
