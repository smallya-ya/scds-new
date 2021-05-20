package com.hitqz.scds.biz.battle.context;

import com.hitqz.scds.biz.battle.domain.BattleEntity;
import com.hitqz.scds.biz.battle.domain.SendDataMissionModel;
import com.hitqz.scds.biz.battle.thread.BattleRecordThread;
import com.hitqz.scds.biz.battle.thread.MapDataThread;
import com.hitqz.scds.biz.map.domain.MapEntity;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import jssc.SerialPortException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 射击演习上下文环境，包含马甲、地图、演习记录、各种线程集中管理等
 * @author hongjiasen
 */
public interface ShootingBattleContext {

    void init() throws SerialPortException, IOException;

    void start() throws InterruptedException;

    void stop() throws SerialPortException, InterruptedException;

    boolean isStop();

    BattleEntity getBattleEntity();

    Map<Integer, ShootingVestModel> getVestEntityMap();

    MapDataThread getMapDataThread();

    BattleRecordThread getBattleRecordThread();

    MapEntity getMapEntity();

    BlockingQueue<SendDataMissionModel> getRollingMissionBlockingQueue();

}
