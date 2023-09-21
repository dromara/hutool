package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IssueI81N3HTest {

	@Test
	void sortTest() {
		final List<String> list = IoUtil.readUtf8Lines(ResourceUtil.getStream("IssueI81N3H.list"), new ArrayList<>());
		ListUtil.sort(list, VersionComparator.INSTANCE.reversed());
	}
}
