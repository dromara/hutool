package cn.hutool.core.date;

import cn.hutool.core.date.DateUtil;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @ClassName EzStopWatch
 * @Description: TODO  实现耗时统计的具体类
 * <p>
 *     参考实现:
 *     @https://github.com/dromara/hutool/blob/v5-master/hutool-core/src/main/java/cn/hutool/core/date/StopWatch.java
 * </p>
 *
 * example ：
 * 1. EzStopWatch ezStopWatch = EzStopWatch.create(taskName);
 *   ezStopWatch.start();
 *   //业务逻辑
 *   TraceTask task  = ezStopWatch.stop();
 *   log.info(task.toString())
 *   //可手动清除 ezStopWatch.remove();
 * 2.EzStopWatch ezStopWatch = EzStopWatch.withName(taskName);
 *   ezStopWatch.start();
 *   //业务逻辑
 *   TraceTask task  = ezStopWatch.stop();
 *   log.info(task.toString())
 *
 * 可配置复用线程的名称包含字符
 * 配置路径：
 *  classpath:ezWatch.properties
 * 配置参数：
 *  threadMask
 * @Author: li
 * @Date: 2021/3/15 15:17
 * @Version v1.0
 **/
public class EzStopWatch{
    
    /**
     * 任务名
     */
    private String taskName;
    
    private long totalTimeNanos;
    
    private long startTimeNanos;
    
    /**
     * 判断是否已经开始执行
     */
    private AtomicInteger mark = new AtomicInteger(0);
    
    /**
     * 是否可复用
     */
    private boolean isReuse;
    
    private final static String INIT_MARK = "init";
    
    private static EzThreadLocal ezThreadLocal = new EzThreadLocal();
    
    /**
     * 保证只有指定线程才能一直持有EzStopWatch对象，防止内存泄露
     */
    private static String USABLE_THREAD_MASK = "";
    
    static {
        Properties properties = new Properties();
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(new File("classpath:ezWatch.properties"));
            properties.load(fileInputStream);
            USABLE_THREAD_MASK = properties.getProperty("threadMask");
        } catch (IOException ignored) {
        
        }
        
        if (USABLE_THREAD_MASK == null || "".equals(USABLE_THREAD_MASK)) {
            //不做任何配置的话,只有执行日志打印的线程可长期持有
            USABLE_THREAD_MASK = "log";
        }
    }
    
    /**
     * 调用此方法，请确保使用确切的线程池调用，以免影响正常业务
     * @param taskName 任务名字
     * @return 该线程持有的EzStopWatch 对象
     */
    public static EzStopWatch create(String taskName) {
        EzStopWatch ezStopWatch = ezThreadLocal.get();
        ezStopWatch.rename(taskName);
        return ezStopWatch;
    }
    
    /**
     * 由这里获得的EzStopWatch不会被复用
     * @param taskName 任务名
     * @return
     */
    public EzStopWatch withName(String taskName) {
        return new EzStopWatch(taskName,false);
    }
    
    
    /**
     * 主动清除：
     * 线程中EzStopWatch不再使用时可手动清除
     */
    public void remove() {
        ezThreadLocal.remove();
    }
    
    private EzStopWatch(String taskName,boolean isReuse) {
        this.taskName = taskName;
        this.isReuse = isReuse;
    }
    
    
    
    public void start() {
        if (mark.compareAndSet(0,1)) {
            this.startTimeNanos = System.nanoTime();
        }
        throw new IllegalStateException("Can't start StopWatch: it's already running");
    }
    
    public TraceTask stop() {
        if (mark.compareAndSet(1,0)) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        cleaner();
        final long lastTime = System.nanoTime() - this.startTimeNanos;
        this.totalTimeNanos += lastTime;
        return new TraceTask(this.taskName,this.totalTimeNanos);
    }
    
    private void rename(String newName) {
        this.taskName = newName;
    }
    
    /**
     * EzStopWatch 只在指定的线程池的线程中复用，不能是个线程就存着，容易造成内存泄露
     */
    private void cleaner() {
        if (isReuse && !Thread.currentThread().getName().contains(USABLE_THREAD_MASK)) {
            ezThreadLocal.remove();
        }
    }
    
    static class EzThreadLocal extends ThreadLocal<EzStopWatch> {
    
        /**
         * 从这里获得的EzStopWatch （不做清除操作时）必为复用的
         * @return
         */
        @Override
        protected EzStopWatch initialValue() {
            return new EzStopWatch(INIT_MARK,true);
        }
    }
 
    public static final class TraceTask {
    
        private String taskCallTime;
        private final String taskName;
        private final long timeNanos;
       
    
        public TraceTask(String taskName, long timeNanos) {
            this.taskName = taskName;
            this.timeNanos = timeNanos;
        }
    
        /**
         * 获取任务名
         *
         * @return 任务名
         */
        public String getTaskName() {
            return this.taskName;
        }
    
        /**
         * 获取任务花费时间（单位：纳秒）
         *
         * @return 任务花费时间（单位：纳秒）
         * @see #getTimeMillis()
         * @see #getTimeSeconds()
         */
        public long getTimeNanos() {
            return this.timeNanos;
        }
    
        /**
         * 获取任务花费时间（单位：毫秒）
         *
         * @return 任务花费时间（单位：毫秒）
         * @see #getTimeNanos()
         * @see #getTimeSeconds()
         */
        public long getTimeMillis() {
            return DateUtil.nanosToMillis(this.timeNanos);
        }
    
        /**
         * 获取任务花费时间（单位：秒）
         *
         * @return 任务花费时间（单位：秒）
         * @see #getTimeMillis()
         * @see #getTimeNanos()
         */
        public double getTimeSeconds() {
            return DateUtil.nanosToSeconds(this.timeNanos);
        }
    
        public TraceTask setTaskCallTime(String taskCallTime) {
            this.taskCallTime = taskCallTime;
            return this;
        }
    
        @Override
        public String toString() {
            return "TraceTask{" +
                    "任务调用时间 = '" + taskCallTime + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", 花费时间 =" + this.getTimeMillis() + "毫秒" +
                    '}' + '\n';
        }
    }
    
}
