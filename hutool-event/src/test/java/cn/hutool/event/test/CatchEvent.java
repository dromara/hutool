package cn.hutool.event.test;

import cn.hutool.event.domain.BaseEvent;

/**
 * Class CatchEvent...
 *
 * @author Ted.L
 * Created on 2019-09-28
 */
public class CatchEvent extends BaseEvent {

    private String msg = "Tom抓了第{%d}只Jerry";

    /**
     * 构造函数
     *
     * @param source 产生事件的事件源
     * @param id     事件id
     */
    public CatchEvent(Object source, String id) {
        super(source, id);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
