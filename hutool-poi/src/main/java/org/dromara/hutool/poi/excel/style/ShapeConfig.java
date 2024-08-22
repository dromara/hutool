/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.poi.excel.style;

import org.apache.poi.sl.usermodel.ShapeType;

import java.awt.Color;
import java.io.Serializable;

/**
 * 形状配置
 * 用于在Excel中定义形状的样式，包括形状类型、线条样式、线条宽度、线条颜色、填充颜色等
 *
 * @author Looly
 * @since 6.0.0
 */
public class ShapeConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建一个形状配置
	 *
	 * @return ShapeConfig
	 */
	public static ShapeConfig of() {
		return new ShapeConfig();
	}

	/**
	 * 形状类型，如矩形、圆形等，默认直线
	 */
	private ShapeType shapeType = ShapeType.LINE;
	/**
	 * 线条样式，如实线、虚线等，默认实线
	 */
	private LineStyle lineStyle = LineStyle.SOLID;
	/**
	 * 线条宽度，以磅为单位
	 */
	private int lineWidth = 1;
	/**
	 * 线条颜色
	 */
	private Color lineColor = Color.BLACK;
	/**
	 * 填充颜色，{@code null表示不填充}
	 */
	private Color fillColor;

	/**
	 * 获取形状类型
	 *
	 * @return 形状类型
	 */
	public ShapeType getShapeType() {
		return shapeType;
	}

	/**
	 * 设置形状类型
	 *
	 * @param shapeType 形状类型
	 * @return 当前形状配置对象，用于链式调用
	 */
	public ShapeConfig setShapeType(final ShapeType shapeType) {
		this.shapeType = shapeType;
		return this;
	}

	/**
	 * 获取线条样式
	 *
	 * @return 线条样式
	 */
	public LineStyle getLineStyle() {
		return lineStyle;
	}

	/**
	 * 设置线条样式
	 *
	 * @param lineStyle 线条样式
	 * @return 当前形状配置对象，用于链式调用
	 */
	public ShapeConfig setLineStyle(final LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}

	/**
	 * 获取线条宽度
	 *
	 * @return 线条宽度，以磅为单位
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * 设置线条宽度
	 *
	 * @param lineWidth 线条宽度，以磅为单位
	 * @return 当前形状配置对象，用于链式调用
	 */
	public ShapeConfig setLineWidth(final int lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	/**
	 * 获取线条颜色
	 *
	 * @return 线条颜色
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * 设置线条颜色
	 *
	 * @param lineColor 线条颜色
	 * @return 当前形状配置对象，用于链式调用
	 */
	public ShapeConfig setLineColor(final Color lineColor) {
		this.lineColor = lineColor;
		return this;
	}

	/**
	 * 获取填充颜色，{@code null表示不填充}
	 *
	 * @return 填充颜色，{@code null表示不填充}
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * 设置填充颜色，{@code null表示不填充}
	 *
	 * @param fillColor 填充颜色，{@code null表示不填充}
	 * @return 当前形状配置对象，用于链式调用
	 */
	public ShapeConfig setFillColor(final Color fillColor) {
		this.fillColor = fillColor;
		return this;
	}
}
