package com.hitqz.scds.biz.battle.domain;

import java.util.Comparator;

/**
 * 优先级队列任务优先级比较器
 * 优先级顺序：上位机下发给马甲的各种操作指令 > 上位机下发给马甲的轮询指令
 * @author hongjiasen
 */
public class SendDataMissionModelComparator implements Comparator<SendDataMissionModel> {

    @Override
    public int compare(SendDataMissionModel o1, SendDataMissionModel o2) {
        return - (o1.getData()[0] - o2.getData()[0]);
    }
}
