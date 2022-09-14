package cn.hutool.core.lang.ansi;

/**
 * ANSI可转义节点接口，实现为ANSI颜色等
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb
 */
public interface AnsiElement {

	/**
	 * 获取ANSI代码
	 * @return ANSI代码
	 * @since 5.8.7
	 */
	int getCode();

	/**
	 * @return ANSI转义编码
	 */
	@Override
	String toString();

}
