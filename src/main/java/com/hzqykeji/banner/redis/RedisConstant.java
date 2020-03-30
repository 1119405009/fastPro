package com.hzqykeji.banner.redis;


import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * @Author felix
 * @Description
 * @Date 17:52
 */
public class RedisConstant {

    private static ObjectMapper mapper = new ObjectMapper();
    public final static String  PROJECT="CXAPP";
    public final static String  GOODS ="goods";
    public final static String  TRAVEL ="travel";


    public static String delGoodsKeys() {
        return PROJECT + ":" + GOODS + ":" + "*";
    }

    public static String delTravelKeys() {
        return PROJECT + ":" + TRAVEL + ":" + "*";
    }


}
