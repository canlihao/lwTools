package com.canlihao.lwutil;

/*******
 *
 *Created by 李魏
 *
 *创建时间 2020/1/17
 *
 *类描述：
 *
 ********/
public class NetChangeUtil {
    private static NetChangeUtil util;

    /**
     * -1  无网络
     * 0  移动网
     * 1  wifi
     */
    String lastNetState;

    public static NetChangeUtil getInstance(){
        if(null == util){
            synchronized (NetChangeUtil.class){
                if(null == util){
                    util = new NetChangeUtil();
                }
            }
        }
        return util;
    }

    public String getLastNetState() {
        return lastNetState;
    }

    public NetChangeUtil setLastNetState(String lastNetState) {
        this.lastNetState = lastNetState;
        return this;
    }
}
