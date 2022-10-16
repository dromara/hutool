package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;

/**
 * @author martin
 * @create 2022-10-09 16:25
 */
public class CollectionTest {
	@Test
    public void test() {
        List<Integer> list=new ArrayList<>();
        list.add(3);
        list.add(5);
        list.add(1);
        list.add(2);
        list.add(7);
        list.add(4);
        //排序工具
        Collections.sort(list);
        //返回断开位置，如果返回size-1证明未断开
        int maxLength = findMaxLength(list);
		Assert.assertEquals(1,maxLength);

    }
    public int findMaxLength(List<Integer> nums) {
        //集合长度
        int n = nums.size();
        //遍历
        for (int i = 0; i < n; i++) {
            //下一指针
            int next=i+1;
            if (next>=n){
                break;
            }
            //当前值
            Integer num = nums.get(i);
            //下一值
            Integer nextNum = nums.get(next);
            if (num == null||nextNum==null){
                return i;
            }
            if (nextNum-num!=1){
                return i;
            }

        }
        return n-1;
    }
}
