package com.hitqz.scds.biz.battle.handler;

import java.util.HashMap;
import java.util.Map;

public class DataHandlerContext {

    private static Map<Byte, CommonDataHandler> dataHandlerMap = new HashMap<Byte, CommonDataHandler>() {{
        put(Byte.valueOf("03", 16), new HeartbeatHandler());
        put(Byte.valueOf("04", 16), new OutdoorGPSHandler());
        put(Byte.valueOf("05", 16), new HitRecordHandler());
        put(Byte.valueOf("06", 16), new IndoorLocationHandler());
    }};

    public static CommonDataHandler getHandler(byte dataType) {
        return dataHandlerMap.getOrDefault(dataType, new DefaultHandler());
    }
}
