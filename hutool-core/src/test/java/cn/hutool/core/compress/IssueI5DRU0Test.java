package cn.hutool.core.compress;

import cn.hutool.core.util.ZipUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IssueI5DRU0Test {

	@Test
	@Ignore
	public void appendTest(){
		// https://gitee.com/dromara/hutool/issues/I5DRU0
		// 向zip中添加文件的时候，如果添加的文件的父目录已经存在，会报错。实际中目录存在忽略即可。
		ZipUtil.append(Paths.get("d:/test/zipTest.zip"), Paths.get("d:/test/zipTest"), StandardCopyOption.REPLACE_EXISTING);
	}
}
