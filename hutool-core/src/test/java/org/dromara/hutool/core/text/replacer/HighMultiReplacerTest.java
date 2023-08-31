package org.dromara.hutool.core.text.replacer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HighMultiReplacerTest {

    @Test
    public void replaceTest() {
        final Map<String, String> map = new HashMap<>();
        map.put("abc", "bar");
        map.put("123", "1234");
        map.put("12", "哈哈哈");
        map.put("bc", "67868");
        map.put("你好", "hello world");
        map.put("AAACC", "%^$%$");
        map.put("_啊", "qqqq");

        final HighMultiReplacer replacer = HighMultiReplacer.of(map);
        Assertions.assertEquals("bar哈哈哈hello world4", replacer.apply("abc12你好4").toString());
        Assertions.assertEquals("qqqq啊qqqq-啊", replacer.apply("_啊啊_啊-啊").toString());
        Assertions.assertEquals("哈哈哈3456789", replacer.apply("123456789").toString());
        Assertions.assertEquals("AAAC67868", replacer.apply("AAACbc").toString());
        Assertions.assertEquals("哈哈哈哈哈1哈哈", replacer.apply("哈哈121哈哈").toString());
        Assertions.assertEquals("你hello world好%^$%$CACAC", replacer.apply("你你好好AAACCCACAC").toString());
        Assertions.assertEquals("哈哈哈3", replacer.apply("123").toString());
        Assertions.assertEquals("---11", replacer.apply("---11").toString());
        Assertions.assertEquals("", replacer.apply("").toString());
    }
}
