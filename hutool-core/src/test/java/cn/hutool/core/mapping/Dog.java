package cn.hutool.core.mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XueRi
 * @since 2021/9/26
 * 狗
 */
@Data
public class Dog extends Animal {

	private int weight;

	private String color;

	private String gender;
}
