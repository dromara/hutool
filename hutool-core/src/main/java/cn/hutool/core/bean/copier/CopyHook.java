package cn.hutool.core.bean.copier;

import java.util.function.Supplier;

/**
 *
 * 属性拷贝钩子函数<br>
 *
 * eg:
 *   entity{
 *       //database
 *       private int gender; (男(0), 女(1))
 *   }
 *
 *   VO{
 *       //show
 *  *    private String gender; (男, 女)
 *   }
 *
 *   VO vo = BeanUtil.copyProperties(entity, VO::new, (entity, vo) -> {
 *       vo.setGender(Enum.getString(entity.getGender))
 *   });
 * 属性拷贝中返回的值有时候和数据库不相同, 加个钩子函数在拷贝过程中处理
 * @see cn.hutool.core.bean.BeanUtil#copyProperties(Object, Class, CopyHook, String...)
 * @see cn.hutool.core.bean.BeanUtil#copyProperties(Object, Supplier, CopyHook, String...)
 *
 * @author WangChen
 * @param <S> 拷贝源对象
 * @param <T> 拷贝目标对象
 **/
@FunctionalInterface
public interface CopyHook<S, T> {

    /**
     * 属性拷贝钩子方法
     * @param s 源对象
     * @param t 目标对象
     */
    void hook(S s, T t);
}
