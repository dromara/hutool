package cn.hutool.core.math;

import java.math.BigDecimal;

public class Matrix {
    /**
     * 矩阵加法
     * @param a 矩阵a
     * @param b 矩阵b
     * @return 矩阵a+b
     */
    public static double[][] addMatrix(double[][] a, double[][] b) {
        double[][] c = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    /**
     * 矩阵减法
     * @param a 矩阵a
     * @param b 矩阵b
     * @return 矩阵a-b
     */
    public static double[][] subMatrix(double[][] a, double[][] b) {
        double[][] c = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }

    /**
     * 矩阵乘法(多线程)
     * @param a 矩阵a
     * @param b 矩阵b
     * @return 矩阵a*b
     */
    public static BigDecimal[][] mulMatrix(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            try {
                throw new Exception("矩阵乘法不可行");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BigDecimal[][] c = new BigDecimal[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                c[i][j] = BigDecimal.ZERO;
            }
        }
        int threadNum = 4;
        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                for (int j = finalI * a.length / threadNum; j < (finalI + 1) * a.length / threadNum; j++) {
                    for (int k = 0; k < b[0].length; k++) {
                        for (int l = 0; l < a[0].length; l++) {
                            c[j][k] = c[j][k].add(BigDecimal.valueOf(a[j][l] * b[l][k]));
                        }
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threadNum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    /**
     * 矩阵转置
     * @param a 矩阵a
     * @return 矩阵a的转置
     */
    public static double[][] transposeMatrix(double[][] a) {
        double[][] b = new double[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    /**
     * 矩阵求逆
     * @param data 待转置的矩阵
     * @return 转置后的矩阵
     */
    public static double[][] inverseMatrix(double[][] data) {
        double[][] newdata = new double[data.length][data[0].length];
        double A = getMatrixResult(data);
        if (A == 0) {
            try {
                throw new Exception("矩阵不可逆");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<data.length; i++) {
            for(int j=0; j<data[0].length; j++) {
                if((i+j)%2 == 0) {
                    newdata[i][j] = getMatrixResult(getConfactor(data, i+1, j+1)) / A;
                }else {
                    newdata[i][j] = -getMatrixResult(getConfactor(data, i+1, j+1)) / A;
                }
            }
        }
        newdata = transposeMatrix(newdata);
        return newdata;
    }

    /**
     * 计算行列式的值
     * @param data 待计算的行列式
     * @return 行列式的值
     */
    public static double getMatrixResult(double[][] data) {
        //一维矩阵直接返回
        if (data.length == 1) {
            return data[0][0];
        }

        //二维矩阵计算
        if(data.length == 2) {
            return data[0][0]*data[1][1] - data[0][1]*data[1][0];
        }

        //二维以上矩阵计算
        double result = 0;
        int num = data.length;
        double[] nums = new double[num];
        for(int i=0; i<data.length; i++) {
            if(i%2 == 0) {
                nums[i] = data[0][i] * getMatrixResult(getConfactor(data, 1, i+1));
            }else {
                nums[i] = -data[0][i] * getMatrixResult(getConfactor(data, 1, i+1));
            }
        }
        for(int i=0; i<data.length; i++) {
            result += nums[i];
        }
        return result;
    }

    /**
     * 求(h,v)坐标的位置的余子式
     * @param data
     * @param h 行
     * @param v 列
     * @return 余子式的值
     */
    public static double[][] getConfactor(double[][] data, int h, int v) {
        int H = data.length;
        int V = data[0].length;
        double[][] newdata = new double[H-1][V-1];
        for(int i=0; i<newdata.length; i++) {
            if(i < h-1) {
                for(int j=0; j<newdata[i].length; j++) {
                    if(j < v-1) {
                        newdata[i][j] = data[i][j];
                    }else {
                        newdata[i][j] = data[i][j+1];
                    }
                }
            }else {
                for(int j=0; j<newdata[i].length; j++) {
                    if(j < v-1) {
                        newdata[i][j] = data[i+1][j];
                    }else {
                        newdata[i][j] = data[i+1][j+1];
                    }
                }
            }
        }
        return newdata;
    }

    /**
     * 矩阵右除
     * @param a 被除矩阵
     * @param b 除矩阵
     * @return 商矩阵
     */
    public static BigDecimal[][] rightDivMatrix(double[][] a, double[][] b) {
        return mulMatrix(a, inverseMatrix(b));
    }

    /**
     * 矩阵左除
     * @param a 被除矩阵
     * @param b 除矩阵
     * @return 商矩阵
     */
    public static BigDecimal[][] leftDivMatrix(double[][] a, double[][] b) {
        return mulMatrix(inverseMatrix(b), a);
    }
}
