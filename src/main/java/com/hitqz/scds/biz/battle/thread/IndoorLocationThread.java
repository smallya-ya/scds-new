package com.hitqz.scds.biz.battle.thread;

import com.hitqz.scds.biz.battle.context.ShootingBattleContext;
import com.hitqz.scds.biz.battle.service.HandleService;

import java.util.concurrent.BlockingQueue;

public class IndoorLocationThread extends InstructionProcessThread {

    public IndoorLocationThread(ShootingBattleContext context, BlockingQueue<byte[]> dataQueue, HandleService handleService) {
        super(context, dataQueue, handleService);
    }
}
