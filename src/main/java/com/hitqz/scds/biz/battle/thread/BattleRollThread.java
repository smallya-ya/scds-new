package com.hitqz.scds.biz.battle.thread;

import com.hitqz.scds.biz.battle.domain.SendDataMissionModel;
import com.hitqz.scds.biz.shootingvest.domian.ShootingVestModel;
import com.hitqz.scds.common.Constant;
import com.hitqz.scds.utils.DataFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public class BattleRollThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Collection<ShootingVestModel> vestModelList;
    private BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue;

    public BattleRollThread(Collection<ShootingVestModel> vestModelList, BlockingQueue<SendDataMissionModel> rollingMissionBlockingQueue) {
        this.vestModelList = vestModelList;
        this.rollingMissionBlockingQueue = rollingMissionBlockingQueue;
    }

    @Override
    public void run() {
        log.info("[轮询线程]：启动");
        for (ShootingVestModel vestModel : vestModelList) {
            if (vestModel.getHp() == 0 && vestModel.getMode() == Constant.DRILL_MODE && vestModel.getManualLiveFlag() != 1) {
                log.info("[轮询线程]：{}号阵亡，跳过轮询", vestModel.getNum());
                continue;
            }
            SendDataMissionModel data = SendDataMissionModel.SendDataMissionModelBuilder
                    .aSendDataMissionModel()
                    .vestNum(vestModel.getNum())
                    .isWait(true)
                    .data(DataFrameUtils.createPollingData(vestModel))
                    .build();
            try {
                rollingMissionBlockingQueue.put(data);
            } catch (InterruptedException e) {
                log.error("[轮询线程]：下发轮询指令时出现异常", e);
                continue;
            } catch (Exception e) {
                log.error("[轮询线程]：下发轮询指令时出现异常", e);
                continue;
            }
        }
    }

}
