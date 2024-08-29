package cn.hutool.core.util;

import java.util.concurrent.TimeUnit;

/**
 * 重试工具类
 * @author lianw
 */
public class RetryUtil {
    /**
     * 运行
     * @param retryCount 重试次数,默认不重试.
     * @param retryInterval 重试间隔,默认无间隔
     * @param timeUnit 间隔时间单位,默认秒
     * @param runnable 运行体,不能为null
     */
    public static void run(Integer retryCount, Long retryInterval, TimeUnit timeUnit, Runnable runnable){
        if(retryCount == null || retryCount < 0){
            retryCount = 0;
        }
        if(retryInterval == null || retryInterval < 0L){
            retryInterval = 0L;
        }
        if(timeUnit == null){
            timeUnit = TimeUnit.SECONDS;
        }
        if(runnable == null){
            throw new IllegalArgumentException("运行体不能为null");
        }
        int count = 0;
        while (count <= retryCount) {
            try {
                runnable.run();
                break;
            } catch (Exception e) {
                count++;
                if(count >= retryCount){
                    throw e;
                }else{
                    try {
                        timeUnit.sleep(retryInterval);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }
    }
}
