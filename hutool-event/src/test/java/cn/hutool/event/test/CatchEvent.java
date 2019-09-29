package cn.hutool.event.test;

import cn.hutool.event.domain.BaseEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class CatchEvent...
 *
 * @author Ted.L
 * Created on 2019-09-28
 */
public class CatchEvent extends BaseEvent {

    private String msg = "Tom抓了第{%d}只Jerry";

    private CountDownLatch cd;

    private AtomicLong dealTimes = new AtomicLong(0);

    public AtomicLong getDealTimes() {
        return dealTimes;
    }

    /**
     * 构造函数
     *
     * @param source 产生事件的事件源
     * @param id     事件id
     */
    public CatchEvent(Object source, String id, CountDownLatch cd) {
        super(source, id);
        this.cd = cd;
    }

    public CountDownLatch getCd() {
        return cd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
