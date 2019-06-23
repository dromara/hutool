package cn.hutool.cache.test;

import cn.hutool.cache.impl.SimpleCache;
import cn.hutool.core.util.babel.Supplier;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SimpleCacheTest {
    private static final String PREFIX="normalPrefix";
    private static final String SUPPLIER_PREFIX="supplierPrefix";
    private static final int TEST_COUNT=10000;
    @Test
    public void canCleanByItSelf() {
        SimpleCache<String,String> softValueHashMap = new SimpleCache<>();
        for (int i = 0; i < TEST_COUNT; i++) {
            softValueHashMap.put(i + "", PREFIX+i);
        }

        //缓存如果存在，就必须保证正确
        for (int i = 0; i < TEST_COUNT; i++) {
            String o = softValueHashMap.get(i + "");
            if (o!=null){
             Assert.assertEquals(o,PREFIX+i);
            }
        }


        List list = new ArrayList();
        int count=0;
        while (true) {
            try {
                byte[] bytes = new byte[1024 * 1024];
                list.add(bytes);
            } catch (Throwable e) {
                System.out.println(e);
                System.gc();
                break;
            }
            if (count>100000){
                break;
            }
        }
        //缓存应当发生自动清除
        for (int i = 0; i < TEST_COUNT; i++) {
            Assert.assertTrue(softValueHashMap.size() < TEST_COUNT);
        }

        //如果缓存不存在，那么supplier要生效
        for (int i = 0; i < TEST_COUNT; i++) {
            final int fi=i;
            String o = softValueHashMap.getOrDefault(i + "", new Supplier<String>() {
                @Override
                public String get() {
                    return SUPPLIER_PREFIX+fi;
                }
            });
            if (!(PREFIX + i).equals(o)){
                Assert.assertEquals(o,SUPPLIER_PREFIX+fi);
            }
        }

    }
}
