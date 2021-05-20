package com.hitqz.scds.biz.battle.handler;

public class DefaultHandler extends CommonDataHandler {

    @Override
    public void handle(byte[] data) {
        log.error("未找到指令类型为{}的处理器，抛弃该数据帧", data[1]);
    }
}
