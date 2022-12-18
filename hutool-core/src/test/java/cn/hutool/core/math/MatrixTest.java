package cn.hutool.core.math;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 矩阵单元测试
 *
 */
public class MatrixTest {

	@Test
	public void addMatrixTest() {
		double[][] a = new double[][] {
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};
		double[][] b = new double[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		double[][] c = new double[][] {
				{2, 4, 6},
				{8, 10, 12},
				{14, 16, 18}
		};
		Assert.assertArrayEquals(c, Matrix.addMatrix(a, b));
	}

	@Test
	public void subMatrixTest() {
		double[][] a = new double[][] {
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};
		double[][] b = new double[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		double[][] c = new double[][] {
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
		};
		Assert.assertArrayEquals(c, Matrix.subMatrix(a, b));
	}

	@Test
	public void mulMatrixTest() {
		double[][] a = new double[][] {
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};
		double[][] b = new double[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		BigDecimal[][] c = new BigDecimal[3][3];
		c[0][0] = new BigDecimal("30.0");
		c[0][1] = new BigDecimal("36.0");
		c[0][2] = new BigDecimal("42.0");
		c[1][0] = new BigDecimal("66.0");
		c[1][1] = new BigDecimal("81.0");
		c[1][2] = new BigDecimal("96.0");
		c[2][0] = new BigDecimal("102.0");
		c[2][1] = new BigDecimal("126.0");
		c[2][2] = new BigDecimal("150.0");
		Assert.assertArrayEquals(c, Matrix.mulMatrix(a, b));
	}

	@Test
	public void leftDivMatrixTest() {
		double[][] a = new double[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		double[][] b = new double[][] {
				{1, 1, 1},
				{0, 1, 1},
				{0, 0, 1}
		};
		BigDecimal[][] c = new BigDecimal[3][3];
		c[0][0] = new BigDecimal("-3.0");
		c[0][1] = new BigDecimal("-3.0");
		c[0][2] = new BigDecimal("-3.0");
		c[1][0] = new BigDecimal("-3.0");
		c[1][1] = new BigDecimal("-3.0");
		c[1][2] = new BigDecimal("-3.0");
		c[2][0] = new BigDecimal("7.0");
		c[2][1] = new BigDecimal("8.0");
		c[2][2] = new BigDecimal("9.0");
		Assert.assertArrayEquals(c, Matrix.leftDivMatrix(a, b));
	}

	@Test
	public void rightDivMatrixTest() {
		double[][] a = new double[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		double[][] b = new double[][] {
				{1, 1, 1},
				{0, 1, 1},
				{0, 0, 1}
		};
		BigDecimal[][] c = new BigDecimal[3][3];
		c[0][0] = new BigDecimal("1.0");
		c[0][1] = new BigDecimal("1.0");
		c[0][2] = new BigDecimal("1.0");
		c[1][0] = new BigDecimal("4.0");
		c[1][1] = new BigDecimal("1.0");
		c[1][2] = new BigDecimal("1.0");
		c[2][0] = new BigDecimal("7.0");
		c[2][1] = new BigDecimal("1.0");
		c[2][2] = new BigDecimal("1.0");
		Assert.assertArrayEquals(c, Matrix.rightDivMatrix(a, b));
	}

	@Test
	public void transposeMatrixTest() {
		double[][] a = new double[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
		};
		double[][] b = new double[][] {
				{1, 4, 7},
				{2, 5, 8},
				{3, 6, 9}
		};
		Assert.assertArrayEquals(b, Matrix.transposeMatrix(a));
	}

	@Test
	public void inverseMatrixTest() {
		double[][] a = new double[][] {
				{1, 1, 1},
				{0, 1, 1},
				{0, 0, 1}
		};
		double[][] b = new double[][] {
				{1, -1, 0},
				{0, 1, -1},
				{0, 0, 1}
		};
		double[][] c = Matrix.inverseMatrix(a);
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				c[i][j] += 0.0;
			}
		}
		Assert.assertArrayEquals(b, c);
	}
}
